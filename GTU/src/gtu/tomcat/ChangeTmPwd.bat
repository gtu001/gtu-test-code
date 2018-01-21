echo off

cls
:: Run the vbs code
start /w ChangeTmPwd.hta
:: At this point a batch file "%TEMP%\ExportVabData.bat" exists and you should 
:: call it! If you don't call the batch file here and instead opt to
:: call it from another batch file, be sure NOT to delete it in the
:: "Clean up" code section below!

if not exist "%TEMP%\Key.bat" goto END

call %TEMP%\Key.bat
del %TEMP%\Key.bat

java fillMd5Pwd %OLD_PWD% %NEW_PWD% %NEW_PWD_CONFIRM% %USER_ID%

rem echo ERRORLEVEL= %ERRORLEVEL%

:END
pause

rem IF %ERRORLEVEL% NEQ 0 GOTO SHOW_ERROR

rem cls
rem echo User Password 變更成功！
rem goto DONE

rem :SHOW_ERROR
rem echo User Password 變更失敗！
rem goto DONE

rem :DONE
rem pause
