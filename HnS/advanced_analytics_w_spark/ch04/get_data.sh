set -e
set -x

mkdir _data
cd _data
wget https://archive.ics.uci.edu/ml/machine-learning-databases/covtype/covtype.data.gz
gunzip covtype.data.gz
rm covtype.data.gz
