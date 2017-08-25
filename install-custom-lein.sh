#!/bin/bash -ex

# we're deploying with this custom build of leiningen until
# our pull request supporting unattended signatures is merged.
# (https://github.com/technomancy/leiningen/pull/2279)


mkdir -p _tools
cd _tools
git clone https://github.com/gonewest818/leiningen.git
cd leiningen
git checkout unattended-signatures
cd leiningen-core
lein bootstrap
cd ..
bin/lein compile

