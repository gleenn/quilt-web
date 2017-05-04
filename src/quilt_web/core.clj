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
    #_api
    #_(GET "/.well-known/acme-challenge/:acme-token" [acme-token]
         (let [resp (sabayon-response (sabayon-config) acme-token)]
           (if (= (:status resp) 200)
             (-> resp
                 (response/content-type "text/plain; charset=utf-8"))
             (-> (not-found (json/write-str {:error 404}))
                 (response/content-type "application/json; charset=utf-8")))))

    #_(GET "/vote-buttons" [& params]
         (urban-api.vote-buttons/response params))

    (GET "/haro" [& params]
         "Haro werld")

    (ANY "/*" []
         (-> (not-found (json/write-str {:error 404}))
             (response/content-type "application/json; charset=utf-8")))))

(def app (-> all-routes
             (cond-> (env :reload) wrap-reload
                     (env :reload) wrap-refresh)))

(defn create-executor []
  (aleph.flow/utilization-executor
    (Float/parseFloat (env :aleph-utilization "0.9"))
    (Integer/parseInt (env :aleph-max-threads "512"))
    {}))

(defn run []
  (http/start-server #'app {:port     (Integer/parseInt (env :port))
                            :executor (create-executor)})
  (println "server running on port " (Integer/parseInt (env :port))))

(defn -main [& args]
  (run))
