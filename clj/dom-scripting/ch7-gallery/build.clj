(require 'cljs.build.api)

(cljs.build.api/build
  "src"
  { :main 'ch7-gallery.core
    :output-to "out/main.js"})
