import play.sbt.PlayImport

normalizedName := "redis-and-ehcache"

name := "Redis and EhCache"

description := "Example of simultaneous use of play-redis and ehcache"

libraryDependencies += PlayImport.ehcache
