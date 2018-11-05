(ns social-events.db)

(def default-db
   {:events {"1" {:id "1" :title "Halloween Party" :date-string "31/10/18" :participants ["Dave" "Ed" "Hugh"] :creator "Rachael" :description "Hi guys, was planning on having a Halloween party if anyone is interested! Respond if you want to go :)"}
             "2" {:id "2" :title "Christmas Party" :date-string "22/12/18" :participants ["Roy" "Thomas" "Sarah"] :creator "Tony"}
             "3" {:id "3" :title "Retirement Party" :date-string "22/12/18" :participants ["Roy" "Thomas" "Sarah"] :creator "Stephen"}
             "4" {:id "4" :title "Easter Party" :date-string "22/12/18" :participants ["Roy" "Thomas" "Sarah"] :creator "Aaron"}
             "5" {:id "5" :title "Cake Party" :date-string "22/12/18" :participants ["Roy" "Thomas" "Sarah"] :creator "Rachael"}}
    :selected-event {}})
