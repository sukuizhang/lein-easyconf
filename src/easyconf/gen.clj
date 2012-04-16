(ns easyconf.gen
  (:require [easyconf.confs :as confs]
            [clojure.tools.namespace :as namespace]))

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

(defn write-config-file
  "write easyconf config items in special format into special path
   head: content above all config items
   format-fn: a function to make a config item to string."
  [path head format-fn]
  (let [script (->> @@#'confs/conf-vars
                    vals
                    (map format-fn)
                    (cons head)
                    (apply str))]
    (ensure-path-exists (-> (java.io.File. path)
                             .getAbsoluteFile
                             .getParent))
    (spit path script)))

(defn config-var-script
  [var]
  (-> (:comment (meta var))
      (#(if % (str ";;" % "\n")))
      (str "(config-once " (confs/get-conf-name var) " " (pr-str (var-get var)) ")\n\n")))

(defn config-var-template
  [var]
  (-> (:comment (meta var))
      (#(if % (str ";;" % "\n")))
      (str "(config-once " (confs/get-conf-name var) " #{" (name (confs/get-conf-name var)) "})\n\n")))

(defn create-config-clj
  "create a config template file."
  [path]
  (let [ns-file (str path "/config/autocreate.clj")
        head "(ns config.autocreate\n  (:use    [easyconf.core]))\n\n"]
    (write-config-file ns-file head config-var-script)))

(defn create-config-template
  "create a config template file."
  [path]
  (let [ns-file (str path "/autocreate.clj")
        head "(ns config.autocreate\n  (:use    [easyconf.core]))\n\n"]
    (write-config-file ns-file head config-var-template)))

(defn config-var-properties
  [var]
  (-> (:comment (meta var))
      (#(if % (str "##" % "\n")))
      (str (name (confs/get-conf-name var)) "=" (pr-str (var-get var)) "\n\n")))

(defn create-config-properties
  "create a config template file."
  [path]
  (let [properties-file (str path "/autocreate.properties")]
    (write-config-file properties-file nil config-var-properties)))

(defn gen-conf [path & ns-syms]
  (println "loading config vars in project ...")
  (apply confs/load-ns (map symbol ns-syms))
  (merge-exist-config path)
  (println "creating config file ...")
  (create-config-clj path)
  (println "successful created config file ...")
  (println "creating template file ...")
  (create-config-template path)
  (println "successful created template file ...")
  (println "creating properties file ...")
  (create-config-properties path)
  (println "successful created properties file ..."))

