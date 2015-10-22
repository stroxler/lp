set -e
set -x
cd /opt
version=2.7.1
wget http://apache.arvixe.com/hadoop/common/hadoop-$version/hadoop-$version.tar.gz
tar xzf hadoop-$version.tar.gz
rm hadoop-$version.tar.gz
cd hadoop-$version

ln -s /opt/hadoop-$version /opt/hadoop
echo 'export HADOOP_PREFIX=/opt/hadoop' > /etc/profile.d/hadoop_prefix.sh
source /etc/profile.d/hadoop_prefix.sh

hetc=$HADOOP_PREFIX/etc/hadoop

for file in $(ls $hetc); do
  cp $hetc/$file $hetc/$file.orig
done

# make a shortcut for the hadoop command
echo 'alias hadoop=/opt/hadoop/bin/hadoop' >> /root/.bashrc
