(require 'cljs.build.api)

(cljs.build.api/build
  "src"
  { :main 'ch2-status-board.core
    :output-to "out/main.js"})
