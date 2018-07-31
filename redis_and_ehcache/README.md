<div align="center">

  # EhCache together with Redis cache

</div>

Advanced example shows the use of local EhCache together with Redis cache. 
It uses the mechanism of named caches, where the EhCache is bound to unqualified
APIs while the Redis cache is named `redis`. 
There is no  further customization nor special configuration.
For any of those please see more advanced examples or [the
project wiki](https://github.com/KarelCemus/play-redis/wiki)

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
  PlayImport.cacheApi,
  // EhCache
  PlayImport.ehcache
)
```

Second, enable the RedisCacheModule. See `application.conf`.

```hocon
play.modules.enabled += play.api.cache.redis.RedisCacheModule
```

Third, disable the binding of play-redis to unqualified APIs
and rename the default redis cache to `redis`. Otherwise it
would be `play` which would collide with EhCache's default.

```hocon
play.cache.redis {
  # do not bind default unqualified APIs
  bind-default: false

  # name of the instance in simple configuration,
  # i.e., not located under `instances` key
  # but directly under 'play.cache.redis'
  default-cache: "redis"
}

```

## Use

Finally, pick the API, inject it and use it. Here, we use
asynchronous advanced API with lot of handful methods
enabling us fully use the operations of Redis server.

Besides this, there are 2 API provided by Play itself
and one synchronous API provided by the play-redis. [the
project wiki](https://github.com/KarelCemus/play-redis/wiki#provided-apis).


```scala

import javax.inject._

import scala.concurrent.ExecutionContext

import play.api.cache._
import play.api.mvc._

class HomeController @Inject()
(
  // ehcache
  ehcache: AsyncCacheApi,
  // redis instance
  @NamedCache( "redis" ) redis: AsyncCacheApi,
  // components
  cc: ControllerComponents

)( implicit executionContext: ExecutionContext ) extends AbstractController( cc ) {

    // custom implementation
}

```
