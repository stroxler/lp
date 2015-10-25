set -e
set -x

mkdir _data
cd _data
wget http://www.iro.umontreal.ca/~lisa/datasets/profiledata_06-May-2005.tar.gz
tar xzf profiledata_06-May-2005.tar.gz
rm profiledata_06-May-2005.tar.gz
mv profiledata_06-May-2005/* .
rmdir profiledata_06-May-2005
