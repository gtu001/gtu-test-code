
del /f /s /q  D:\python_build\*.*

rmdir /s /q d:\python_build\dist


d:

mkdir python_build

cd d:\python_build

chcp 65001 

pyinstaller -F   D:\workstuff\gtu-test-code\PythonGtu\gtu\_tkinter\ex\ex7\excel_test_rpt_8110_cellCompare_UI.spec    --noconsole 


D:\python_build\dist\excel_test_rpt_8110_cellCompare_UI\excel_test_rpt_8110_cellCompare_UI.exe


REM --windowed  有視窗
REM --noconsole 無視窗
REM -F 打包成只有一個exe


PAUSE