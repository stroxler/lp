set -e
set -x

version=1.5.1
hadoop=hadoop2.6

cd /opt
wget http://apache.arvixe.com/spark/spark-$version/spark-$version-bin-$hadoop.tgz
tar xzf spark-$version-bin-$hadoop.tgz
rm spark-$version-bin-$hadoop.tgz

sn -s /opt/spark-$version-bin-$hadoop.tgz spark
