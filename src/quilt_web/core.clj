(ns quilt-web.core
  (:gen-class)
  (:require
    [aleph.flow]
    [aleph.http :as http]
    [clojure.string :as string]
    [environ.core :refer [env]]
    [clojure.data.json :as json]
    [compojure.core :refer [wrap-routes]]
    [compojure.api.sweet :refer [context defapi GET POST ANY swagger-routes routes]]
    [compojure.api.impl.logging :as logging]
    [compojure.api.exception :refer [stringify-error]]
    [hiccup.core :as hiccup]
    [hiccup.page :as page]
    [ring.middleware.params :refer [wrap-params]]
    [ring.util.response :as response]
    [ring.middleware.reload :refer [wrap-reload]]
    [ring.middleware.refresh :refer [wrap-refresh]]
    [ring.util.http-response :refer [ok bad-request not-found internal-server-error]]))

(def all-routes
  (routes
    (GET "/" [& params]
         (-> (page/html5 [:body {:style "margin: 20px"}
                        [:div
                         [:h1 "Regex Compressor"]
                         [:p "Enter a list of expressions you want compressed into a regular expression that matches them all"]
                         [:form {:style "width: 100%"}
                          [:textarea {:name "words" :size 100 :cols 80 :rows 30 :style "vertical-align: middle" :value ()}]
                          [:input {:type "submit" :value "Compress" :style "vertical-align: middle"}]
                          [:textarea {:name "output" :size 100 :cols 80 :rows 30 :style "vertical-align: middle"}]]]])
             response/response
             (response/header "Content-Type" "text/html")))

    (GET "/haro" [& params] "Haro werld")

    (ANY "/*" [] (response/redirect "/"))))

(def app (-> all-routes
             (cond-> (env :reload) wrap-reload
                     (env :reload) wrap-refresh)))

(defn create-executor []
  (aleph.flow/utilization-executor
    (Float/parseFloat (env :aleph-utilization "0.9"))
    (Integer/parseInt (env :aleph-max-threads "512"))
    {}))

(defn run []
  (let [port (Integer/parseInt (env :port))]
    (http/start-server #'app {:port port
                              :executor (create-executor)})
    (println "server running on port" port)))

(defn -main [& args]
  (run))
