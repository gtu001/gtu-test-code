

#PS : 重要 設定lib路徑
export LD_LIBRARY_PATH=/home/gtu001/anaconda3/lib

rm  -rfd  /media/gtu001/OLD_D/python_build/*.*


cd     /media/gtu001/OLD_D/
mkdir  python_build

cd /media/gtu001/OLD_D/python_build/

pyinstaller -F   /media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/PythonGtu/gtu/_tkinter/ex/ex7/excel_test_rpt_8110_cellCompare_UI_linux.spec     --noconsole 


#執行exe
./media/gtu001/OLD_D/python_build/dist/excel_test_rpt_8110_cellCompare_UI/excel_test_rpt_8110_cellCompare_UI


REM --windowed  有視窗
REM --noconsole 無視窗
REM -F 打包成只有一個exe