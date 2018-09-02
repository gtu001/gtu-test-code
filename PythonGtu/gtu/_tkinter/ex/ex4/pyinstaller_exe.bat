
e:

mkdir python_build

cd e:\python_build

pyinstaller -F e:\workstuff\workspace\gtu-test-code\PythonGtu\gtu\_tkinter\ex\ex4\excel_test_006_ui.spec --noconsole 

rem --windowed  有視窗
rem --noconsole 無視窗
rem -F 打包成只有一個exe