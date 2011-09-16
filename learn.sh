#!/bin/sh
bindir=$(dirname $(readlink -f $0))
curdir=$(pwd)

name=$1
file="$(pwd)/$2"

cd $bindir
java -classpath ".:db4o-8.0.184.15484-all-java5.jar" userIdent/main learn $name $file
