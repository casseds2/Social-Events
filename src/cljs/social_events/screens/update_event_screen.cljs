(ns social-events.screens.update-event-screen
  (:require
    [re-frame.core :refer [subscribe dispatch]]
    [reagent.core :as reagent]
    [social-events.subs :as subs]
    [social-events.events :as events]))

(def page-state
     (reagent/atom {}))

(def page-updates
     (reagent/atom {}))

(defn- event-banner [event]
       [:section {:class "hero is-primary"}
        [:div {:class "hero-body"}
         [:div {:class "container"}
          [:input {:class "input"
                   :type "text"
                   :value (:title event)
                   :placeholder "Event Name"
                   :style {:max-width "50%"}
                   :on-change #(swap! page-updates assoc :title (-> % .-target .-value))}]
          [:h2 {:class "subtitle"} (str "by " (:creator event))]
          [:input {:class "input"
                   :type "text"
                   :value (:date-string event)
                   :placeholder "Date (DD/MM/YYYY)"
                   :style {:max-width "50%"}
                   :on-change #(swap! page-updates assoc :date-string (-> % .-target .-value))}]]]])

(defn event-body [event]
      [:section {:class "section"}
       [:div {:class "container is-full-width"}
        [:textarea {:class "textarea is-medium"
                    :rows "8"
                    :value (:description event)
                    :placeholder "Event Descripti"
                    :on-change #(swap! page-updates assoc :description (-> % .-target .-value))}]]])

(defn- participant-item [member]
       [:li
        [:h6 {:class "subtitle is-6"} member
         [:a {:class "icon has-text-danger"
              :on-click #(swap! page-updates assoc :participants (filterv (fn [name]
                                                                            (not= member name))
                                                                          (:participants @page-updates)))}
          [:i {:class "far fa-trash-alt"}]]]])

(defn- add-participant []
       (let [{:keys [username]} @page-state]
         [:div
          [:input {:class "is-primary is-full-width is-family-monospace"
                   :type "text"
                   :placeholder "Name"
                   :on-change #(swap! page-state assoc :username (-> % .-target .-value))
                   :value (:username @page-state)
                   :style {:padding "10px"}}]
          [:button {:class "button is-primary is-full-width"
                    :disabled (clojure.string/blank? username)
                    :on-click #(do (swap! page-updates assoc :participants (conj (:participants @page-updates) username))
                                   (swap! page-state assoc :username ""))} "Add"]]))

(defn- participants [event]
       (let [{:keys [show-participants?]} @page-state
             icon (if show-participants? "fas fa-angle-down" "fas fa-angle-up")
             count-participants (count (:participants event))
             header-title (if show-participants? "Participants" (str "Participants (" count-participants ")"))]
         [:div {:class "card is-overflow-capable"}
          [:header {:class "card-header"}
           [:p {:class "card-header-title"} header-title]
           [:a {:class "card-header-icon"
                :on-click #(swap! page-state assoc :show-participants? (not show-participants?))}
            [:span {:class "icon"}
             [:i {:class icon}]]]]
          (if show-participants?
            [:div {:class "card-content"}
             [:div {:class "content"}
              [:ol {:type "1"}
               (doall
                 (map-indexed (fn [index participant]
                                ^{:key index} [participant-item participant])
                              (:participants event)))]]])
          [add-participant]]))

(defn update-event-screen []
  (let [selected-event (subscribe [::subs/selected-event])]
    (reagent/create-class
      {:component-did-mount
         (fn []
           (reset! page-updates {:title ""
                                 :description ""
                                 :date-string ""
                                 :participants []
                                 :creator ""})
           (reset! page-state {:show-participants? true
                               :synced? false
                               :username ""}))
       :component-did-update
         (fn []
           (if-not (:synced? @page-state)
             (do (swap! page-updates merge @selected-event)
                 (swap! page-state assoc :synced? true))))
       :reagent-render
         (fn []
           [:div {:class "container is-full-height" :style {:padding "5%"}}
            [:div {:class "columns is-full-height"}
             [:div {:class "column is-9" :style {:max-width "100%"}}
              [:div {:class "is-full-height" :style {:background-color "#efefef"}}
               [event-banner @page-updates]
               [event-body @page-updates]]
              [:button {:class "button is-primary"
                        :on-click #(dispatch [::events/update-event (:_id @selected-event) @page-updates])} "Save"]
              [:a {:class "button is-danger"
                   :href (str "/#/event/" (:_id @selected-event))} "Cancel"]]
             [:div {:class "column is-3"}
              [participants @page-updates]]]])})))
