(ns easyconf.gen
  (:require [easyconf.confs :as confs]
            [clojure.tools.namespace :as namespace])
  (:use [midje.sweet]))

(defn load-ns-in-project
  []
    (doseq [n (namespace/find-namespaces-on-classpath)]
       (try
         (require n)
         (catch Exception e (println "error load " n)))))

(defn config-var-script
  [var]
  (-> (:comment (meta var))
      (#(if % (str ";;" % "\n")))
      (str "(config-once " (confs/get-conf-name var) " " (pr-str (var-get var)) ")\n\n")))

(fact
  (config-var-script (load-string "(def var1 23)")) => "(config-once :var1 23)\n\n"
  (config-var-script (load-string "(def ^{:comment \"this is var1\"} var1 \"23\")")) =>
  ";;this is var1\n(config-once :var1 \"23\")\n\n")

(defn ensure-path-exists
  "make directory if does not existes"
  [path & [isdir?]]
  (let [f (java.io.File. path)]
    (if (not (.exists f))
      (do
        (ensure-path-exists (.getParent f) true)
        (if isdir?
          (.mkdir f)
          (.createNewFile f))))
    f))

(defn create-config-file
  "create a config template file."
  [path]
  (let [ns-file (.getAbsolutePath (java.io.File. (str path "/config/autocreate.clj")))
        head "(ns config.autocreate\n  (:use    [easyconf.core]))\n\n"
        script (->> @@#'confs/conf-vars
                    vals
                    (map config-var-script)
                    (cons head)
                    (apply str))]
    (ensure-path-exists ns-file)
    (spit ns-file script)))

(defn gen-conf [path]
  (println "loading config vars in project ...")
  (load-ns-in-project)
  (println "creating config file ...")
  (create-config-file path)
  (println "successful created config file ..."))
