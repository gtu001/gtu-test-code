
  

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



======
看modification檔案
---
	$ svn status
	$ svn st 


新增檔案
---
	需要新增的檔案開頭為 ?
	$ svn add  --force --force-interactive  <從 svn status 看到的路徑>  
		ps : 可接 "*" 省略部份輸入


取消add檔案
---
	$ svn revert --recursive <folder_name>


上傳檔案
---
	$ svn commit -m "<msg>"


====================================

看log 含檔案清單
---
	svn log --verbose -r <版號>


看log 最近幾版
---
	svn log -limit 5   <---最近五版
	
	
看log 從某日期開始
---
	svn log -r {2011-02-02}:{2011-02-03}  <---起訖都要不然無效

====================================


檔案lock解開
---
	$ svn cleanup
	$ svn status <--複製開頭為Ｌ路徑
	$ svn unlock <path>


取得某特定版本的某檔
---
	$ svn up -r <version> <path_to_file>
	Ex : svn up -r 51 EupGobal/app/src/main/res/layout/activity_login.xml

