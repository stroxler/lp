# this handy script - which I'm saving because it might help
# me memorize the command (which I should maybe port to a python
# script) does a replace on all my build clojure scripts
for f in *.clj; do
  sed -i.bak -e 's/ch2-status-board/ch2-bar-chart/g' $f
done

sed -i.bak -e 's/ch2-status-board/ch2-bar-chart/g' src/ch2-status-board/core.cljs
mv src/ch2-status-board src/ch2-bar-chart

find . -name '*.bak' | xargs rm
