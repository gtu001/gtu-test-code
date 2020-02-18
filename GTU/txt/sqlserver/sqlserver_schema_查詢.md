

在MS-SQL裡面要查詢Table schema通常會用到Information_Schema這訊息資料庫

 

以下就是常用的指令

 

--Where 的條件可以下 Where TABLE_NAME='XXXX'

--資料庫裏的所有欄位
---
	SELECT * FROM Information_Schema.COLUMNS

--目前資料庫中的索引鍵。
---
	SELECT * FROM Information_Schema.KEY_COLUMN_USAGE

--目前使用者在目前資料庫中可以存取的資料表
---
	SELECT * FROM Information_Schema.TABLES

--目前資料庫中的資料表條件約束
---
	SELECT * FROM Information_Schema.TABLE_CONSTRAINTS

--目前資料庫中的外部條件約束
---
	SELECT * FROM Information_Schema.REFERENTIAL_CONSTRAINTS

另外,也可以使用以下指令查詢出要找的Table Schema
---
	Select a.colid, a.name, IsNull(d.value, '') col_desp, 
		c.name + '(' + Convert(varchar(4), (Case When a.xtype In (99, 231, 239) Then a.length / 2 Else a.length End)) + ')' col_type, 
		(Case When b.pk_name Is Not Null Then 'PK' Else 
			(Case When a.isnullable = 0 Then 'NN' Else 'N' End) End) col_status,
		IsNull(e.text, '') col_default
	From sysobjects main
	Inner Join syscolumns a
		On main.id = a.id
	Left Join (
			Select a.id, c.name pk_name
			From sysindexes a
			Inner Join sysindexkeys b
				On a.id = b.id And a.indid = b.indid
			Inner Join syscolumns c
				On a.id = c.id And b.colid = c.colid
			Where a.status & 2048 = 2048) b
		On a.id = b.id And a.name = b.pk_name
	Inner Join systypes c
		On a.xtype = c.xtype And c.status = 0
	Left Join sys.extended_properties d
		On a.id = d.major_id And a.colid = d.minor_id And d.name = 'chnName'
	Left Join dbo.syscomments e
		On a.cdefault = e.id
	Where main.name = '輸入您的TABLENAME'
	Order By a.colid



===================================
以下可用於FastDBQuery
	        
        Select a.colid, a.name, 
                convert(varchar(100), 
                    IsNull(d.value, '')
                ) col_desp, 
                 convert(varchar(100), 
                    c.name + '(' + Convert(varchar(4), (Case When a.xtype In (99, 231, 239) Then a.length / 2 Else a.length End)) + ')'
                )  col_type, 
                convert(varchar(100), 
                	(Case When b.pk_name Is Not Null Then 'PK' Else 
                		(Case When a.isnullable = 0 Then 'NN' Else 'N' End) End) 
                ) col_status,
                convert(varchar(100), 
            	    IsNull(e.text, '') 
	        ) col_default
        From sysobjects main
        Inner Join syscolumns a
        	On main.id = a.id
        Left Join (
        		Select a.id, c.name pk_name
        		From sysindexes a
        		Inner Join sysindexkeys b
        			On a.id = b.id And a.indid = b.indid
        		Inner Join syscolumns c
        			On a.id = c.id And b.colid = c.colid
        		Where a.status & 2048 = 2048) b
        	On a.id = b.id And a.name = b.pk_name
        Inner Join systypes c
        	On a.xtype = c.xtype And c.status = 0
        Left Join sys.extended_properties d
        	On a.id = d.major_id And a.colid = d.minor_id And d.name = 'chnName'
        Left Join dbo.syscomments e
        	On a.cdefault = e.id
        Where  main.name = :TableName  
        Order By a.colid