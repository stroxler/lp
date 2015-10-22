# Code for learning to use hadoop, and for handling simple installs

## Install

On an ubuntu machine which already has java defined, you can
 - run `setup/install_hadoop.sh` to install hadoop and create an alias
   for the hadoop command (which for reasons I will not explain here, is
   easier to have as an alias than on the path)
 - run `setup/setup_ssh.sh` if you need to add `localhost` self-access
 - run `setup/java_home_setup.sh` assuming you installed java using `ape2`...
   for some reason hadoop fails to pick up `JAVA_HOME` defined in
   `/etc/profile.d`, so I add it explicitly to `.bashrc`.

At this point your system shoudl be essentially ready. Also, since running
hadoop requires modifying some of the config files, all of the files in
`hadoop/etc/hadoop` have had `.orig` backups made of them.

## running

By default hadoop is configured for standalone mode, which allows you to run
mapreduce tasks on local text files as if they were `hdfs` files. You do
not need to do anything to the original configuration to make this work,
but if you have been running in single-node mode (see below) you may need
to run `setup/standalone.sh` to reset your configuration and kill the `hdfs`
and `yarn` daemons.

To start up `hdfs` and `yarn` in pseudo-distributed mode, you first need
to format the dfs. To do so, run `setup/singlenode_format.sh`. You should only
need to do this once per machine per power cycle or something like that.
This script, by the way, modifies a bunch of files in `hadoop/etc/hadoop`. It
creates backups first, but the backups eventually overwrite one another so
be careful.

To actually boot the `hdfs` and `yarn` daemons, run `setup/singlenode.sh`.

## using the hdfs

This is right where I am leaving off for the day so I do not have much
advice, but to interface with the hdfs the easiest thing is to use
```
hadoop fs <cmd>
```
where command has a leading dash but otherwise comes from a subset of the
ordinary linux filesystem commands, e.g. `hadoop fs -mkdir /somedir`.

## Running mapreduce tasks in standalone mode

TODO

## Running mapreduce tasks in single-node mode

TODO
