#!/bin/sh

service tomcat7 stop

echo "Deleting existing exploded WARs"
find /var/lib/tomcat7/sites -type d -name 'ROOT' | xargs -L 1 rm -rf

echo "Copying ROOT.war"
find /var/lib/tomcat7/sites -maxdepth 1 -mindepth 1 -type d | xargs -L 1 cp -v /home/ubuntu/ROOT.war

echo "Creating backup of ROOT.war"
cp /home/ubuntu/ROOT.war /home/ubuntu/ROOT.war.bak

echo "Creating backup of dependency lib"
cp /home/ubuntu/scalapress-deps.jar /home/ubuntu/scalapress-deps.jar.bak

echo "Copying dependency lib"
cp /home/ubuntu/scalapress-deps.jar /usr/share/tomcat/lib/

service tomcat7 restart
