(ns ch6-gallery.core
  (:require [clojure.browser.repl :as cbrepl]))

(enable-console-print!)

; What does this demo show?
;
; Mostly, using the window.onload function to set everything up.
; Don't use document.onload... the property seems to exist, at least in
; Chrome, but it never appears to run.
;
; Chapter 6 does a lot more stuff: he adds a bunch of checks to the code
; (make sure the image element exists, make sure we have access to the DOM
; functions before calling them) but most of it isn't interesting to me.
;
; So this code is mostly very similar to Chapter 4's code. Aside from using
; document.onload, the other main change I did was make show-pic return a
; function which is a closure over the link.
;
; What else did I learn?
;   document .onload is a mistake, use window.onload
;
;   the connection to the repl can't happen before the body loads.
;     - If you put the <script> tag for your main.js file, in the
;       <body> of your doc - even at the top of it - then this
;       isn't an issue
;     - but if you put it in the <head>, then you need to defer the
;       defonce connection until window.onload or some such thing.
;
;   if your .onclick handler isn't working, check for typos. I had a nasty
;     snafu of many typos (including that I was setting onload, not onclick!)
;     that took a while to debug
;
;   To properly undo the effect of a float: left list in css - which causes
;   the image to also float and appear to the right of the list - you need
;   to give the image *both* the clear: both and display: block styles. Either
;   one of them alone isn't enough.
;   ... it basically appears that the float property kind of propagates down
;   the html tree to sibling nodes, which means you have to explicitly clear
;   it. That's what the clear:both does. The display: block I think is
;   needed even after clear:both because, by default, images aren't displayed
;   in block mode, they are displayed inline. This doesn't matter before we
;   make the li's float:left because they end their line anyway, but as soon
;   as we make them float, the image wants to go to their right *both* becuase
;   it likes to be displayed inline *and* because the float is propagating,
;   which is why we have to deal with both issues.
;   (I find css confusing grr)


(defn show-pic [which-pic]
  (let [image       (.getElementById js/document "image")
        caption     (.getElementById js/document "caption")
        source      (.getAttribute which-pic "href")
        title       (.getAttribute which-pic "title")]
    (.setAttribute image "src" source)
    (set! (.-innerText caption) title)
  ))


(defn prepare-gallery [_]
  ; I'm skipping the checks for DOM functions.
  (let [gallery (.getElementById js/document "imagegallery")
        links   (.getElementsByTagName gallery "a")]
  (doseq [link (array-seq links)]
    (letfn [(handle-click [_]
              (show-pic link)
              false)]
    (set! (.-onclick link) handle-click)))))

(set! (.-onload js/window) (fn [_]
  (defonce conn
    (cbrepl/connect "http://localhost:9000/repl"))
  (prepare-gallery)))
