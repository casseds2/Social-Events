(ns social-events.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [social-events.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 1))))
