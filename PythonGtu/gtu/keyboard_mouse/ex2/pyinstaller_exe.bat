
d:

mkdir python_build

cd d:\python_build

pyinstaller -F D:\workstuff\workspace\gtu-test-code\PythonGtu\gtu\keyboard_mouse\ex2\pyhk_usage_calljar.spec --noconsole 

rem --windowed  有視窗
rem --noconsole 無視窗
rem -F 打包成只有一個exe