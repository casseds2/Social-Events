(ns social-events.db)

(def default-db
   {:active-panel :home-screen
    :app-state {:creating-event? false
                :fetching-events? false
                :fetching-event? false}
    :events {}
    :selected-event {}})
