(defproject org.clojars.gonewest818/defcon "0.5.0-SNAPSHOT"
  :description "Handle configuration settings with defaults"
  :url "http://github.com/gonewest818/defcon"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ "1.1.0"]]
  :profiles {:dev {:dependencies [[midje "1.8.3"]]
                   :plugins [[lein-midje "3.2.1"]
                             [lein-cloverage "1.0.9"]]}})
