# Spark

## install

To install spark in concert with the hadoop 2.7.1 distro whose install script
lives at `../hadoop/setup/install_hadoop.sh`, you can run the install
script here (which installs from a build tarball, not from source).

Unlike hadoop, there is no configuration needed for either stand-alone
or under-singlenode-yarn spark use.

## running

To run spark in standalone mode, just run `/opt/spark/bin/spark-shell`. To
get an equivalent shell in python, you can run `/opt/spark/bin/pyspark`, 
although it would probably be preferable to get spark on your PYTHONPATH
and set up a spark context in ipython.

Once in the spark shell, you can open up and work with text data as an RDD
by running `val rdd = sc.textFile("/path/to/file")`, or if you have your
hdfs daemons running you can load hdfs text data by running
`val rdd = sc.textFile("hdfs://localhost:9000/path/to/file")`
