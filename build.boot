(set-env!
 :source-paths #{"src" "content" "styles"}
 :dependencies '[[perun "0.4.2-SNAPSHOT" :scope "test"]
                 [pandeiro/boot-http "0.8.1" :scope "test"]
                 [deraen/boot-sass "0.3.1" :scope "test"]
                 [hiccup "1.0.5"]
                 [clj-time "0.14.0"]])

(require '[io.perun :as p]
         '[pandeiro.boot-http :refer [serve]]
         '[deraen.boot-sass :refer [sass]])

(deftask build
  []
  (let [post? (fn [{:keys [path]}] (.startsWith path "public/posts"))
        page? (fn [{:keys [path]}] (.startsWith path "public/pages"))]
    (comp (sass)
       (sift :move {#"main.css" "public/styles/main.css"})
       (p/markdown)
       (p/collection :renderer 'site.core/index :filterer post?)
       (p/render :renderer 'site.core/post :filterer post?)
       (p/render :renderer 'site.core/page :filterer page?))))


(deftask dev
  []
  (comp (serve :resource-root "public" :port 4000)
     (watch)
     (build)))
