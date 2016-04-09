(require 'cljs.build.api)

(cljs.build.api/watch
  "src"
  { :main 'ch2-status-board.core
    :output-to "out/main.js"})
