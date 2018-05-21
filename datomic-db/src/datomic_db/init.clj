(ns datomic-db.init)

(defn schema
  []
  [
   ;;GAME
   {:db/ident       :game/id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "the id of the game"}

   {:db/ident       :game/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "the name of game"}

   {:db/ident       :game/summary
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "a short summary of a game"}

   {:db/ident       :game/min-players
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "the minimal number of players"}

   {:db/ident       :game/max-players
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "the maximum number of players of a game"}

   {:db/ident       :game/designers
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/many
    :db/doc         "the designers of the game"}

   ;;MEMBER
   {:db/ident       :member/id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The id of the member"}

   {:db/ident       :member/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The name of the member"}

   ;;DESIGNER
   {:db/ident       :designer/id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The id of the designer"}

   {:db/ident       :designer/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The name of the designer"}

   {:db/ident       :designer/url
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The webpage of the designer"}

   ;;RATING
   {:db/ident       :rating/game-id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "the id of the rated game"}

   {:db/ident       :rating/member-id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "the id of the member that rated the game"}

   {:db/ident       :rating/rating
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "the numerical rating of the game"}
   ])

(defn test-data
  []
  [
   ;; GAME
   {:game/id          "1234"
    :game/name        "Zertz"
    :game/summary     "Two player abstract with forced moves and shrinking board"
    :game/min-players 2
    :game/max-players 2
    :game/designers   ["200"]}

   {:game/id          "1235"
    :game/name        "Dominion"
    :game/summary     "Created the deck-building genre; zillions of expansions"
    :game/min-players 2
    :game/max-players 8
    :game/designers   ["204"]}

   {:game/id          "1236"
    :game/name        "Tiny Epic Galaxies"
    :game/summary     "Fast dice-based sci-fi space game with a bit of chaos"
    :game/min-players 1
    :game/max-players 4
    :game/designers   ["203"]}

   {:game/id          "1237"
    :game/name        "7 Wonders: Duel"
    :game/summary     "Tense, quick card game of developing civilizations"
    :game/min-players 2
    :game/max-players 2
    :game/designers   ["201" "202"]}

   ;;MEMBER
   {:member/id   "37"
    :member/name "curiousattemptbunny"}

   {:member/id   "1410"
    :member/name "bleedingedge"}

   {:member/id   "2812"
    :member/name "missyo"}

   ;;RATING
   {:rating/member-id "37" :rating/game-id "1234" :rating/rating 3}
   {:rating/member-id "1410" :rating/game-id "1234" :rating/rating 5}
   {:rating/member-id "1410" :rating/game-id "1236" :rating/rating 4}
   {:rating/member-id "1410" :rating/game-id "1237" :rating/rating 4}
   {:rating/member-id "2812" :rating/game-id "1237" :rating/rating 4}
   {:rating/member-id "37" :rating/game-id "1237" :rating/rating 5}

   ;;DESIGNER
   {:designer/id   "200"
    :designer/name "Kris Burm"
    :designer/url  "http://www.gipf.com/project_gipf/burm/burm.html"}

   {:designer/id   "201"
    :designer/name "Antoine Bauza"
    :designer/url  "http://www.antoinebauza.fr/"}

   {:designer/id   "202"
    :designer/name "Bruno Cathala"
    :designer/url  "http://www.brunocathala.com/"}

   {:designer/id   "203"
    :designer/name "Scott Almes"
    :designer/url  "www.google.com"}

   {:designer/id   "204"
    :designer/name "Donald X. Vaccarino"
    :designer/url  "www.svd.se"}
   ])