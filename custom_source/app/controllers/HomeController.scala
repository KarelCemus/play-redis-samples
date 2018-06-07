package controllers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger
import javax.inject._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

import play.api.cache.redis.CacheAsyncApi
import play.api.mvc._
import play.cache.NamedCache

/**
  *
  *
  * Very simple example of use of the play-redis library.
  *
  * First, import the play-redis library. See `Sample` class
  * declaring the dependencies.
  *
  * Second, enable the RedisCacheModule. See `application.conf`.
  *
  * Third, pick the API, inject it and use it. Here, we use
  * asynchronous advanced API with lot of handful methods
  * enabling us fully use the operations of Redis server.
  * Besides this, there are 2 API provided by Play itself
  * and one synchronous API provided by the play-redis. See
  * the project wiki for more details.
  *
  *
  */
@Singleton
class HomeController @Inject()( cache: CacheAsyncApi, @NamedCache( "custom" ) custom: CacheAsyncApi, cc: ControllerComponents )( implicit executionContext: ExecutionContext ) extends AbstractController( cc ) {
  import Imports._

  /**
    *
    * Using getOrElse method get or compute and save a simple String
    *
    */
  private def message = cache.getOrElse( "hello-world#message", expiration = 10.seconds ) {
    s"This message was created at ${ now.asString }."
  }


  /**
    *
    * Using getOrElse method get or compute and save a custom object
    *
    *
    */
  private def obj = cache.getOrElse( "hello-world#object", expiration = 8.seconds ) {
    MyObject.next
  }


  def index = Action.async {
    //
    // asynchronously get both cached values
    // construct the result
    //
    for {
      message <- this.message
      obj <- this.obj
    } yield {
      Ok( views.html.index( message, obj, now.asString ) )
    }
  }

}

/**
  *
  *
  * Example of a simple object, which is also support by play-redis.
  * However, keep in mind, that by default it uses very inefficient
  * java serialization and you should use a different mechanism. For
  * more details see the project wiki of the advanced example illustraing
  * use of Kryo library.
  *
  *
  */
case class MyObject( index: Int, createdAt: LocalDateTime ) extends Serializable {
  import Imports._

  def createdAtString = createdAt.asString
}

object MyObject {

  import Imports._

  // atomic integer used to prevent concurrency issues and race conditions
  private val nextIndex = new AtomicInteger( 1 )

  def next = MyObject( index = nextIndex.getAndIncrement(), now )
}

/**
  *
  *
  * Unimportant helpers easing the code readability
  *
  *
  */
object Imports {

  implicit class RichDate( val date: LocalDateTime ) extends AnyVal {
    def asString = DateTimeFormatter.ofPattern( "HH:mm:ss" ).format( date )
  }

  def now = LocalDateTime.now()
}
