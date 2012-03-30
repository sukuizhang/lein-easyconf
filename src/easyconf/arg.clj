(ns easyconf.arg)

(defn build-args-text
  [project path]
  (let [path (or path (:test-path project))
        defconf-ns-prefixs (or (:defconf-ns-prefixs project)
                               [(or (:defconf-ns-prefix project)
                                    (:name project))])]
    (->> (cons path defconf-ns-prefixs)
         (map pr-str)
         (interpose " ")
         (apply str))))
