<div align="center">

  # Samples for Redis Cache<br/>module for Play framework

  **This version supports Play framework 2.6.x with JDK 8 and both Scala 2.11 and Scala 2.12.**<br/>
  **For previous versions see older releases.**

  [![Travis CI: Status](https://travis-ci.org/KarelCemus/play-redis-samples.svg?branch=master)](https://travis-ci.org/KarelCemus/play-redis-samples)

</div>


## About the project

[The `play-redis` module](https://github.com/KarelCemus/play-redis/) for 
Play framework 2 adds support of Redis cache 
server and provides a set of handful APIs. For more details and 
[full documentation](https://github.com/KarelCemus/play-redis/wiki) for the version 2.x.x 
and newer please see [the wiki](https://github.com/KarelCemus/play-redis/wiki). 
For the documentation of older versions see README at corresponding tag in git history
of [the module](https://github.com/KarelCemus/play-redis/).

This project provides a set of examples showing the use of `play-redis` modules.

1. [**Getting Started**](https://github.com/KarelCemus/play-redis-samples/tree/master/hello_world) is a very basic example showing the 
minimal configuration required to use the redis cache

1. [**Named Caches**](https://github.com/KarelCemus/play-redis-samples/tree/master/named_caches) is the advanced example with custom recovery policy and multiple named caches.

1. [**EhCache and Redis**](https://github.com/KarelCemus/play-redis-samples/tree/master/redis_and_ehcache) shows a combination of both caching provides used at once. 
While the EhCache is bound to unqualified APIs, the Redis cache uses named APIs.

For more details about these example please **read the README within each project**.

## Versioning

These samples are **versioned along the play-redis with the same version number**. 
To visit the samples for the particular version of play-redis please [**visit
these samples at the same version**](https://github.com/KarelCemus/play-redis-samples/releases).

## Compatibility matrix

| play framework  | play-redis     |
|-----------------|---------------:|
| 2.6.x           | 2.0.1  ([Migration Guide](https://github.com/KarelCemus/play-redis/wiki/Migration-Guide))        |
| 2.5.x           | 1.4.2          |
| 2.4.x           | 1.0.0          |
| 2.3.x           | 0.2.1          |


## Changelog

For the list of changes and migration guide please see
[the Changelog](https://github.com/KarelCemus/play-redis/blob/master/CHANGELOG.md) 
in the original module repository.


## Contribution

If you encounter any issue, have a feature request, or just
like this library, please feel free to report it or contact me.
