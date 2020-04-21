#!/bin/bash

BASEDIR=$(dirname "$0")

prog_home="./"
prog_home=${BASEDIR}

cd ${prog_home} 

for lib_path in `find  ${BASEDIR}  -maxdepth  1  -regex  '.*\.(?:exe|jar)' `
do 
  prog_lib=${prog_lib}:${lib_path}
done

java -cp .:${prog_lib}  gtu._work.ui.FastDBQueryUI  $1
