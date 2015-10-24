set -e
set -x
mkdir _data
cd _data
curl -L -o donation.zip http://bit.ly/1Aoywaq
unzip donation.zip
rm donation.zip
for f in block_*.zip; do
  unzip $f
  rm $f
done
