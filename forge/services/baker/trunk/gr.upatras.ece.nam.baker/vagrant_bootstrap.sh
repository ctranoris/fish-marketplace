#!/usr/bin/env bash

set -ex

# Adding an apt gpg key is idempotent.
wget -q -O - https://get.docker.io/gpg | apt-key add -
# Creating the docker.list file is idempotent, but it may overwrite desired
# settings if it already exists. This could be solved with md5sum but it
# doesn't seem worth it.
echo 'deb http://get.docker.io/ubuntu docker main' >> /etc/apt/sources.list

apt-get update

apt-get -y --force-yes install \
openjdk-7-jdk \
git \
lxc-docker \
maven \
subversion

usermod -a -G docker "vagrant"

echo "Done installing, putting in place bootstrap content.."


