# defcon

A Clojure library to handle configuration settings with defaults.

[![Build Status](https://travis-ci.org/gonewest818/defcon.svg?branch=master)](https://travis-ci.org/gonewest818/defcon)
[![Coverage Status](https://coveralls.io/repos/github/gonewest818/defcon/badge.svg?branch=master)](https://coveralls.io/github/gonewest818/defcon?branch=master)

## Usage

Defcon exposes a single function "init", where init loads
configurables via environ, then casts to desired types, and finally
return a hash-map containing the specified configurables and their
values.
  
Example:

    (def configurables
      [{:name :foo :type :integer :default 10}
       {:name :bar :type :boolean :default true}
       {:name :baz :type :string  :default nil}])

Environ resolves variables either in the shell environment or in a
properties file. Whereas here the names are keywordized, in the shell
environment or properties they would be names in uppercase (i.e. FOO,
BAR, BAZ).

Supported types are :integer, :float, :boolean, and :string. Values
will be cast to their defined types, or exceptions will be thrown if
the cast cannot be done.

When :default is nil, the value *must* be found in the environment
else we throw an exception.

    (init configurables)
    ; ==> {:foo 10, :bar true, :baz \"hello world\"}

Therefore in this example the values for :foo and :bar were *not*
provided in the environment, therefore their defaults were used
instead. :baz was successfully resolved by environ.


## License

Copyright © 2017 Neil Okamoto

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
