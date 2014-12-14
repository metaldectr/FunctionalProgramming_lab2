(ns lab2.core-test
  (:require [clojure.test :refer :all]
            [lab2.core :refer :all]
            [lab2.core :as lab2]
            [clj-http.client :as client]))

(deftest parse-external-urls-test
  (testing "Parse external urls test."
    (let [externalUrls (getLink (:links (client/get "http://tut.by")))]
      (is (and (not= externalUrls nil)
               (= (count externalUrls) 2)))
      (is (= (nth externalUrls 0) "http://test1.com"))
      (is (= (nth externalUrls 1) "http://test2.com")))))


(deftest parseLink
  (testing "Handle url with 404 response status."
    (is (= (lab2/parseLink "http://404!!!.com" 1) "Bad"))))
