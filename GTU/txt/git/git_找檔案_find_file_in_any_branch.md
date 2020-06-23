找含有檔案CalBase.java
---
	$ git log --all -- '**/CalBase.java'   

	================
	commit e0a1b72cf2bd339a5f4e7f4e4e7a39d90eae51de (origin/Feature/#CalBaseDeal)
	Author: 孟祥焱 <mengxiangyan@sinosoft.hk>
	Date:   Thu Jun 11 11:51:43 2020 +0800

	    添加AllSumPrem累計所繳保費、PolSumAmnt本险累计投保金额、PolSumPrem本险累计所缴保险费

	commit a02e45bcceaeba439c59cd52e19d2461727e3d9f
	Author: 孟祥焱 <mengxiangyan@sinosoft.hk>
	Date:   Wed Jun 10 14:58:02 2020 +0800

	    將component中的calbase類移到ind中
	================

找含有該commit的branch
---
	$ git branch -a --contains e0a1b72 

	================
	  remotes/origin/Feature/#INF-0612-addCLMXml
	  remotes/origin/Feature/#NCSPOS-1122
	  remotes/origin/Feature/#NCSPOS-129
	  remotes/origin/Feature/#NOSPOS-412
	  remotes/origin/Merge_feature/#NCSPOS-1122-1_into_develop
	  remotes/origin/Merge_feature/#NCSPOS-1122_into_develop
	  remotes/origin/Merge_feature/#NCSPOS-1122_into_sit
	================
