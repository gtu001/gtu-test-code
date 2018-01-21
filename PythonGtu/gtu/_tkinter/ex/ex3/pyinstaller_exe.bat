
d:

mkdir python_build

cd d:\python_build

pyinstaller -F D:\workstuff\workspace\gtu-test-code\PythonGtu\gtu\_tkinter\ex\ex3\excel_test_005_ui.spec --noconsole 

rem --windowed  有視窗
rem --noconsole 無視窗
rem -F 打包成只有一個exe