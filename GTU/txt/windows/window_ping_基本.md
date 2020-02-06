ping_基本
---
	$ ping -t -a <ip,url>

	Ps : 若出現 "回覆自 10.35.253.35: TTL 在傳輸時到期。"
		表示有可能卡到

$ tracert <ip,url>
---
	看卡在哪

	有卡 解法:
		$ ipconfig /flushdns