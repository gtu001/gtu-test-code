powershell_UnauthorizedAccess_錯誤處理.md
---
. : 因為這個系統上已停用指令碼執行，所以無法載入 C:\Users\wistronits\Documents\WindowsPowerShell\profile.ps1 檔案。如需
詳細資訊，請參閱 about_Execution_Policies，網址為 https:/go.microsoft.com/fwlink/?LinkID=135170。
位於 線路:1 字元:3
+ . 'C:\Users\wistronits\Documents\WindowsPowerShell\profile.ps1'
+   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    + CategoryInfo          : SecurityError: (:) [], PSSecurityException
    + FullyQualifiedErrorId : UnauthorizedAccess


===
用管理員模式開啟

or

開啟後執行此指令
	Set-ExecutionPolicy RemoteSigned 
	or
	Set-ExecutionPolicy unrestricted

	改回原來設定
		Get-ExecutionPolicy
		Set-ExecutionPolicy Restricted

