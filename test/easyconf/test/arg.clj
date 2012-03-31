(ns easyconf.test.arg
  (:use [easyconf.arg]
        [midje.sweet]))

(fact
  (build-args-text {:name "xyz"} nil) => "\"etc\" \"xyz\""
  (build-args-text {:name "xyz"} "xetc") => "\"xetc\" \"xyz\""
  (build-args-text {:name "xyz" :defconf-ns-prefix "udd"} "xetc") => "\"xetc\" \"udd\""
  (build-args-text {:name "xyz" :defconf-ns-prefix "udd" :defconf-ns-prefixs ["udd1" "udd2" "udd3"]} "etc") => "\"etc\" \"udd1\" \"udd2\" \"udd3\"")
