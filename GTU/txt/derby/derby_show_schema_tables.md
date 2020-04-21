derby_show_schema_tables.md
---
	
     select s.schemaname || '.' || t.tablename  , t.*, s.*
     from sys.systables t, sys.sysschemas s  
     where t.schemaid = s.schemaid 
          and t.tabletype = 'T' 
     order by s.schemaname, t.tablename

        --select * from SYS.SYSTABLES