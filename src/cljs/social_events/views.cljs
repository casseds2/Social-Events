(ns social-events.views
  (:require
   [re-frame.core :as re-frame]
   [social-events.screens.home-screen :refer [home-screen]]
   [social-events.screens.event-screen :refer [event-screen]]
   [social-events.screens.about-screen :refer [about-screen]]
   [social-events.screens.create-event-screen :refer [create-event-screen]]
   [social-events.components.navbar :refer [navbar]]
   [social-events.subs :as subs]))

(defn- panels [panel-name]
  (case panel-name
    :home-screen [home-screen]
    :event-screen [event-screen]
    :about-screen [about-screen]
    :create-event-screen [create-event-screen]
    [:div {:class "is-centered-x-y"}
     [:h1 "Error, No Panel!"]]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div {:style {:height "100vh" :width "100%"}}
     [navbar]
     [panels @active-panel]]))
