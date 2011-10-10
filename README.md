Java Disco Worker
=================

Overview
--------

[Disco](http://discoproject.org) Java
[Worker](http://discoproject.org/doc/howto/worker.html) implementation
with a focus on keeping things *simple* and *type-safe*. The goal is
to allow [map-reduce](http://en.wikipedia.org/wiki/Map-reduce) jobs to
be submitted and run from a *single Java jar*.

*NOTE:* This project is still very beta and in active development. I'm
operating under the "release early, release often" principle.


Usage
-----

### Overview

MapReduce basically has three steps. The first step, called the
"client" by Disco, defines the MapReduce "job" (input space and
references to the map and reduce functions) and submits the "job" to
Disco. The second step is the actual "map" function which runs for
each input. The third step is the reduce which takes the results of
all the map functions and combines them if needed.

*The disco command line tool must be installed on the box used to
 submit the job.*

### Implementing

1. Define the map and reduce phases by implementing the
`DiscoMapFunction` interface which has a `map` function. This function
will do the work to be done in parallel on the cluster. Do the same
for `DiscoReduceFunction`.

2. Make a `main` function which creates the `DiscoJob`. This is the "client".

    1. The `DiscoJob` describes the input space and takes three arguments:
        * `jobName`
        * `inputs` - Each element of this list will have a map instance run.
        * `args` - Fixed arguments to pass to each map run.

    2. Call `setMapFunction` on the `DiscoJob` instance with the class containing the map
    function to run for each input. Do the same with `setReduceFunction`.

    3. Submit the job with a call to `submit` on the `DiscoJob`
    instance.

3. Set the `DISCO_MASTER` environment variable to point to the master
    node. For example, something like `export
    DISCO_MASTER=disco://localhost:8989`.

4. Pack the `java-disco-worker-*.jar`, `DiscoMapFunction` implementation, and
`DiscoReduceFunction` implementation into the same jar with your
client `main` function. Run the jar with `java -cp <your.jar>
yourMainClass`. The VM args and Classpath will be passed on to the
worker as well.


Hacking
-------

### Build Dependencies

* [Mockito](http://code.google.com/p/mockito/) Test framework.
* [JUnit](http://www.junit.org/)


### To-Do List

#### Generic

* Reduce phase doesn't correctly handle the `dir://` input scheme.
* Outputs aren't correctly reported as relative to the jobhome.
* Java [JobPack](http://discoproject.org/doc/howto/jobpack.html)
  implementation is incomplete. Currently the CLI `disco job` command
  is used to submit jobs.

#### DiscoTaskInputFetcher

* Doesn't support including and excluding based on inputs
  which have already been downloaded
* Doesn't handle fail, retry, or pause messages.
* Doesn't support failed inputs.
* Doesn't implement the local
  [filesystem optimization](http://discoproject.org/doc/howto/worker.html#input)
  instead of HTTP access when input files are already locally
  available.

### Repositories & Issues

* [Git Mirror](https://github.com/LukeHoersten/java-disco-worker/)
* [Mercurial Mirror](http://code.dirigible.co/java-disco-worker/)

Please submit bugs to the [GitHub Issue Tracker](https://github.com/LukeHoersten/java-disco-worker/issues/)
