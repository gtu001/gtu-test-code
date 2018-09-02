@if "%1"=="" goto end
@if "%1"=="HOME" goto home
@if "%1"=="COMPANY" goto company

:error
@echo 參數錯誤=[%1]
goto end

:home
@rem ===================================
@echo 家
@rem -----------------------------------
@set var="eclipse_jee"
@rem -----------------------------------
@echo 關閉IE Proxy代理
@reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyEnable /t REG_DWORD /d 0 /f
@rem -----------------------------------
@cd C:\Users\Troy\.m2
@if exist company_settings.xml goto skip_company
@ren settings.xml company_settings.xml
@ren home_settings.xml settings.xml
@:skip_company
@rem ===================================
goto common

:company
@rem ===================================
@echo 公司
@rem -----------------------------------
@set var="iisi_eclipse"
@rem -----------------------------------
@echo 啟用IE Proxy代理
@reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyEnable /t REG_DWORD /d 1 /f
@reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Internet Settings" /v ProxyServer /d "192.168.2.14:3128" /f
@rem -----------------------------------
@cd C:\Users\Troy\.m2
@if exist home_settings.xml goto skip_home
@ren settings.xml home_settings.xml
@ren company_settings.xml settings.xml
@:skip_home
@rem ===================================
goto common

:common
@rem ===================================
@start C:\資拓宏宇相關檔案\"%var%"\eclipse.exe
@start C:\apps\MozillaFirefox7\firefox.exe
@start C:\apps\MirandaIM\miranda32.exe
@start C:\apps\Lingoes\Lingoes.exe
@start C:\apps\BitComet1.28\BitComet_64.exe
@rem ===================================
goto end

:end
@echo end