(require 'cljs.build.api)

(cljs.build.api/watch
  "src"
  { :main 'ch8-dom-descr.core
    :output-to "out/main.js"})
