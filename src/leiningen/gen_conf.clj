(ns leiningen.gen-conf
  (:require [leiningen.compile :as eval]
            [leiningen.classpath :as classpath]
            [easyconf.arg :as arg]))

(defn gen-conf
  [project & [path]]
  (let [args-text (arg/build-args-text project path)]
    (eval/eval-in-subprocess
     project
     (str "(do (require 'easyconf.gen)
             (easyconf.gen/gen-conf " args-text "))"))))
