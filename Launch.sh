#!/bin/bash
# cron */1 * * * * /srv/web-lombard-connector/Launch.sh
i=0
do
  app=$(ps aux | grep java | grep web-lombard-connector | grep -v -c grep) > /dev/null
  if [ $app -eq 0 ]
    then {
      exec java -jar /srv/web-lombard-connector/web-lombard-connector.jar > /dev/null
    }
  fi
done