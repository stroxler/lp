# this handy script - which I'm saving because it might help
# me memorize the command (which I should maybe port to a python
# script) does a replace on all my build clojure scripts
for f in *.clj; do
  sed -i.bak -e 's/ch3-lines-plot/ch3-scatter-plot/g' $f
done

sed -i.bak -e 's/ch3-lines-plot/ch3-scatter-plot/g' src/ch3-lines-plot/core.cljs
mv src/ch3-lines-plot src/ch3-scatter-plot

find . -name '*.bak' | xargs rm
