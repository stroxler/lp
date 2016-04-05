(require 'cljs.build.api)

(cljs.build.api/watch
  "src"
  { :main 'ch7-gallery.core
    :output-to "out/main.js"})
