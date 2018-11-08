(ns social-events.db)

(def default-db
   { :app-state {:creating-event? false
                 :fetching-events? false}
    :events {}
    :selected-event {}})
