(ns ch4-gallery.core
  (:require [clojure.browser.repl :as cbrepl]))

(enable-console-print!)

(defonce conn
  (cbrepl/connect "http://localhost:9000/repl"))

; What does this demo show?
;
; - defining functions for event handlers
; - attaching onclick handlers using the vanilla dom
;    - note that the way I did it avoided using the `this` object
;    - if you *do* need `this`, you would use a built-in macro this-as, e.g.
;      (this-as my-var-for-this (some-function my-var-for-this))
; - preventing links from being followed (return false from the event handler)
; - using element.firstChile.nodeValue as a synonym, in certain contexts,
;   for .innerText
;
; What else have I learned?
; A if something unexpected happens, check for errors. For example, after a
;   slight modification that didn't change the fact that handle-click returns
;   false, it started following the links. But the reason was because a line
;   in show-pic was throwing an error, which caused the handler to not return.
; B there are three kinds of nodes in the DOM specification.
;   Element nodes have a nodeType value of 1
;   Attribute nodes have a nodeType value of 2
;   Test nodes have a nodeType value of 3.
;   A lot of the DOM functions that return more items than you expect do so
;   because they are returning non-Element nodes.


; when which-pic is one of the <a> tags with an image url, calling this
; function will set the image placeholder to the image specified in that url.
; by returning false, we prevent the browser from following the link.
(defn show-pic [which-pic]
  (let [image       (.getElementById js/document "image")
        caption     (.getElementById js/document "caption")
        source      (.getAttribute which-pic "href")
        title       (.getAttribute which-pic "title")]
    (.setAttribute image "src" source)
    ; these two lines are equivalent... innerText is a shortcut for getting
    ; the first child - which is a text node, and then its value, which is the
    ; actual text
    ;(set! (.-innerText caption) title)
    (set! (-> caption (.-firstChild) (.-nodeValue)) title)
  ))

(def as (.getElementsByTagName js/document "a"))

(doseq [a (array-seq as)]
  (letfn [(handle-click [_]
            (show-pic a)
            false)]
    (set! (.-onclick a) handle-click)))
