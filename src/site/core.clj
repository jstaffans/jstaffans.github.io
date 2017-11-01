(ns site.core
  (:require [hiccup.page :as hp]
            [site.common :refer [head header]]))

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
   (-> data :entry :content)))

(defn page
  [data]
  (base
   (-> data :entry :content)))
