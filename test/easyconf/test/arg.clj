(ns easyconf.test.arg
  (:use [easyconf.arg]
        [midje.sweet]))

(fact
  (build-args-text {:name "xyz" :test-path "test"} nil) => "\"test\" \"xyz\""
  (build-args-text {:name "xyz" :test-path "test"} "etc") => "\"etc\" \"xyz\"")
