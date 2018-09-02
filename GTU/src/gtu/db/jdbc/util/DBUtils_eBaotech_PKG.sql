
PROCEDURE P_READ_CLOB(as_sql varchar2,
                        ai_offset number,
                        ai_len number,
                        ar_raw out varchar2) is
    lb_clob clob;
    li_len number(12);
    cursor_name INTEGER;
    rows_processed INTEGER;
  begin
    li_len := ai_len;
    cursor_name := dbms_sql.open_cursor;
    DBMS_SQL.PARSE(cursor_name,
                   as_sql,
                   dbms_sql.native);
    dbms_sql.define_column(cursor_name,
                           1,
                           lb_clob);
    rows_processed := dbms_sql.execute(cursor_name);
    if dbms_sql.fetch_rows(cursor_name) > 0 then
      dbms_sql.column_value(cursor_name,
                            1,
                            lb_clob);
    end if;
    DBMS_SQL.close_cursor(cursor_name);
    dbms_lob.read(lb_clob,
                  li_len,
                  ai_offset,
                  ar_raw);
  exception
    when others then
      DBMS_SQL.CLOSE_CURSOR(cursor_name);
      rollback;
END P_READ_CLOB;
 
 
 
PROCEDURE P_WRITE_BLOB(s_sql varchar2,
                         n_len number,
                         s_data in raw) is
    lb_blob blob;
    n_cursor_name INTEGER;
    n_rows_processed INTEGER;
  begin
    n_cursor_name := dbms_sql.open_cursor;
    DBMS_SQL.PARSE(n_cursor_name,
                   s_sql,
                   dbms_sql.native);
    dbms_sql.define_column(n_cursor_name,
                           1,
                           lb_blob);
    n_rows_processed := dbms_sql.execute(n_cursor_name);
    if dbms_sql.fetch_rows(n_cursor_name) > 0 then
      dbms_sql.column_value(n_cursor_name,
                            1,
                            lb_blob);
    end if;
    dbms_lob.writeappend(lb_blob,
                         n_len,
                         s_data);
    DBMS_SQL.close_cursor(n_cursor_name);
end P_WRITE_BLOB;
 

PROCEDURE P_WRITE_CLOB(s_sql varchar2,
                         n_len number,
                         s_data varchar2) is
    lb_clob clob;
    n_cursor_name INTEGER;
    n_rows_processed INTEGER;
  begin
    n_cursor_name := dbms_sql.open_cursor;
    DBMS_SQL.PARSE(n_cursor_name,
                   s_sql,
                   dbms_sql.native);
    dbms_sql.define_column(n_cursor_name,
                           1,
                           lb_clob);
    n_rows_processed := dbms_sql.execute(n_cursor_name);
    if dbms_sql.fetch_rows(n_cursor_name) > 0 then
      dbms_sql.column_value(n_cursor_name,
                            1,
                            lb_clob);
    end if;
    dbms_lob.writeappend(lb_clob,
                         n_len,
                         s_data);
    DBMS_SQL.close_cursor(n_cursor_name);
END P_WRITE_CLOB;
