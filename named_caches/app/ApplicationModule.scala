import play.api.cache.redis.RecoveryPolicy
import play.api.inject.Module
import play.api.{Configuration, Environment}

/**
  * @author Karel Cemus
  */
class ApplicationModule extends Module {

  def bindings( environment: Environment, configuration: Configuration ) = {
    Seq(
      // register custom recovery policy as a named policy
      bind[ RecoveryPolicy ].qualifiedWith( "custom" ).to[ CustomRecoveryPolicy ]
    )
  }
}
