(ns site.core
  (:require [hiccup.page :as hp]
            [site.common :refer [head header post-date]]))

(defn trace
  [x]
  (prn x)
  x)

(defn base
  [content]
  (hp/html5
   (head)
   [:div
    (header)
    [:div.container
     content]]))

(defn index
  [{:keys [entries]}]
  (base
   [:div (str (count entries) " posts.")]))

(defn post
  [data]
  (base
   [:div
    [:div.flex.items-baseline.justify-between
     [:h2 (-> data :entry :title)]
     [:div.h3 (post-date (-> data :entry :date))]]
    [:div
     (-> data :entry :content)]]))

(defn page
  [data]
  (base
   (-> data :entry :content)))
