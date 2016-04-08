(ns ntrw.core
  (:require [cljs.nodejs :as node]))

(node/enable-util-print!)

(def child_process (node/require "child_process"))
(def fs (node/require "fs"))



(defn local [init-value]
  "Create a mutable local variable"
  #js [init-value])

(defn slocal [variable value]
  "Set the value of a mutalbe local"
  (aset variable 0 value))

(defn glocal [variable value]
  "Get the value of a mutable local"
  (aget variable 0))

(defn ulocal [variable func]
  "Update a local using `func`"
  (let [newvalue (-> variable (glocal) (func))]
    (slocal variable newvalue)))


(defn js-arr [x]
  "Convert a seq to a native array. In settings where you have a literal
  vector, you can use the #js macro. But otherwiwse you need a function."
  (apply array x))


(defn system-call
  "Make a system call, return the child process object.
  `call` should be a string, and `args` a seq of strings.

  If there is a callback function, it will be called with, as input, a
  string containing the stdout of the system call.

  There's no real error handling here, nore a way to get stderr.

  Dev note:
  As with many JS functions, if you forget to convert data it fails but
  does so slilently :("
  ([call args] (.spawn child_process call (js-arr args)))
  ([call args cb]
   (let [proc        (system-call call args)
         proc-stdout (.-stdout proc)
         output      (local "")]
     (.on proc-stdout "data"
          (fn [data] (ulocal output #(+ % data))))
     (.on proc-stdout "close"
          #(cb (glocal output))))))


(defn forward-stdout [proc]
  "forward stdout of a child process othe this program's stdout"
  (let [stdout      (.-stdout node/process)
        proc-stdout (.-stdout proc)]
    (.pipe proc-stdout stdout)))


(defn fs-watch [filename func]
  "Whatch the file at path `filename`, calling `func` on change"
  (.watch fs filename func))


(defn ls
  "ls -lh $filename as a non-blocking child process"
  ([filename] (system-call "ls" ["-lh" filename]))
  ([filename cb] (system-call "ls" ["-lh" filename] cb)))


(defn print-file-at-once [filename]
  (.readFile fs filename
             (fn [e d] (if e (println e) (println (.toString d))))))


(defn print-file-in-chunks [filename]
  (let [rs (.createReadStream fs filename)]
    (.on rs "data"  #(-> node/process (.-stdout) (.write %)))
    (.on rs "error" #(println %))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn make-changed-alert [filename]
  (let [print-stdout #(println %)]
    (ls filename print-stdout)))


(defn setup-fs-watch [filename]
  (let [alert-print (fn [] (make-changed-alert filename))]
    (fs-watch filename alert-print)
    (println (str "Now watching " filename " for changes"))))

; this main syntax is what clojure uses
;   clojurescript actually doesn't care, you have to use the
;   set! *main-cli-fn* regardless. But for consistency with
;   jvm clojure, it makes sense to use -main.
(defn -main [& args]
  (let [filename (first args)]
    (setup-fs-watch filename)
    (print-file-at-once   filename)
    (print-file-in-chunks filename)))


(set! *main-cli-fn* -main)
