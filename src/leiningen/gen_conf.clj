(ns leiningen.gen-conf
  (:require [leiningen.compile :as eval]
            [leiningen.classpath :as classpath]))

(defn gen-conf
  [project & args]
  (let [path (or (:test-path project)
                 "etc")
        ns-syms (cond (empty? args) [(:name project)]
                      :else args)
        args-text (->> (cons path ns-syms)
                       (map pr-str)
                       (interpose " ")
                       (apply str))]
    (eval/eval-in-subprocess
     project
     (str "(do (require 'easyconf.gen)
             (easyconf.gen/gen-conf " args-text "))"))))
