(ns easyconf.gen
  (:require [easyconf.confs :as confs]
            [clojure.tools.namespace :as namespace])
  (:use [midje.sweet]))

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
  "ensure directory existes"
  [path]
  (let [f (java.io.File. path)]
    (if (not (.exists f))
      (do
        (ensure-path-exists (.getParent f))
        (.mkdir f)))))

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
  (println "creating config file ...")
  (create-config-file path)
  (println "successful created config file ..."))

(defn build-args-text
  [project args]
  (let [path (or (:test-path project)
              "etc")
        ns-syms (cond (empty? args) [(:name project)]
                      :else args)]
    (->> (cons path ns-syms)
         (map pr-str)
         (interpose " ")
         (apply str))))

(fact
  (build-args-text {:name "xyz" :test-path "test"} []) => "\"test\" \"xyz\""
  (build-args-text {:name "xyz"} []) => "\"etc\" \"xyz\""
  (build-args-text {:name "xyz" :test-path "test"} ["abc"]) => "\"test\" \"abc\""
  (build-args-text {:name "xyz" :test-path "test"} ["abc" "def" "ghi"]) => "\"test\" \"abc\" \"def\" \"ghi\"")
