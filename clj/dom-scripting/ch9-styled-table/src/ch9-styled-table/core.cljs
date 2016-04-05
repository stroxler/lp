(ns ch9-styled-table.core
  (:require [clojure.browser.repl :as cbrepl]))

; What new stuff did I learn here?
;
; For starters, the XMLHttpRequest syntax basics, and how to get json responses
;   the old-school way
; Oddly, JSON/parse doesn't use the js/ prefix
; I learned the simplest way to convert json data to clojurescript.
;   For a "real" example, it woudl be better to use a library. One
;   option is transit-cljs. The performance of using JSON/parse plus js->clj
;   can apparently be quite significant.
;
; I also learned some more about the table elements in html, as well as some
; css formatting for tables.
;
; I used aget for the first time, which works on both javascript arrays and
; objects (remember that as with lua, they are semantically the same thing
; in javascript). Using that, I was able to fetch computed styles (that is,
; styles potentially from css or browser defaults, which aren't accessible
; using the element .style property) on elements.
;
; Finally, I learned to swap classes of items on mouseover events to get
; dynamic boldfacing.

(defn add-load-func [f]
  (let [old-onload (.-onload js/window)]
    (set! (.-onload js/window)
          (if (nil? old-onload)
            f
            (fn [_] (old-onload) (f))))))

(defn set-up-dev []
  (enable-console-print!)
  (defonce conn (cbrepl/connect "http://localhost:9000/repl")))

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

(defn view-computed-style [elt style-key]
  "If style-key is a string, get the computed style for an element"
  (aget (.getComputedStyle js/window elt) style-key))


(defn add-itinerary-table []
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


(defn make-request [method url async handler]
  (let [req         (js/XMLHttpRequest.)
        req-ok      (fn [] (= (.-status req) 200))
        data-ready  (fn [] (= (.-readyState req) 4))
        req-done    (fn [] (and (req-ok) (data-ready)))
        parse-json  (fn [string] (-> string (JSON/parse) (js->clj)))
        parse-resp  (fn [] (-> req (.-responseText) (parse-json)))
        handle-resp (fn [_]
                      (when (req-done)
                        (let [response (parse-resp)]
                          (handler response))))]
    (set! (.-onreadystatechange req) handle-resp)
    (.open req method url async)
    (.send req)))

(def body (->
            (.getElementsByTagName js/document "body")
            (array-seq)
            (first)))

(defn make-table [itinerary]
  (let [table     (create-elt "table")
        caption   (create-elt "caption" {:id "table-caption"} "Itinerary")
        head      (create-elt "thead")
        headwhen  (create-elt "th" {} "When")
        headwhere (create-elt "th" {} "Where")]
    (insert-at-bottom table body)
    (insert-at-bottom caption table)
    (insert-at-bottom headwhen head)
    (insert-at-bottom headwhere head)
    (insert-at-bottom head table)
    (doseq [{whn "when" whr "where"} itinerary]
      (let [row     (create-elt "tr")
            whentd  (create-elt "td" {} whn)
            wheretd (create-elt "td" {} whr)]
        (insert-at-bottom whentd  row)
        (insert-at-bottom wheretd row)
        (insert-at-bottom row table)))))


(defn sleep [milliseconds]
  "I wound up not using it, but here's a synchronous sleep function"
  (let [until (+ milliseconds (.getTime (js/Date.)))]
    (while (> until (.getTime (js/Date.))))))


(defn print-some-styles-for-caption []
  (let [caption      (.getElementById js/document "table-caption")
        print-style  (fn [style-key]
                       (println
                         (str style-key ": "
                              (view-computed-style caption style-key))))]
    (println caption)
    (print-style "margin")
    (print-style "padding")
    (print-style "font-size")
    (print-style "font-weight")
    (print-style "color")))

(defn set-class [elt class-name]
  (.setAttribute elt "class" class-name))

(defn add-bold-on-mouseover []
  ; Note: it's possible to directly set styles from javascript. They are
  ;       found under the .style.styleKey entry of DOM elements, where
  ;       any style keys with a dash are converted to camelcase. For example:
  ;         (set! (.-fontWeight (.-style row)) "bold")
  ;       would work. But using classes and keeping css in css is generally
  ;       considered better practice.
  (let [rows (array-seq (.getElementsByTagName js/document "tr"))]
    (doseq [row rows]
      (set! (.-onmouseover row) (fn [_] (set-class row "active")))
      (set! (.-onmouseout row) (fn [_] (set-class row nil)))
    )))

(defn handle-request-response [response]
  (make-table response)
  (add-bold-on-mouseover)
  (print-some-styles-for-caption))

(defn fetch-data[]
    (make-request "GET" "data/itinerary.json" true handle-request-response))

(add-load-func set-up-dev)
(add-load-func fetch-data)
