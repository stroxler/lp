(ns ch3.core
  (:require [clojure.browser.repl :as cbrepl]))

(enable-console-print!)

(defonce conn
  (cbrepl/connect "http://localhost:9000/repl"))


; What does this demo show?
;
; These DOM methods:
;   .getElementsByTagNAme
;   .getElementById
;   (the other common one is .getElementsByClassName)
;
; These fields / methods on dom elements
;   .-innerText  (there's also .-innerHtml... if they agree then
;                 it basically means you are at a node of the tree)
;   .-children   (these are dom element children. There's also .-childNodes,
;                 but that introduces extra stuff so it isn't usually what you
;                 want)
;   .getAttribute
;   .setAttribute
;
; These clojurescript features
;   doseq blocks
;   array-seq to make java script arrays into seqables

(println "Hello World! Yeah, that's right")

(let [ps (.getElementsByTagName js/document "p")]
  (doseq [p (array-seq ps)]
   (println (str "p with content \"" (.-innerText p)
                 "\" has title \"" (.getAttribute p "title") "\"")))) 

; Note that this causes us to lose the red on the first item, since
; we are changing the class. The delay in loading clojurescript is enough that
; you can see the color change.
(let [lis (-> js/document
          (.getElementById "purchases")
          (.-children))]
  (doseq [li (array-seq lis)]
    (.setAttribute li "class" "italicize")))
