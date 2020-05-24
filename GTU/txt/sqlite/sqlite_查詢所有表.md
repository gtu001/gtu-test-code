sqlite_查詢所有表.md
---
	

    SELECT 
        name
    FROM 
        sqlite_master 
    WHERE 
        type ='table' AND 
        name NOT LIKE 'sqlite_%' 
        
   
查詢column     
---
	  PRAGMA table_info(table_name)
	  




