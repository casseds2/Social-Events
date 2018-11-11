(ns social-events.db)

(def default-db
   {:active-panel :home-screen
    :app-state {:creating-event? false
                :fetching-events? false
                :fetching-event? false
                :updating-event? false
                :fetched-all-events? false}
    :events {}
    :selected-event {}})
