#!/bin/bash
if [ -z "$1" ]; then
   echo "usage: ./upload.sh <server>"
   exit
fi

scp target/scalapress.war ubuntu@$1:ROOT.war
scp target/scalapress-deps.jar ubuntu@$1:scalapress-deps.jar