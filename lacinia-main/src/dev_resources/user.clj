(ns dev-resources.user
  (:require
    [com.walmartlabs.lacinia :as lacinia]
    [clojure.java.browse :refer [browse-url]]
    [lacinia-main.system :as system]
    [clojure.walk :as walk]
    [com.stuartsierra.component :as component])
  (:import (clojure.lang IPersistentMap)))

(defn simplify
  "Converts all ordered maps nested within the map into standard hash maps, and
   sequences into vectors, which makes for easier constants in the tests, and eliminates ordering problems."
  [m]
  (walk/postwalk
    (fn [node]
      (cond
        (instance? IPersistentMap node)
        (into {} node)

        (seq? node)
        (vec node)

        :else
        node))
    m))

(defonce system (system/new-system))

(defn q
  [query-string]
  (-> system
      :schema-provider
      :schema
      (lacinia/execute query-string nil nil)
      simplify))

(defn start
  []
  (alter-var-root #'system component/start-system)
  (browse-url "http://localhost:8888/")
  :started)

(defn stop
  []
  (alter-var-root #'system component/stop-system)
  :stopped)

(start)

(comment (stop))

(q "{ game_by_id(id: \"1237\") { name rating_summary { count average }}}")
(q "{ game_by_id(id: \"1237\") { name }}")

(q "{ member_by_id(id: \"1410\") { member_name ratings { game { id name } rating }}}")
(q "{ member_by_id(id: \"1410\") { member_name }}")

(q "{ member_by_id(id: \"1410\") { member_name ratings { game { name rating_summary { count average } designers { name  games { name }}} rating }}}")

(q "mutation { rate_game(member_id: \"1410\", game_id: \"1236\", rating: 6) { rating_summary { count average }}}")