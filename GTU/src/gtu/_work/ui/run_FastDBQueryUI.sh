#!/bin/bash

BASEDIR=$(dirname "$0")

prog_home=${BASEDIR}

list=(`find  ${BASEDIR}  -regextype posix-egrep -regex  '.*\.(exe|jar)$'`)

printf "list_size = %s\n" "${#list[@]}"

for lib_path in ${list[@]}
do 
	printf "jar = %s\n" "${lib_path}"
	prog_lib=${prog_lib}:${lib_path}
done

java -cp .:${prog_lib}  gtu._work.ui.FastDBQueryUI  $1
