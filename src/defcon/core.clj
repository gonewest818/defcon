(ns defcon.core
  (:require [environ.core :refer [env]]))


(defn- cast-to
  "Switch on the keyword 'type' and cast value appropriately, where
  supported types are :integer, :float, :boolean, and :string."
  [type val]
  (case type
    :integer (Integer/parseInt val)
    :float   (Float/parseFloat val)
    :boolean (Boolean/parseBoolean val)
    :string  (str val)
    val))


(defn- mandatory-var-exception
  "Throws a (for now generic) exception with descriptive string."
  [name]
  (throw (Exception.
          (str "Error in configuration. Required setting not provided: "
               name))))


(defn- get-config-or-default
  "Get environment variable or use default, casting to the specified type.
  
  This function depends on weavejester/environ to obtain the configuation
  values. See environ documentation for details on the search priority.
  
  In terms of applying defaults, if :default is non-nil and the variable
  isn't found elsewhere we substitute the default. If :default is nil then
  it is considered mandatory, and an exception will be thrown when the caller
  hasn't provided explicit configuration in the environment."
  [{:keys [name type default]}]
  (vector name
          (if-let [x (env name)]
            (cast-to type x)
            (or default (mandatory-var-exception name)))))

(defn init
  "Load configurables via environ, then cast to desired types, and
  finally return a hash-map containing the specified configurables
  and their values.
  
  Example:
  (def configurables
    [{:name :foo :type :integer :default 10}
     {:name :bar :type :boolean :default true}
     {:name :baz :type :string :default nil}])

  (init configurables)
  ; ==> {:foo 10, :bar true, :baz \"hello world\"}

  where in this example the value for :baz was resolved by environ
  either in the environment or in a properties file.

  Supported types are :integer, :float, :boolean, and :string. Values
  will be cast to their defined types.
  
  When :default is nil, the value must be found in the environment
  else we will throw an exception."
  [configurables]
  (->> configurables
       (map get-config-or-default)
       (into {})))
