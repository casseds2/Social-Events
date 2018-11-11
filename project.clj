(defproject social-events "0.1.0-SNAPSHOT"
  :description "Social Events App For Everyone and Anyone!"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.6"]
                 [day8.re-frame/async-flow-fx "0.0.11"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [cljsjs/chartjs "2.7.0-0"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [secretary "1.2.3"]
                 [compojure "1.6.1"]
                 [yogthos/config "0.8"]
                 [ring "1.7.1"]
                 [compojure "1.6.1"]
                 [liberator "0.15.2"]
                 [com.novemberain/monger "3.1.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [org.clojure/tools.cli "0.3.5"]
                 [aero "1.1.3"]]

  :main social-events.core

  :plugins [[lein-cljsbuild "1.1.7"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :test-paths ["test/clj" "test/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :server-logfile false
             :ring-handler social-events.handler/dev-handler}

  :repl-options {:welcome (println "Welcome to Social Events!")
                 :init (println "You are in the social-events.core (backend) namespace")
                 :prompt (fn [ns] (str "Thy Bidding for <" ns ">? "))
                 :host "0.0.0.0"
                 :port "4002"
                 :timeout 40000}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.3.3"]
                   [day8.re-frame/tracing "0.5.1"]
                   [re-frisk "0.5.3"]
                   [org.clojure/tools.nrepl "0.2.13"]]
    :plugins [[lein-figwheel "0.5.16"]
              [lein-doo "0.1.8"]]}
   :prod {:dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}
   :uberjar {:source-paths ["env/prod/clj"]
             :dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]
             :omit-source true
             :main social-events.core
             :aot [social-events.core]
             :uberjar-name "social-events.jar"
             :prep-tasks ["compile" ["cljsbuild" "once" "min"]]}}

  :cljsbuild
  {:builds
   [{:id "dev"
     :source-paths ["src/cljs"]
     :figwheel {:on-jsload "social-events.core/mount-root"}
     :compiler {:main social-events.core
                :output-to "resources/public/js/compiled/app.js"
                :output-dir "resources/public/js/compiled/out"
                :asset-path "js/compiled/out"
                :source-map-timestamp true
                :preloads [devtools.preload
                           day8.re-frame-10x.preload
                           re-frisk.preload]
                :closure-defines {"re_frame.trace.trace_enabled_QMARK_" true
                                  "day8.re_frame.tracing.trace_enabled_QMARK_" true}
                :external-config {:devtools/config {:features-to-install :all}}}}


    {:id "min"
     :source-paths ["src/cljs"]
     :jar true
     :compiler {:main social-events.core
                :output-to "resources/public/js/compiled/app.js"
                :optimizations :advanced
                :closure-defines {goog.DEBUG false}
                :pretty-print false}}

    {:id "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler {:main social-events.runner
                :output-to "resources/public/js/compiled/test.js"
                :output-dir "resources/public/js/compiled/test/out"
                :optimizations :none}}]})
    
  
