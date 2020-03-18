@echo off
title Lag and World not loading FIX V:1.0
color c
echo ~-=--------------------------------=-~
echo Lag and World not loading fix + FPS and stability increase
echo Beta release: V1.2
echo ~-=--------------------------------=-~
echo        GTA V pc script only
echo Lag and World not loading glitch fix (graphics not loading)
echo FPS and stability increase while in-game
echo ~-=--------------------------------=-~
echo Press any button to continue
pause >nul


cls
echo Task done! 1 out of 11
powercfg -s 8c5e7fda-e8bf-4a96-9a85-a6e23a8c635c
cls
echo Task done! 2 out of  11
taskkill /f /im GTAVLauncher.exe
cls
echo Task done! 3 out of 11
wmic process where name="GTA5.exe" CALL setpriority 128
cls
echo Task done! 4 out of 11
taskkill /f /im wmpnetwk.exe.exe
cls
echo Task done! 5 out of 11
taskkill /f /im OneDrive.exe
cls 
echo Task done! 6 out of 11
taskkill /f /im speedfan.exe
cls
echo Task done! 7 out of 11
taskkill /f /im lightshot.exe
cls
echo Task done! 8 out of 11
taskkill /f /im opera.exe
cls
echo Task done! 9 out of 11
taskkill /f /im java.exe
cls
echo Task done! 11 out of 11
cls
echo SCRIPT WORKED! ENJOY YOUR GTA V! (if you want to feel it working, you need to run it everytime you start up GTA V)
timeout /t 2 /nobreak