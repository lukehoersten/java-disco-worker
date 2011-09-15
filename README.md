Java Disco Worker
=================

[Disco](http://discoproject.org) Java
[Worker](http://discoproject.org/doc/howto/worker.html) implementation
with a focus on keeping things *simple* and *type-safe*. The goal is
to allow map-reduce jobs to be submitted and run from a *single Java
jar*.

*NOTE:* This project is still very beta and in active development. I'm
operating under the "release early, release often" principle.

Dependencies
------------

* [JSON-Java](https://github.com/douglascrockford/JSON-java) -
  Unfortunately no Jar is produced for this dependency so I may end up
  including one in the BitBucket downloads second.

To-Do List
----------

### Generic

* Reduce phase doesn't correctly handle the `dir://` input scheme.
* Outputs aren't correctly reported as relative to the jobhome.
* Java [JobPack](http://discoproject.org/doc/howto/jobpack.html)
  implementation is incomplete. Currently the CLI `disco job` command
  is used to submit jobs.

### DiscoTaskInputFetcher

* Doesn't support including and excluding based on inputs
  which have already been downloaded
* Doesn't handle fail, retry, or pause messages.
* Doesn't support failed inputs.
* Doesn't implement the local
  [filesystem optimization](http://discoproject.org/doc/howto/worker.html#input)
  instead of HTTP access when input files are already locally
  available.
