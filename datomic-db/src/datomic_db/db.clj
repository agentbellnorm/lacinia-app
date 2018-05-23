(ns datomic-db.db
  (:require
    [datomic.client.api :as d]
    [com.stuartsierra.component :as component]
    [lacinia-core.protocol.data-provider :as data-provider]))

(defn printret
  [o]
  (println o)
  o)

(defn internal-find-game-by-id
  [component game-id]
  (let [db (d/db (:connection component))
        query '[:find ?name ?summary ?min-players ?max-players ?designers ;; TODO wrappa i vektor för att bara få ett resultat
                :in $ ?id
                :where [?e :game/name ?name]
                [?e :game/summary ?summary]
                [?e :game/min-players ?min-players]
                [?e :game/max-players ?max-players]
                [?e :game/designers ?designers]
                [?e :game/id ?id]]]
    (let [[name summary min-players max-players designers] (first (d/q query db game-id))]
      {:id          game-id
       :name        name
       :summary     summary
       :min_players min-players
       :max_players max-players
       :designers   #{designers}})))                        ;;TODO returnerar bara en designer

(defn internal-find-member-by-id
  [component member-id]
  "internal-find-member-by-id"
  (let [db (d/db (:connection component))
        query '[:find ?name
                :in $ ?id
                :where
                [?e :member/name ?name]
                [?e :member/id ?id]]]
    (let [[name] (first (d/q query db member-id))]
      {:id          member-id
       :member_name name})))

(defn internal-list-designers-for-game
  [component game-id]
  "internal-list-designers-for-game"
  (let [db (d/db (:connection component))
        query '[:find ?name ?url
                :in $ ?id
                :where
                [?game :game/id ?id]
                [?game :game/designers ?designer]
                [?designer-id :designer/id ?designer]
                [?designer-id :designer/name ?name]
                [?designer-id :designer/url ?url]]]
    (let [[name url] (first (d/q query db game-id))]
      {:id   game-id
       :name name
       :url  url})))

(defn internal-list-games-for-designer
  [component designer-id]
  "internal-list-games-for-designer"
  (let [db (d/db (:connection component))
        query '[:find ?name ?summary ?min-players ?max-players ?designers ?game-id
                :in $ ?id
                :where
                [?e :game/designers ?id]                    ;; TODO måste vara i en lista?
                [?e :game/name ?name]
                [?e :game/summary ?summary]
                [?e :game/min-players ?min-players]
                [?e :game/max-players ?max-players]
                [?e :game/designers ?designers]
                [?e :game/id ?game-id]]]
    (->> (d/q query db designer-id)
         (map #(let [[name summary min-players max-players designers game-id] %]
                 {:id          game-id
                  :name        name
                  :summary     summary
                  :min_players min-players
                  :max_players max-players
                  :designers   designers})))))

(defn internal-list-ratings-for-game
  [component game-id]
  "internal-list-ratings-for-game"
  (printret (let [db (d/db (:connection component))
                  query '[:find ?member-id ?rating
                          :in $ ?id
                          :where
                          [?e :rating/game-id ?id]
                          [?e :rating/member-id ?member-id]
                          [?e :rating/rating ?rating]]]
              (->> (d/q query db game-id)
                   (map #(let [[member-id rating] %]
                           {:game_id   game-id
                            :member_id member-id
                            :rating    rating}))))))

(defn internal-list-ratings-for-member
  [component member-id]
  "internal-list-ratings-for-member"
  (let [db (d/db (:connection component))
        query '[:find ?game-id ?rating
                :in $ ?id
                :where
                [?e :rating/member-id ?id]
                [?e :rating/rating ?rating]
                [?e :rating/game-id ?game-id]]]
    (->> (d/q query db member-id)
         (map #(let [[game-id rating] %]
                 {:member_id member-id
                  :game_id   game-id
                  :rating    rating})))))

(defn ^:private create-game-rating
  [component member-id game-id rating]
  "creates a new game rating if"
  (d/transact (:connection component) {:tx-data [{:rating/member-id member-id
                                                  :rating/game-id   game-id
                                                  :rating/rating    rating}]}))

(defn ^:private update-game-rating
  [component e-id rating]
  "updates an existing game rating datom"
  (d/transact (:connection component) {:tx-data [{:db/id         e-id
                                                  :rating/rating rating}]}))

(defn internal-upsert-game-rating
  "Adds a new game rating or changes the old value of an existing game rating"
  [component game-id member-id rating]
  (let [db (d/db (:connection component))
        get-query '[:find ?e
                    :in $ ?game-id ?member-id ?rating
                    :where
                    [?e :rating/game-id ?game-id]
                    [?e :rating/member-id ?member-id]]]
    (let [e-id (first (printret (d/q get-query db game-id member-id)))]
      (if (nil? e-id)
        (create-game-rating component member-id game-id rating)
        (update-game-rating component e-id rating)))))

(defn config []
  {:server-type :peer-server
   :access-key  "myaccesskey"
   :secret      "mysecret"
   :endpoint    "localhost:8998"})


(defrecord BoardGameTutorialDb [data]

  component/Lifecycle

  (start [this] (assoc this :connection (-> (config)
                                            (d/client)
                                            (d/connect {:db-name "hello"}))))

  (stop [this] (assoc this :connection nil))

  data-provider/DataProviderProtocol

  (find-game-by-id [data id] (internal-find-game-by-id data id))
  (find-member-by-id [data id] (internal-find-member-by-id data id))
  (upsert-game-rating [data game-id member-id rating] (internal-upsert-game-rating data game-id member-id rating))
  (list-designers-for-game [data game-id] (internal-list-designers-for-game data game-id))
  (list-games-for-designer [data game-id] (internal-list-games-for-designer data game-id))
  (list-ratings-for-game [data game-id] (internal-list-ratings-for-game data game-id))
  (list-ratings-for-member [data member-id] (internal-list-ratings-for-member data member-id)))

(defn new-db
  []
  {:db (map->BoardGameTutorialDb {})})