#!/bin/sh
sox "$1" "$2" silence 1 0.1 0.1% -1 0.1 0.1%
