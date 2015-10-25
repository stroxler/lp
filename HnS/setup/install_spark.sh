set -e
set -x

version=1.5.1
hadoop=hadoop2.6

cd /opt
wget http://apache.arvixe.com/spark/spark-$version/spark-$version-bin-$hadoop.tgz
tar xzf spark-$version-bin-$hadoop.tgz
rm spark-$version-bin-$hadoop.tgz

ln -s /opt/spark-$version-bin-$hadoop.tgz /opt/spark
echo 'alias spark-shell=/opt/spark/bin/spark-shell' >> $HOME/.bashrc
echo 'alias spark-submit=/opt/spark/bin/spark-submit' >> $HOME/.bashrc
echo 'alias pyspark=/opt/spark/bin/pyspark' >> $HOME/.bashrc
