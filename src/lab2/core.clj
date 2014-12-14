(ns lab2.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clj-http.client :as client])
  (:require [clj-http.links :as link])
)

(declare parseLinks)
(declare get-valid-urls)

(defn- readFile [filePath]
  (with-open [reader (io/reader filePath)]
    (reduce conj [] (line-seq reader)))
)

(defn getLink [data]
  (map (fn [vecM] (:href vecM)) data)
  )

(defn parseLink
  [externalUrl depth]
  (let [response (client/get externalUrl { :throw-exceptions false })
        statusCode (:status response)]
    (cond
      (= statusCode 200)
      (let [subLink (getLink (:links (client/get externalUrl)))]
        {:numOfLinks (count subLink)
         :links (if (> depth 0)
        (parseLinks subLink (- depth 1))
        []) })
      (= statusCode 404) "Page not found"
      (contains? [300 301 302 303 307] statusCode)
        (let [traceRedirects (:trace-redirects response)]
        (str "Redirect to " (peek traceRedirects)))
    )
  )
)

(defn parseLinks
  [links depth]
  (pmap (fn [link] { link (parseLink link depth) }) links)
)

(defn -main
  [& args]
  (println "Start lab")
  (let [links (readFile "./resources/links")
        linksMap (parseLinks links 2)]
    (println links)
    (println linksMap)
  )
  (println "End lab")
)


