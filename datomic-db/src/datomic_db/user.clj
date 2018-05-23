(ns datomic-db.user
  (:require [datomic.client.api :as d]
            [datomic-db.init :as init]
            [datomic-db.db :as db]
            [lacinia-core.schema :as schema]))

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

(datomic-db.db/internal-list-games-for-designer {:connection conn} "200")

(datomic-db.db/internal-list-ratings-for-member {:connection conn} "37")

(datomic-db.db/internal-list-ratings-for-game {:connection conn} "1237")

(comment {:rating/member-id "2812" :rating/game-id "1237" :rating/rating 4})

(datomic-db.db/internal-upsert-game-rating {:connection conn} "1237" "2812" -1)
