sqlite_查詢所有表.md
---
	

    SELECT 
        name
    FROM 
        sqlite_master 
    WHERE 
        type ='table' AND 
        name NOT LIKE 'sqlite_%' 