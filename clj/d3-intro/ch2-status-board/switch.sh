# this handy script - which I'm saving because it might help
# me memorize the command (which I should maybe port to a python
# script) does a replace on all my build clojure scripts
for f in *.clj; do
  sed -i.bak -e 's/ch1-bar-plots/ch2-status-board/g' $f
done

sed -i.bak -e 's/ch1-bar-plots/ch2-status-board/g' src/ch2-status-board/core.cljs
mv src/ch1-bar-plots src/ch2-status-board

find . -name '*.bak' | xargs rm
