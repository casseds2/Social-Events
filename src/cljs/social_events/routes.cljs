(ns social-events.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
   [secretary.core :as secretary]
   [goog.events :as gevents]
   [goog.history.EventType :as EventType]
   [re-frame.core :as re-frame]
   [social-events.events :as events]))
   

(defn hook-browser-navigation! []
  (doto (History.)
    (gevents/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  (defroute "/spectre-home" []
    (re-frame/dispatch [::events/set-active-panel :spectre-home-panel]))
  (defroute "/spectre-about" []
    (re-frame/dispatch [::events/set-active-panel :spectre-about-panel])
    (defroute "/bulma-home" []
      (re-frame/dispatch [::events/set-active-panel :bulma-home-panel]))
    (defroute "/bulma-about" []
      (re-frame/dispatch [::events/set-active-panel :bulma-about-panel])))
  (hook-browser-navigation!))
