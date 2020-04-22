derby_show_schema_tables.md
---
	

             select s.schemaname || '.' || t.tablename as table_n_schema,
                t.TABLEID,
                t.TABLENAME,
                t.TABLETYPE,
                t.SCHEMAID,
                t.LOCKGRANULARITY,
                s.SCHEMAID,
                s.SCHEMANAME,
                s.AUTHORIZATIONID
             from sys.systables t, sys.sysschemas s  
             where t.schemaid = s.schemaid 
                  and t.tabletype = 'T' 
             order by s.schemaname, t.tablename



        

        select * from SYS.SYSTABLES