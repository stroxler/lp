(ns ch8-dom-descr.core
  (:require [clojure.browser.repl :as cbrepl]))

; What new stuff did I learn here?
;
; I learned some new html: dl = definition list, dt=definition term,
; dd = definition definition
;
; I think I used the map function for the first time. Note that the order
; of args is the same as for python
;
; I learned that clojure doesn't need a zip function, you can just use
; (map vector seq0 seq1 ...)
;
; It became clear writing add-abbr-table that putting a big chunk of your
; code in a let block can make things very readable in some cases.
;
; I used when for the first time, which is like an if plus a do.
;
; I learned a hack to get the last child element from a dom element (you
; can't use lastChild, because that counts non-element nodes, so you instead
; search for all child elements and find the last one).

(defn add-load-func [f]
  (let [old-onload (.-onload js/window)]
    (set! (.-onload js/window)
          (if (nil? old-onload)
            f
            (fn [_] (old-onload) (f))))))

; create a textNode with content `string`
(defn create-text [string]
  (.createTextNode js/document string))

; insert a new child at the bottom of a parent element
(defn insert-at-bottom [new-elt parent-elt]
  (.appendChild parent-elt new-elt))

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

(defn set-up-dev []
  (enable-console-print!)
  (defonce conn (cbrepl/connect "http://localhost:9000/repl")))

(defn get-text [elt]
  (-> elt (.-firstChild) (.-nodeValue)))


(defn add-abbr-table []
  (let [body         (.-body js/document)
        abbr-elts    (array-seq (.getElementsByTagName js/document "abbr"))
        abbrs        (map #(-> (get-text %)) abbr-elts)
        meanings     (map #(-> (.getAttribute % "title"))     abbr-elts)
        abbr-header  (create-elt "h2" {} "Abbreviations")
        defntn-list  (create-elt "dl")
        defntn-terms (map #(create-elt "dt" {} %) abbrs)
        defntn-defs  (map #(create-elt "dd" {} %) meanings)]
    (when (> (count abbr-elts) 0) ; don't do anything if in IE
      (insert-at-bottom abbr-header body)
      (doseq [[dt dd] (map vector defntn-terms defntn-defs)]
        (insert-at-bottom dt defntn-list)
        (insert-at-bottom dd defntn-list))
      (insert-at-bottom defntn-list body))))

(defn add-citations []
  (let [body       (.-body js/document)
        quotes     (array-seq (.getElementsByTagName js/document "blockquote"))
        citations  (map #(.getAttribute % "cite") quotes)]
    (doseq [[q c] (map vector quotes citations)]
      (println q)
      (println c)
      (let [q-children (-> q (.getElementsByTagName "*") (array-seq))]
        (when (> (count q-children) 0)
          (let [a            (create-elt "a" {:href c} "source")
                sup          (create-elt "sup")
                last-q-child (last q-children)]
            (insert-at-bottom a sup)
            (insert-at-bottom sup last-q-child)))))))


(add-load-func set-up-dev)
(add-load-func add-abbr-table)
(add-load-func add-citations)
