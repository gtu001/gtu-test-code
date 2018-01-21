
d:

mkdir python_build

cd d:\python_build

pyinstaller -F D:\workstuff\workspace\gtu-test-code\PythonGtu\gtu\_tkinter\ex\ex1\excel_test_003_ui.spec --noconsole 

rem --windowed  有視窗
rem --noconsole 無視窗
rem -F 打包成只有一個exe