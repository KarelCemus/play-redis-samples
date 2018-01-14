normalizedName := "play-redis-samples"

name := "Samples using Redis Cache for Play"

description := "Set of samples using Redis cache plugin for the Play framework 2"


lazy val root = Sample(id = "root", path = ".").aggregate(helloWorld, namedCaches)

lazy val helloWorld = Sample("helloWorld", "hello_world")

lazy val namedCaches = Sample("namedCaches", "named_caches")

lazy val ehCache = Sample("ehCache", "redis_and_ehcache")
