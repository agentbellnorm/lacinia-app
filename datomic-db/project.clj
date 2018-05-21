(defproject datomic-db "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [lacinia-core/lacinia-core "0.1.0-SNAPSHOT"]
                 [com.datomic/client-pro "0.8.14"
                  :exclusions [org.eclipse.jetty/jetty-client
                               org.eclipse.jetty/jetty-http
                               org.eclipse.jetty/jetty-util]]])
