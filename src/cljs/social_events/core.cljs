(ns social-events.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [social-events.events :as events]
   [social-events.routes :as routes]
   [social-events.views :as views]
   [social-events.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
