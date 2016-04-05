(ns ch7-gallery.core
  (:require [clojure.browser.repl :as cbrepl]))

; What does this demo show?
;
; I port the add-to-window.onload function that the author gives in Chapter 6
; to clojurescript (I didn't d it in Chapter 6). I also factored out the dev
; setup (enabling console printing and connecting the repl) into a function
; which can be added.
;
; I stopped returning false from the click handler. That's a bad way of
; preventing link following for a few reasons:
;   - first, it's possibly not part of the W3 standard (although I've never
;     actually seen a browser fail to support it
;   - second, it's confusing for non-front-end developers, whereas preventing
;     the default response to clicking a link is self-explanatory
;   - third - and maybe most important - you can do it up front, which prevents
;     the situation I keep encountering where there's a bug that throws an
;     error in the handler... and then the link is followed so you can't see
;     the error!
; Using preventDefault is way way better :)
;
; The html no longer has the #image and #caption elements, so we use adding
; them as a chance to show off the DOM api for adding elements.
;
; In the process, we define clojurescript-idiomatic wrappers around the DOM
; api functions
;   createElement, createTextNode, appendChild, and insertBefore
; and we also define our own
;   insert-after
; function, which the DOM api doesn't provide.
;
; The create-elt function also has some new clojure stuff (for me) in it:
;   how to make variadic functions
;   how to use a doseq over a map
;     answer: use the usual syntax plus vector destructuring
;   how to convert a string or keyword into a string (useful for host APIs!)
;     answer: use the name function

; set up dev browser
(defn set-up-dev []
  (enable-console-print!)
  (defonce conn (cbrepl/connect "http://localhost:9000/repl")))


; append a function to a chain of functions to run when the window loads.
; assume all of the functions have no args.
(defn add-load-func [f]
  (let [old-onload (.-onload js/window)]
    (set! (.-onload js/window)
          (if (nil? old-onload)
            f
            (fn [_] (old-onload) (f))))))



; add an element at the bottom of a parent element
(defn insert-at-bottom [new-elt, parent-elt]
  (.appendChild parent-elt new-elt))

; add an element in front of another (as a preceding sibling)
(defn insert-before [new-elt, target-elt]
  (let [parent (.-parentNode target-elt)]
    (.insertBefore parent new-elt target-elt)))

; add an element in behind another (as the next sibling)
(defn insert-after [new-elt, target-elt]
  (let [parent (.-parentNode target-elt)]
    (if (= (.-lastChild parent) target-elt)
      (insert-at-bottom new-elt parent)
      (.insertBefore parent new-elt (.-nextSibling target-elt)))))

; create a textNode with content `string`
(defn create-text [string]
  (.createTextNode js/document string))

(defn set-text [elt string]
  (set! (-> elt (.-firstChild) (.-nodeValue)) string))


; create an Element with tag `tag` attributes according to `attr-map`,
; and text content `text`.
(defn create-elt
  ([tag] (.createElement js/document tag))
  ([tag attr-map]
   (let [elt (create-elt tag)]
     (doseq [[k v] attr-map]
       (.setAttribute elt (name k) v))
     elt))
  ([tag attr-map text]
   (let [elt (create-elt tag attr-map)
         textNode (create-text text)]
     (insert-at-bottom textNode elt)
     elt)))
  

(defn show-pic [which-pic]
  (let [image       (.getElementById js/document "image")
        caption     (.getElementById js/document "caption")
        source      (.getAttribute which-pic "href")
        title       (.getAttribute which-pic "title")]
    (.setAttribute image "src" source)
    (set-text caption title)
  ))


(defn prepare-image-and-caption []
  (let [image (create-elt "img"
                          {:id "image" :src "public/images/placeholder.gif"
                           :alt "my image gallery"})
        caption (create-elt "p" {:id "caption"} "Choose an image")
        gallery (.getElementById js/document "imagegallery")]
    (insert-after image gallery)
    (insert-after caption image)))


(defn prepare-gallery [_]
  ; I'm skipping the checks for DOM functions.
  (let [gallery (.getElementById js/document "imagegallery")
        links   (.getElementsByTagName gallery "a")]
  (doseq [link (array-seq links)]
    (letfn [(handle-click [e]
              (.preventDefault e)
              (show-pic link))]
    (set! (.-onclick link) handle-click)))))


(add-load-func set-up-dev)
(add-load-func prepare-image-and-caption)
(add-load-func prepare-gallery)
