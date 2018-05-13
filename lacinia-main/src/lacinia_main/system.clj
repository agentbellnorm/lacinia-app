(ns lacinia-main.system
  (:require [com.stuartsierra.component :as component]
            [lacinia-core.schema :as schema]
            [lacinia-core.server :as server]
            [file-db.db :as db]))

(defn new-system
  []
  (merge (component/system-map)
         (server/new-server)
         (schema/new-schema-provider)
         (db/new-db)))
