(ns easyconf.test.gen
  (:use [midje.sweet]
        [easyconf.gen]))


(def var1 23)
(def ^{:comment "this is var2"} var2 "23")

(fact
  (config-var-script #'var1) => "(config-once :var1 23)\n\n"
  (config-var-script #'var2) => ";;this is var2\n(config-once :var2 \"23\")\n\n"
  (config-var-template #'var1) => "(config-once :var1 #{var1})\n\n"
  (config-var-template #'var2) => ";;this is var2\n(config-once :var2 #{var2})\n\n"
  (config-var-properties #'var1) => "var1=23\n\n"
  (config-var-properties #'var2) => "##this is var2\nvar2=\"23\"\n\n")
