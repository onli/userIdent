#!/bin/sh
$(dirname "$0")/arecord &
sleep 30
killall -SIGINT arecord
sleep 2
killall arecord
exit 0
