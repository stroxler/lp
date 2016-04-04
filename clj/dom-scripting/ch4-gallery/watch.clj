(require 'cljs.build.api)

(cljs.build.api/watch
  "src"
  { :main 'ch4-gallery.core
    :output-to "out/main.js"})
