package controllers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
  import Imports._

  private def message( instance: CacheAsyncApi, name: String ) = instance.getOrElse( "named-caches#message", expiration = 10.seconds ) {
    s"This message was set to $name instance at ${ now.asString }."
  }

  private def messageInDefault = message( cache, "default" )

  private def messageInLocal = message( local, "local" )

  private def messageInRemote = message( remote, "remote" )

  def messageInFailing = message( failing, "failing" )

  def index = Action.async {
    for {
      default <- this.messageInDefault
      local <- this.messageInLocal
      remote <- this.messageInRemote
      failing <- this.messageInFailing
    } yield {
      Ok( views.html.index( default, local, remote, failing, now.asString ) )
    }
  }

}

object Imports {

  implicit class RichDate( val date: LocalDateTime ) extends AnyVal {
    def asString = DateTimeFormatter.ofPattern( "HH:mm:ss" ).format( date )
  }

  def now = LocalDateTime.now()
}
