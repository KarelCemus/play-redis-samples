import javax.inject.Singleton

import scala.concurrent.Future

import play.api.Logger
import play.api.cache.redis._


@Singleton
class CustomRecoveryPolicy extends RecoveryPolicy {

  val logger = Logger( "CustomRecoveryPolicy" )

  def recoverFrom[ T ]( rerun: => Future[ T ], default: => Future[ T ], failure: RedisException ) = {
    logger.warn( "Cache invocation failed, recovering with a neutral value." )
    default
  }
}
