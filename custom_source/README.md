<div align="center">

  # Example of custom source and API

</div>

Advanced example of use of the play-redis library. 
It uses custom source and overrides default API.

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
// provide additional configuration in the custom module
play.modules.enabled += CustomCacheModule
```

## Use

Third, configure the `RedisInstance` and register the binding.
You must implement either `RedisStandalone` or `RedisCluster`.
You can also implement `RedisDelegatingSettings` to ease their
implementation. In that case, the settings can be loaded from
the conf file, as is illustrated in the example.


```scala
import javax.inject.Inject

import play.api.Configuration
import play.api.cache.redis.configuration._
import play.cache.NamedCacheImpl

import com.google.inject.AbstractModule

class CustomCacheModule extends AbstractModule {

  def configure( ) = {
    // register custom configuration provider
    bind( classOf[ RedisInstance ] ).annotatedWith( new NamedCacheImpl( "play" ) ).to( classOf[ CustomRedisInstance ] )
  }
}


@Singleton
class CustomRedisInstance @Inject()( configuration: Configuration ) extends RedisStandalone with RedisDelegatingSettings {

  def name = "play"

  private def defaultSettings = RedisSettings.load(
    // this should always be "play.cache.redis" 
    // as it is the root of the configuration with all defaults
    configuration.underlying, "play.cache.redis"
  )

  def settings = {
    RedisSettings.withFallback( defaultSettings ).load(
      // this is the path to the actual configuration of the instance
      //
      // in case of named caches, this could be, e.g., "play.cache.redis.instances.my-cache"
      //
      // in that case, the name of the cache is "my-cache" and has to be considered in 
      // the bindings in the CustomCacheModule (instead of "play", which is used now) 
      configuration.underlying, "play.cache.redis"
    )
  }

  def host = "localhost"

  def port = 6379

  def database = Some( 1 )

  def password = None
}


```

You can also implement your own API or extend default.

```scala

import play.api.Configuration
import play.api.cache.redis.CacheAsyncApi
import play.api.cache.redis.configuration._
import play.cache.NamedCacheImpl

import com.google.inject.{AbstractModule, Key}

class CustomCacheModule extends AbstractModule {

  def configure( ) = {

    // get the default API implementation
    val defaultImpl = getProvider( Key.get( classOf[ CacheAsyncApi ], new NamedCacheImpl( "play" ) ) )
    
    // override it by custom implementation
    bind( classOf[ CacheAsyncApi ] ).annotatedWith( new NamedCacheImpl( "custom" ) ).toProvider( new CustomizedCacheAsyncApiProvider( defaultImpl ) )
  }
}
```
