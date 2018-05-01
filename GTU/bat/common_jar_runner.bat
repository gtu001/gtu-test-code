
rem echo off
set arg1=%1
set arg2=%2
shift
shift

rem %* 表示所有參數
java -jar %arg1%  %arg2%  %*
