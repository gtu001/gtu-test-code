
rem echo off
set arg1=%1
set arg2=%2
shift
shift

rem %* 表示所有參數
start java -jar %arg1%  %arg2%  %*

exit

