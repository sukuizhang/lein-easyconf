(ns easyconf.test.gen
  (:use [midje.sweet]
        [easyconf.gen]))

(fact
  (config-var-script (load-string "(def var1 23)")) => "(config-once :var1 23)\n\n"
  (config-var-script (load-string "(def ^{:comment \"this is var1\"} var1 \"23\")")) =>
  ";;this is var1\n(config-once :var1 \"23\")\n\n")
