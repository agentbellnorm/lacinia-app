(ns datomic-db.user
  (:require [datomic.client.api :as d]
            [datomic-db.init :as init]
            [datomic-db.db :as db]))

(def cfg {:server-type :peer-server
          :access-key  "myaccesskey"
          :secret      "mysecret"
          :endpoint    "localhost:8998"})

(def client (d/client cfg))

(def conn (d/connect client {:db-name "hello"}))

(d/transact conn {:tx-data (datomic-db.init/schema)})

(d/transact conn {:tx-data (datomic-db.init/test-data)})

datomic-db.user/conn

(datomic-db.db/internal-find-game-by-id {:connection conn} "1237")

(datomic-db.db/internal-find-member-by-id {:connection conn} "37")

(datomic-db.db/internal-list-designers-for-game {:connection conn} "1235")
