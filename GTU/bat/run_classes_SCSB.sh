#!/usr/bin/ksh

prog_home="/zEaiTest/"

cd ${prog_home} 

for lib_path in `ls ${prog_home}/libs/*.jar`
do 
  prog_lib=${prog_lib}:${lib_path}
done

#-Ddaemon=tcp:10.10.2.22:7500
#-Dsubject=scsb.eai.request
#-Dtimeout=360.0

java -Dfile.encoding=utf8 -cp ${prog_lib}:${prog_home}/bin com.test.Test001 $1
