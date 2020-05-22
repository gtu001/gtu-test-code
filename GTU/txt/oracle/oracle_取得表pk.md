oracle_取得表pk.md
---



    SELECT cols.table_name, cols.column_name, cols.position, cons.status, cons.owner
    FROM all_constraints cons, all_cons_columns cols
    WHERE 1=1 
     [ and   cols.table_name = upper(:TABLE_NAME) ]
     [ and   cols.owner = upper(:SCHEMA) ]
    AND cons.constraint_type = 'P'
    AND cons.constraint_name = cols.constraint_name
    AND cons.owner = cols.owner
    ORDER BY cols.table_name, cols.position


---


        SELECT
/*
          C.OWNER, C.TABLE_NAME, tab.COMMENTS as TAB_COMMENT,  C.COLUMN_ID, C.COLUMN_NAME, 
          c.DATA_TYPE, c.DATA_LENGTH, c.DATA_PRECISION, c.DATA_DEFAULT, 
          c.NULLABLE, r.COMMENTS as COLUMN_COMMENT
*/
            col_pk.PK as PK , r.COMMENTS as COLUMN_COMMENT, C.COLUMN_NAME , c.DATA_TYPE, c.DATA_LENGTH, C.TABLE_NAME
        FROM
          ALL_TAB_COLUMNS C 
        JOIN ALL_TABLES T ON 
          C.OWNER = T.OWNER AND C.TABLE_NAME = T.TABLE_NAME
        LEFT JOIN ALL_COL_COMMENTS R ON
          C.OWNER = R.Owner AND 
          C.TABLE_NAME = R.TABLE_NAME AND 
          C.COLUMN_NAME = R.COLUMN_NAME
        left join sys.user_tab_comments tab on
            tab.TABLE_NAME = c.TABLE_NAME 
           and tab.TABLE_TYPE = 'TABLE' 

        left join 
        (
            SELECT cols.table_name, cols.column_name, cols.position, cons.status, cons.owner, 'P' as PK
            FROM all_constraints cons, all_cons_columns cols
            WHERE 1=1 
            AND cons.constraint_type = 'P'
            AND cons.constraint_name = cols.constraint_name
            AND cons.owner = cols.owner
        )  col_pk on col_pk.table_name = c.TABLE_NAME and col_pk.owner = c.OWNER and col_pk.column_name = c.COLUMN_NAME  
 
        WHERE  1=1 
            and C.OWNER  = 'LIS'
           [ and C.OWNER  = upper(:schema) ]
           [ and C.TABLE_NAME like '%' || upper(trim(:tableName)) || '%' ]
        ORDER BY C.TABLE_NAME, C.COLUMN_ID





