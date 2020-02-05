看modification檔案
---
  $ svn st 
  

看prop設定了什麼
---
  $ svn proplist
  

看svn:ignore設了什麼
---
  $ svn propget svn:ignore
  

刪除prop
---
  $ svn propdel svn:ignore


忽略某個目錄
---
	欲忽略 D:\work_tool\Z-Code\20200114\cashweb\WebRoot\WEB-INF\classes
	先切到 D:\work_tool\Z-Code\20200114\cashweb\WebRoot\WEB-INF
	$ svn propset svn:ignore <目錄> . [--recursive]
		Ex :  svn propset svn:ignore classes . 
	或
	$ svn propset svn:ignore '*' <目錄>/
		Ex :  svn propset svn:ignore '*' classes/
	或
	$ svn rm <目錄> --keep-local
		Ex :  svn rm classes --keep-local