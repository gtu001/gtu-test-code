win_tail_效果指令.md
---
	下載安裝 rktools.exe
		https://www.microsoft.com/downloads/en/confirmation.aspx?familyId=9d467a69-57ff-4ae7-96ee-b18c4790cffd&displayLang=en 

	
	$ tail -10 -f data.txt

	Ex : 
		tail -10 -f C:\Users\wistronits\AppData\Local\Unity\Editor\Editor.log



---
	開啟 PowerShell
	$ Get-Content  data.txt  –wait

	Ex : 
		 Get-Content C:\Users\wistronits\AppData\Local\Unity\Editor\Editor.log –wait


    有Match 才秀
    $ Get-Content  data.txt   -wait | where { $_ -match “WARNING” }