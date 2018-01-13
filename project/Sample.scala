import play.sbt.PlayScala

import sbt.Keys._
import sbt._

/**
  * @author Karel Cemus
  */
object Sample {

  private val settings = Seq(

    organization := "com.github.karelcemus",

    scalaVersion := "2.12.4",

    crossScalaVersions := Seq( "2.11.12", scalaVersion.value ),

    libraryDependencies ++= Seq(
      // play framework cache API
      "com.github.karelcemus" %% "play-redis" % version.value
    ),

    resolvers ++= Seq(
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      Resolver.sonatypeRepo( "snapshots" )
    ),

    javacOptions ++= Seq( "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-encoding", "UTF-8" ),

    scalacOptions ++= Seq( "-deprecation", "-feature", "-unchecked" ),

    homepage := Some( url( "https://github.com/karelcemus/play-redis-samples" ) ),

    licenses := Seq( "Apache 2" -> url( "http://www.apache.org/licenses/LICENSE-2.0" ) )
  )

  def apply( id: String ): Project = apply( id, path = id )

  def apply( id: String, path: String ): Project =
    Project( id, file( path ) )
      .enablePlugins( PlayScala )
      .settings( settings )
}
