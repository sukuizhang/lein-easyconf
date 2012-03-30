(ns easyconf.test.arg
  (:use [easyconf.arg]
        [midje.sweet]))

(fact
  (build-args-text {:name "xyz" :test-path "test"} nil) => "\"test\" \"xyz\""
  (build-args-text {:name "xyz" :test-path "test"} "etc") => "\"etc\" \"xyz\""
  (build-args-text {:name "xyz" :test-path "test" :defconf-ns-prefix "udd"} "etc") => "\"etc\" \"udd\""
  (build-args-text {:name "xyz" :test-path "test" :defconf-ns-prefix "udd" :defconf-ns-prefixs ["udd1" "udd2" "udd3"]} "etc") => "\"etc\" \"udd1\" \"udd2\" \"udd3\"")
