hsbin=$HADOOP_PREFIX/sbin
$hsbin/stop-yarn.sh
$hsbin/stop-dfs.sh

hetc=$HADOOP_PREFIX/etc/hadoop

make_backup() {
  file=$1
  if [ -e $file ]; then
    cp $file $file.bak
  fi
}

make_backup $hetc/core-site.xml
make_backup $hetc/hdfs-site.xml
make_backup $hetc/yarn-site.xml
make_backup $hetc/mapred-site.xml

cp $hetc/core-site.xml.orig $hetc/core-site.xml
cp $hetc/hdfs-site.xml.orig $hetc/hdfs-site.xml
cp $hetc/yarn-site.xml.orig $hetc/yarn-site.xml
rm -f $hetc/mapred-site.xml
