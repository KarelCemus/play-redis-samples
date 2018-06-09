import javax.inject.{Inject, Provider, Singleton}

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

import play.api.Configuration
import play.api.cache.redis.CacheAsyncApi
import play.api.cache.redis.configuration._
import play.cache.NamedCacheImpl

import com.google.inject.{AbstractModule, Key}

/**
  * @author Karel Cemus
  */
class CustomCacheModule extends AbstractModule {

  def configure( ) = {
    // register custom configuration provider
    bind( classOf[ RedisInstance ] ).annotatedWith( new NamedCacheImpl( "play" ) ).to( classOf[ CustomRedisInstance ] )

    // get the default API implementation
    val defaultImpl = getProvider( Key.get( classOf[ CacheAsyncApi ], new NamedCacheImpl( "play" ) ) )

    // override it by custom implementation
    bind( classOf[ CacheAsyncApi ] ).annotatedWith( new NamedCacheImpl( "custom" ) ).toProvider( new CustomizedCacheAsyncApiProvider( defaultImpl ) )
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


class CustomizedCacheAsyncApiProvider( default: Provider[ CacheAsyncApi ] ) extends Provider[ CacheAsyncApi ] {
  lazy val get = {
    default.get
  }
}

class CustomizedCacheAsyncApi( internal: CacheAsyncApi ) extends CacheAsyncApi {
  def get[ T: ClassTag ]( key: String ) = internal.get[ T ]( key )
  def getAll[ T: ClassTag ]( key: String* ) = internal.getAll[ T ]( key: _* )
  def getOrElse[ T: ClassTag ]( key: String, expiration: Duration )( orElse: => T ) = internal.getOrElse[ T ]( key, expiration )( orElse )
  def getOrFuture[ T: ClassTag ]( key: String, expiration: Duration )( orElse: => Future[ T ] ) = internal.getOrFuture[ T ]( key, expiration )( orElse )
  def exists( key: String ) = internal.exists( key )
  def matching( pattern: String ) = internal.matching( pattern )
  def set( key: String, value: Any, expiration: Duration ) = internal.set( key, value, expiration )
  def setIfNotExists( key: String, value: Any, expiration: Duration ) = internal.setIfNotExists( key, value, expiration )
  def setAll( keyValues: (String, Any)* ) = internal.setAll( keyValues: _* )
  def setAllIfNotExist( keyValues: (String, Any)* ) = internal.setAllIfNotExist( keyValues: _* )
  def append( key: String, value: String, expiration: Duration ) = internal.append( key, value, expiration )
  def expire( key: String, expiration: Duration ) = internal.expire( key, expiration )
  def remove( key: String ) = internal.remove( key )
  def remove( key1: String, key2: String, keys: String* ) = internal.remove( key1, key2, keys: _* )
  def removeAll( keys: String* ) = internal.removeAll( keys: _* )
  def removeMatching( pattern: String ) = internal.removeMatching( pattern )
  def invalidate( ) = internal.invalidate()
  def increment( key: String, by: Long ) = internal.increment( key, by )
  def decrement( key: String, by: Long ) = internal.decrement( key, by )
  def list[ T: ClassTag ]( key: String ) = internal.list[ T ]( key )
  def set[ T: ClassTag ]( key: String ) = internal.set[ T ]( key )
  def map[ T: ClassTag ]( key: String ) = internal.map[ T ]( key )
}