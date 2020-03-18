pimax 啟動特性

# 啟動
---
	切換到 C:\Program Files\Pimax\Runtime
	執行
		PiService.exe
	後
		會看到 
			PiTool.exe
			pi_server.exe
			pi_overlay.exe
		有看到 pi_overlay.exe 常駐就會有畫面

# 關閉
---
	關掉 pi_server.exe 即可


====
# 最好方法
	已下令存 bat 用系統管理員執行 執行一次開 / 執行一次關


echo off
set ServiceName=PiServiceLauncher
set ProcessName=PiService.exe
tasklist /fi "imagename eq %ProcessName%" |find /v ":" > nul&&(
sc query "%ServiceName%"|find “STATE”|find /v "RUNNING">nul&&(
sc start %ServiceName%
timeout 10
)
sc stop %ServiceName%
) || (
sc start %ServiceName%
)