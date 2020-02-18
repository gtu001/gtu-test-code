SQLServerException: 不支援“variant”資料類型
---
	解決方法
		Select 的欄位有自己組的文字  像是 ‘xxx’ as db_type 
		要改成 convert(varchar(100), ‘xxx’)  db_type,
