#!/bin/sh
kill -9 `ps -ef  | grep -i  knecsaf.jar  | grep -v grep | awk '{print $2}'`
nohup java -jar /opt/applications/smppclients/safaricom/knecsaf.jar &

