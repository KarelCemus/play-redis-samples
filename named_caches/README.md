<div align="center">

  # Named Caches

</div>

Advanced example with multiple named caches and custom recovery policy
build with the play-redis library. For additional configuration please
[see the project wiki](https://github.com/KarelCemus/play-redis/wiki)

## Integration

First, import the play-redis library. See `Sample` class
declaring the dependencies.

```sbt
libraryDependencies ++= Seq(
  // play framework cache API
  "com.github.karelcemus" %% "play-redis" % version.value,
  // runtime DI
  PlayImport.guice,
  // runtime DI
  PlayImport.cacheApi
)
```

Second, configure the named caches, enable the RedisCacheModule and enable 
the ApplicationModule configuring the `CustomRecoveryPolicy`. See `application.conf`.

```hocon
play.cache.redis {
  # bind the instance to unqualified APIs
  bind-default:   true
  # unqualified APIs bind to "local" instance
  default-cache:  "local"

  instances {

    "local" : {
      host:       localhost
      port:       6379
      database:   1
    }

    "remote" : {
      host:       localhost
      port:       6379
      database:   2
    }

    "failing" : {
      host:       localhost
      port:       6380 # there is no running instance! The connection fails
      database:   1
      recovery:   "custom"
    }
  }

play.modules.enabled += play.api.cache.redis.RedisCacheModule
play.modules.enabled += ApplicationModule
```

## Custom Recovery Policy

The `failing` instance uses custom recovery policy. First, we implement it. See `CustomRecoveryPolicy.scala`

```scala
import javax.inject.Singleton
import scala.concurrent.Future
import play.api.cache.redis._

@Singleton
class CustomRecoveryPolicy extends RecoveryPolicy {

  def recoverFrom[ T ]( rerun: => Future[ T ], default: => Future[ T ], failure: RedisException ) = {
    // recover with default neutral value
    default
  }
}

```

## Use

Third, pick the API, inject the named or unqualified cache and use it. 
Here, we use asynchronous advanced API with lot of handful methods
enabling us fully use the operations of Redis server.

Besides this, there are 2 API provided by Play itself
and one synchronous API provided by the play-redis. [the
project wiki](https://github.com/KarelCemus/play-redis/wiki#provided-apis).


```scala

import javax.inject._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

import play.api.cache._
import play.api.cache.redis.CacheAsyncApi
import play.api.mvc._

@Singleton
class HomeController @Inject()
(
  // default unqualified instance, equal to "local"
  cache: CacheAsyncApi,
  // instance with DB 1
  @NamedCache( "local" ) local: CacheAsyncApi,
  // instance with DB 2
  @NamedCache( "remote" ) remote: CacheAsyncApi,
  // instance with a custom recovery policy
  @NamedCache( "failing" ) failing: CacheAsyncApi,
  cc: ControllerComponents

)( implicit executionContext: ExecutionContext ) extends AbstractController( cc ) {

    // customized use of APIs
}

```
