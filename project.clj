(defproject quilt-web "0.1.0-SNAPSHOT"
  :description "A web interface to Quilt (Regex Compressor)"
  :url "http://regex-compressor.herokuapp.com"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :main quilt-web.core
  :uberjar-name "quilt-web.jar"
  :min-lein-version "2.6.1"
  :dependencies [[aleph "0.4.1-beta4"]
                 [clj-time "0.11.0"]
                 [com.layerware/hugsql "0.4.6"]
                 [enlive "1.1.6"]
                 [environ "1.0.2"]
                 [hiccup "1.0.5"]
                 [hiccup "1.0.5"]
                 [metosin/compojure-api "1.0.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [ring-refresh "0.1.2"]
                 [ring/ring-devel "1.5.0"]]

  :plugins [[lein-cljfmt "0.5.6"]
            [lein-environ "1.0.2"]]
  :test-refresh {:changes-only true}
  :profiles {:dev     {:source-paths ["dev"]
                       :env          {:host            "localhost"
                                      :reload          "true"
                                      :port            "3000"
                                      :database-url    "//postgres@localhost:5432/quilt-web-dev"}
                       :plugins      [[com.jakemccrary/lein-test-refresh "0.19.0"]
                                      [venantius/ultra "0.5.1"]]
                       :dependencies [[ring/ring-mock "0.3.0"]
                                      [cljfmt/cljfmt "0.5.6"]
                                      [org.clojure/core.async "0.2.395"]
                                      [org.clojure/tools.namespace "0.2.3"]]}
             :test    {:dependencies [[org.clojure/core.async "0.2.395"]]
                       :env          {:database-url "//postgres@localhost:5432/"}}
             :uberjar {:aot :all}})
