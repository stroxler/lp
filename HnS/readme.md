# Hadoop and Spark

Code for installing and learning some of the basics of Hadoop
and Spark.

If I get around to other related frameworks (Storm, Hive, Scalding, etc) then
the gettings-started examples and install / config scripts will also go here.

# Install

## Underlying frameworks

You need to have java installed to run hadoop, and you need maven or gradle
to build most simple examples. You can find install scripts in `ape2`.

You do *not* need scala or sbt installed to try out the spark shell, but to
build a lot of scala examples you do. Again, you can find install scripts
in `ape2`

## Hadoop

The `setup/install_hadoop.sh` script will install hadoop into `/opt`. In
order to run singlenode mode, you will need to have passwordless ssh to
localhost; if you do not have this, `setup/setup_ssh.sh` sets it up. You
may also need to run `setup/java_home_setup.sh` because for some reason
hadoop does not pick up environment vars defined in `/etc/profile.d`, which
is where I usually define `JAVA_HOME.`

## Spark

To install spark in concert with the hadoop 2.7.1 distro whose install script
lives at `../hadoop/setup/install_hadoop.sh`, you can run the install
script here (which installs from a build tarball, not from source).

Unlike hadoop, there is no configuration needed for either stand-alone
or under-singlenode-yarn spark use.

# Configuring and running daemons

## hadoop

The hadoop install script above creates `.orig` backups of everything
in `hadoop/etc/hadoop`; the scripts described here rely on this fact and the
fact that hadoops default conf is for standalone mode.

### standalone

You do not need to do anything to a fresh install to run standalone map-reduce
tasks against plain text files. However, if you have had a one-node yarn
cluster running, then `setup/standalone.sh` will tear it down.

### single-node hdfs and yarn

In order to run hdfs you first need to format it. The script
`setup/singlenode_format` will do so; it should only need to be done once per
machine per power cycle (the stuff it makes lives in /tmp so you may need to
do it again if you reboot).

Then, you can start the hdfs and yarn daemons - including setting the conf
files appropriately for a single-node cluster - by running
`setup/singlenode.sh`.

## spark

For my purposes, Spark actually does not require any configuration prior to
running; it gets all the information it needs from runtime flags plus any
existing hadoop conf.

# Running simple examples

## hdfs

The key command-line operation for hadoop is
```
hadoop fs <cmd>
```
where command has a leading dash but otherwise comes from a subset of the
ordinary linux filesystem commands, e.g. `hadoop fs -mkdir /somedir`.

For example, to work with the artists / songs toy data, you can do (from
this directory)
```
hadoop fs -mkdir /music
hadoop fs -put test_data/artists.txt /music
hadoop fs -put test_data/songs.txt /music
```
If you want to confirm that it worked, you can run
`hadoop fs -cat /music/songs.txt`.

Another easy way to make or view existing hdfs files is via the spark command
line, more on that later.

## hadoop mapreduce

### Standalone mode

Although useless as far as big data goes, this is actually the default hadoop
mode and the way you probably want to test most programs.

To run a mapreduce job, when configured for standalone mode, just run
```
hadoop jar ex/mapreduce_wc/target/wcex-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
  wcex.WC ./test_data/songs.txt /tmp/wc_out
```
The output is a directory, not a file. It has the same format that it would if
run in hdfs, as described in the following subsection.

### Singlenode mode

Once you have reconfigured hadoop for single-node cluster mode and started
the hdfs and yarn daemons, you can run the equivalent command in cluster mode:
```
hadoop jar ex/mapreduce_wc/target/wcex-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
  wcex.WC /music/songs.txt /music/songs_wc
```
You can check the output, which is a directory, by running
```
hadoop fs -ls /music/songs_wc
```
which should show a `_SUCCESS` file and one output file, and then viewing
the output by running
```
hadoop fs -cat /music/songs_wc/part-r-00000
```
(You cannot run this repeatedly because hadoop will not overwrite the output,
so to rerun first execute `hadoop fs -rm -r -f /mussc/songs_wc`).

## Spark interactive

### standalone shell

You can start up a standalone spark shell - which can interact with hdfs
independently of being standalone, but will be single-threaded on one
machine - by just running `/opt/spark/bin/spark-shell`. You can start a
similar pyspark shell with `/opt/spark/bin/pyspark`.

If you want to run bigger tasks on a single large node with spark, you can
add the `--master local[<n_threads>]` flag.

To access regular text files inside the spark shell, use `sc.testFile(<path>)`,
which returns an RDD of the file contents. To access hdfs files, hand the
same function a hadoop url. For example, to load up the songs example you
can run
```
songsRDD = sc.textFile("hdfs://localhost:9000/music/songs.txt")
songsRdd.collect()
```

### shell attached to yarn

Spark picks up its config from hadoop config files, so just use the
`--master yarn-client` with `spark-shell`.

There are actually two modes for using yarn, `yarn-client` and `yarn-cluster`.
They are similar but affect placement, and `yarn-client` is more suitable
for interactive work (although it may not matter much when running against
a local one-node cluster).

## Spark batch jobs

To run batch spark jobs, use `spark-submit` rather than `spark-shell`.

### Standalone mode

To run the spark version of word count in spark standalone mode, you can
run
```
/opt/spark/bin/spark-submit --class wcex.WC \
  --master local \
  ex/spark_wc/target/wcspex-0.0.1-jar-with-dependencies.jar \
  ./test_data/songs.txt /tmp/songs_wc
```

Unlike hadoop mapreduce, you can run spark standalone pointed at hdfs also;
just run the same command as above - assuming your hdfs daemon is running -
and replace the input and output paths (the last two arguments) with
`hdfs://localhost:9000/music/songs.txt` and
`hdfs://localhost:9000/music/songs_wc`.

### Cluster mode

If you run the same command against yarn, you can omit the hdfs urls from
the filenames. For example:
```
export HADOOP_CONF_DIR=$HADOOP_PREFIX/etc/hadoop
/opt/spark/bin/spark-submit --class wcex.WC \
  --master yarn-cluster \
  ex/spark_wc/target/wcspex-0.0.1-jar-with-dependencies.jar \
  /music/songs.txt /music/spwc_out
```

Note that although hadoop works with just `$HADOOP_PREFIX` set in your
env, spark requires `$HADOOP_CONF_DIR` to be set. For my toy installation,
`$HADOOP_CONF_DIR` is just `$HADOOP_PREFIX/etc/hadoop`, although in a
production deploy you would generally keep these separate so that upgrading
hadoop does not require conf changes.
