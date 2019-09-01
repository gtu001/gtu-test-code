#!/bin/bash

prog_home="./"
prog_home="/media/gtu001/OLD_D/my_tool/FastDBQueryUI"

cd ${prog_home} 

for lib_path in `ls ${prog_home}/*.exe`
do 
  prog_lib=${prog_lib}:${lib_path}
done

java -cp .:${prog_lib}  gtu._work.ui.FastDBQueryUI  $1
