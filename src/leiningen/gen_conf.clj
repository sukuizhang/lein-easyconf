(ns leiningen.gen-conf
  (:require [leiningen.compile :as eval]))

(defn gen-conf
  [project & [path]]
  (let [path (or path
                 (:test-path project)
                 "etc")])
  (eval/eval-in-subprocess
   project
   (str "(do (require 'easyconf.gen)
             (easyconf.gen/gen-conf \"" path "\"))")))
