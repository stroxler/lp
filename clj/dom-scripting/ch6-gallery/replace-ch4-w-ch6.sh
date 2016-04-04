# this handy script - which I'm saving because it might help
# me memorize the command (which I should maybe port to a python
# script) does a replace on all my build clojure scripts
for f in *.clj; do
  sed -i.bak -e 's/ch4/ch6/g' $f
done

sed -i.bak -e 's/ch4/ch6/g' src/ch4-gallery/core.cljs
mv src/ch4-gallery src/ch6-gallery
