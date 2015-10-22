# Start up hdfs and yarn in single-node mode, assuming the hdfs has
# already been formatted.

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

cat > $hetc/core-site.xml << \EOF
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
EOF

cat > $hetc/hdfs-site.xml << \EOF
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>
EOF

cat > $hetc/hdfs-site.xml << \EOF
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
EOF

cat > $hetc/hdfs-site.xml << \EOF
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
</configuration>
EOF

hsbin=$HADOOP_PREFIX/sbin
$hsbin/start-dfs.sh
$hsbin/start-yarn.sh
