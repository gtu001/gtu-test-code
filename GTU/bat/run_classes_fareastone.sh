#!/usr/bin/ksh

prog_home="${HOME}/RPL/report/TO_BU"
Yesterday=`${HOME}/RPL/Batch/util/getDay.pl 1`

cd ${prog_home} 

for lib_path in `ls ${prog_home}/lib/*.jar`
do 
  prog_lib=${prog_lib}:${lib_path}
done

echo Yesterday = ${Yesterday}

/opt/jdk1.7.0_40/bin/java -cp ${prog_lib} com.fet.services.IVRReportMain_F1514 $1
