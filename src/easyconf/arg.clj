(ns easyconf.arg)

(defn build-args-text
  [project path]
  (let [path (or path (:test-path project))]
    (->> [path (:name project)]
         (map pr-str)
         (interpose " ")
         (apply str))))
