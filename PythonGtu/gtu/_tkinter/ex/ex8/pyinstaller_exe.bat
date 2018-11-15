

del /f /s /q  D:\python_build\*.*

rmdir /s /q d:\python_build\dist


d:

mkdir python_build

cd d:\python_build

chcp 65001 

pyinstaller -F   D:\workstuff\gtu-test-code\PythonGtu\gtu\_tkinter\ex\ex8\janna_rename_ui_001.spec    --noconsole 


D:\python_build\dist\janna_rename_ui_001\janna_rename_ui_001.exe


REM --windowed  有視窗
REM --noconsole 無視窗
REM -F 打包成只有一個exe


PAUSE