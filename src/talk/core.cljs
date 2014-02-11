(ns talk.core
 (:require-macros [cljs.core.async.macros :refer [go]])
 (:require [om.core :as om :include-macros true]
           [om.dom :as dom :include-macros true]
           [goog.events :as events]
           [cljs.core.async :refer [put! chan <!]]
           [talk.crypt :as crypt]))

(enable-console-print!)

; new messages could be stored as drafts in the application state
; that way oms cursers could be utilized to handle the change of that
; message as well as moving the message into the thread when submitting
; it

(def app-state
  (atom
    {:messages
      [{:type :message :content "Hi!" :author :mklepsch}
       {:type :message :content "Hi!" :author :tnick}
       {:type :message :content "How are you doing today Tom?" :author :mklepsch}
       {:type :message :content "I'm doing great — how about you, @mklepsch?" :author :tnick}
       {:type :message :content "I'm a bit sick but had a good day nontheless" :author :mklepsch}
       {:type :message :content "Any plans for the weekend?" :author :mklepsch}
       {:type :message :content "Not yet. Heard about this Hackathon..." :author :tnick}
       {:type :message :content "Think the name was static showdown or the like" :author :tnick}]
     :users
      {:mklepsch {:email "martinklepsch@googlemail.com"}
       :tnick {:email "tom.w.a.nick@googlemail.com"}}
     :current-user :mklepsch }))

(defn current-user [app]
  (merge {:handle (:current-user app)} ((:current-user app) (:users app))))

(defn messages [app]
  (->> (:messages app)
    (mapv (fn [x]
            (update-in x [:author]
              (fn [cs] (cs (:users app))))))))

(defn gravatar [email]
  (let [h email]
    (str "http://www.gravatar.com/avatar/" (crypt/md5-hex email) "?s=30")))

(defn add-message [app owner]
  (let [content (.-value (om/get-node owner "new-contact"))
        message {:type :message :content content :author :mklepsch}]

    (when message
      (om/transact! app :messages conj message))))

(defn message [message owner]
  (reify
    om/IRenderState
    (render-state [_ state]
      (let [imgsrc ""]
        (dom/div #js {:className "message"}
          (dom/img #js {:className "message__author"
                        :src       imgsrc }
            nil)
          (dom/p #js {:className "message__text"}
            (:content message)))))))

(defn form [app owner]
  (reify
    om/IRenderState
    (render-state [_ state]
      (dom/div #js {:className "message"}
        (dom/img #js {:className "message__author"
                      :src       (gravatar (om/get-shared owner :email))}
          nil)
        (dom/input #js {:type "text" :ref "new-contact" :value (:text state)})
        (dom/button #js {:onClick #(add-message app owner)} "Send")))))

(defn thread-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:form ""})
    om/IRenderState
    (render-state [_ state]
      (dom/div #js {:className "thread"}
        (dom/h2 nil (:content (first (messages app))))
        (apply dom/div nil
          (map #(om/build message %) (rest (messages app))))
        (om/build form {})))))

(current-user @app-state)
(om/root app-state (current-user @app-state) thread-view (. js/document (getElementById "app")))
