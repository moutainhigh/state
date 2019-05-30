#!/bin/sh
echo -e "start .."
source /etc/profile
nohup java -server -Xms1000m -Xmx1000m -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=7184  -Dapp_name=score -jar /home/admin/software/score/score.jar 1>>/home/admin/software/score/score.log 2>&1  &

echo "start score success..."