(ns easyconf.gen
  (:require [easyconf.confs :as confs]
            [clojure.tools.namespace :as namespace]))

(defn config-var-script
  [var]
  (-> (:comment (meta var))
      (#(if % (str ";;" % "\n")))
      (str "(config-once " (confs/get-conf-name var) " " (pr-str (var-get var)) ")\n\n")))

(defn ensure-path-exists
  "ensure directory existes"
  [path]
  (let [f (java.io.File. path)]
    (if (not (.exists f))
      (do
        (ensure-path-exists (.getParent f))
        (.mkdir f)))))

(defn merge-exist-config
  [path]
  (let [ns-file (str path "/config/autocreate.clj")]
    (when (.exists (java.io.File. ns-file))
      (println "find existd old config file, load it to merge exist config ...")
      (load-file ns-file))))

(defn create-config-file
  "create a config template file."
  [path]
  (let [ns-file (str path "/config/autocreate.clj")
        head "(ns config.autocreate\n  (:use    [easyconf.core]))\n\n"
        script (->> @@#'confs/conf-vars
                    vals
                    (map config-var-script)
                    (cons head)
                    (apply str))]
    (ensure-path-exists (-> (java.io.File. ns-file)
                             .getAbsoluteFile
                             .getParent))
    (spit ns-file script)))

(defn gen-conf [path & ns-syms]
  (println "loading config vars in project ...")
  (apply confs/load-ns (map symbol ns-syms))
  (merge-exist-config path)
  (println "creating config file ...")
  (create-config-file path)
  (println "successful created config file ..."))
