<div align="center">

  # Getting started with play-redis

</div>

Very simple example of use of the play-redis library. 
There is no customization nor special configuration.
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
  PlayImport.cacheApi
)
```

Second, enable the RedisCacheModule. See `application.conf`.

```hocon
play.modules.enabled += play.api.cache.redis.RedisCacheModule
```

## Use

Third, pick the API, inject it and use it. Here, we use
asynchronous advanced API with lot of handful methods
enabling us fully use the operations of Redis server.

Besides this, there are 2 API provided by Play itself
and one synchronous API provided by the play-redis. [the
project wiki](https://github.com/KarelCemus/play-redis/wiki#provided-apis).


```scala

import javax.inject._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

import play.api.cache.redis.CacheAsyncApi
import play.api.mvc._

class HomeController @Inject()( cache: CacheAsyncApi, cc: ControllerComponents )( implicit executionContext: ExecutionContext ) extends AbstractController( cc ) {

  private def message = cache.getOrElse( "hello-world#message", expiration = 10.seconds ) {
    "This is a sample message."
  }

  def index = Action.async {
    message.map( msg => Ok( views.html.index( msg ) ) )
  }
}

```
