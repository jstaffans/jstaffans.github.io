(set-env!
 :source-paths #{"src" "content" "styles"}
 :resource-paths #{"resources"}
 :dependencies '[[perun "0.4.2-SNAPSHOT" :scope "test"]
                 [pandeiro/boot-http "0.8.1" :scope "test"]
                 [deraen/boot-sass "0.3.1" :scope "test"]
                 [hiccup "1.0.5"]
                 [clj-time "0.14.0"]])

(require '[boot.pod :as pod]
         '[io.perun :as p]
         '[pandeiro.boot-http :refer [serve]]
         '[deraen.boot-sass :refer [sass]])

(defn- create-pod [deps]
  (-> (get-env)
      (update-in [:dependencies] into deps)
      pod/make-pod
      future))

(def highlight-deps
  '[[org.clojure/tools.namespace "0.3.0-alpha3"]
    [clygments "1.0.0"]])

(def highlight-defaults
  {:filterer identity
   :extensions [".html"]})

(deftask highlight
  [_ filterer   FILTER     code  "predicate to use for selecting entries (default: `identity`)"
   e extensions EXTENSIONS [str] "extensions of files to include"]
  (let [pod     (create-pod highlight-deps)
        options (merge highlight-defaults *opts*)]
    (p/content-task
     {:render-form-fn (fn [data] `(identity ~data))
      :paths-fn #(p/content-paths % options)
      :passthru-fn p/content-passthru
      :task-name "highlight"
      :tracer :tech.jstaffans/highlight
      :pod pod})))

(deftask build
  []
  (let [post? (fn [{:keys [path]}] (.startsWith path "public/posts"))
        page? (fn [{:keys [path]}] (.startsWith path "public/pages"))]
    (comp (sass)
       (sift :move {#"main.css" "public/styles/main.css"})
       (p/markdown)
       (p/collection :renderer 'site.core/index :filterer post? :sortby :date)
       (p/render :renderer 'site.core/post :filterer post?)
       (p/render :renderer 'site.core/page :filterer page?)
       (highlight :filterer page?))))


(deftask dev
  []
  (comp (serve :resource-root "public" :port 4000)
     (watch)
     (build)))
