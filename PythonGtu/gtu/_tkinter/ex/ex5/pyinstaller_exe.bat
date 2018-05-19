
e:

mkdir python_build

cd e:\python_build

pyinstaller -F e:\workstuff\workspace\gtu-test-code\PythonGtu\gtu\_tkinter\ex\ex5\kill_port_ui_001.spec --noconsole 

REM --windowed  有視窗
REM --noconsole 無視窗
REM -F 打包成只有一個exe