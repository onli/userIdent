#!/bin/sh
$(dirname "$0")/wavrecord &
sleep 30
killall -SIGINT wavrecord
sleep 2
killall wavrecord
sox -M channel4.wav channel1.wav record.wav
exit 0
