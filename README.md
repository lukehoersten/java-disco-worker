Java Disco Worker ReadMe
========================

[Disco](http://discoproject.org) Java Worker implementation with a
focus on keeping things *simple* and *type-safe*. The goal is to allow
map-reduce jobs to be submitted and run from a *single Java jar*.

To-do List
----------

### Generic

* Reduce phase doesn't correctly handle the `dir://` input scheme.
* Outputs aren't correctly reported as relative to the jobhome.
* Java JobPack implementation is incomplete. Currently the CLI `disco
  job` command is used to submit jobs.

### DiscoTaskInputFetcher

* Doesn't support including and excluding based on inputs
  which have already been downloaded
* Doesn't handle fail, retry, or pause messages.
* Doesn't support failed inputs.
* Doesn't implement the local filesystem optimization instead of HTTP
  access when input files are already locally available.
