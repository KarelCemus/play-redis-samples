normalizedName := "play-redis-samples"

name := "Samples using Redis Cache for Play"

description := "Set of samples using Redis cache plugin for the Play framework 2"


lazy val root = Sample(id = "root", path = ".").aggregate(helloWorld)

lazy val helloWorld = Sample("hello_world")
