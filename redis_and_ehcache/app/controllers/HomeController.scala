package controllers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

import play.api.cache._
import play.api.mvc._

@Singleton
class HomeController @Inject()
(
  // ehcache
  ehcache: AsyncCacheApi,
  // redis instance
  @NamedCache( "redis" ) redis: AsyncCacheApi,
  // components
  cc: ControllerComponents

)( implicit executionContext: ExecutionContext ) extends AbstractController( cc ) {
  import Imports._

  private def message( instance: AsyncCacheApi, name: String ) = instance.getOrElseUpdate( "redis-and-ehcache#message", expiration = 5.seconds ) {
    Future successful s"This message was set to $name instance at ${ now.asString }."
  }

  private def messageInEhCache = message( ehcache, "ehcache" )

  private def messageInRedis = message( redis, "redis" )

  def index = Action.async {
    for {
      messageInEhCache <- this.messageInEhCache
      messageInRedis <- this.messageInRedis
    } yield {
      Ok( views.html.index( messageInEhCache, messageInRedis, now.asString ) )
    }
  }

}

object Imports {

  implicit class RichDate( val date: LocalDateTime ) extends AnyVal {
    def asString = DateTimeFormatter.ofPattern( "HH:mm:ss" ).format( date )
  }

  def now = LocalDateTime.now()
}
