create or replace package PKG_For_test is

  ILP_REN_OPT_ONLY_MAIN INT := 1; 
  PAY_MODE__CASH constant int := 1;

  TYPE type_contract_master IS TABLE OF t_contract_master%rowtype INDEX BY PLS_INTEGER;
  type split_tbl is table of varchar2(32767);
  type array_varchar is table of varchar2(20) index by varchar2(20);

  type Round_Param is Record(
    param_name  varchar2(100),
    param_value varchar2(1000));

  type table_Round_param is table of Round_Param;

  git_contract_master type_contract_master;

  function test001_basefor(data1 in varchar2) return varchar2;
  function test002_forloop(data2 in varchar2) return varchar2;
  procedure test003_array;
  procedure test003_array2;
  procedure test004_pipe;
  procedure test005_pipe;
  procedure test006_bulkCollect;
  procedure test007_bulkCollect;
  procedure test008_loop;
  procedure test009_loop;
  procedure test010_exception;
  procedure test011_cursor;
  procedure test012_executeImmediate;
  procedure test013_makeSql;
  procedure test014_savepoint;
  procedure test015_record;
  procedure test016_makeSql;
  procedure test017_goto;
  procedure test018_booleanToInteger;
  procedure test019_connectBy;
  procedure test020_showErrorStack;
  procedure test021_showStack;

  procedure test_bulk_collect;
  procedure test_bulk_collect2;

  procedure test_exception;

  procedure test_for_traceBug;

  function split(i_list in varchar2, i_delimiter in varchar2 := '-')
    return split_tbl
    pipelined;

  function to_pipe2(data varchar2) return table_Round_param
    pipelined;
  function to_pipe return table_Round_param
    pipelined;

  procedure p_add_message(i_str_data in varchar2, o_str_id out varchar2);

  procedure p_msg(i_msg in varchar2);

  function current_time_ms return number;

  
  FUNCTION F_LENGTHB(I_STR VARCHAR2) RETURN NUMBER;
  
  function f_pad_str(i_str      varchar2,
                     i_len      number,
                     i_pad_char char,
                     i_LorR     char) return varchar2;

  function f_use_indicate_date(i_policy_id number,
                               i_item_id   number,
                               i_due_date  date)
    return t_noclaim_discnt_allocate%rowtype;

  function f_use_indicate_date2(i_policy_id number,
                                i_item_id   number,
                                i_due_date  date)
    return t_noclaim_discnt_allocate%rowtype;
    
  function f_get_tw_date_to_oracle_date(i_date_str varchar2) return date;

end PKG_For_test;
/
create or replace package body PKG_For_test is

  function test001_basefor(data1 in varchar2) return varchar2 is
    aa varchar2(10);
    ii integer;
  begin
    for i in 1 .. 10 loop
      dbms_output.put_line('a1 = ' || i);
    end loop;
    for i in reverse 1 .. 10 loop
      dbms_output.put_line('a2 = ' || i);
    end loop;
  
    ii := 0;
    loop
      ii := ii + 3;
      dbms_output.put_line('a3 = ' || ii);
      exit when ii > 30;
    end loop;
  
    while ii > 0 loop
      ii := ii - 4;
      dbms_output.put_line('a4 = ' || ii);
      if ii <= 0 then
        exit;
      end if;
    end loop;
  
    return aa;
  end;

  function test002_forloop(data2 in varchar2) return varchar2 is
    cursor cc is
      select * from t_string_resource;
  begin
    for emp in cc loop
      dbms_output.put_line('test -- ' || emp.str_id || ' ' || emp.str_data);
    end loop;
    return data2;
  end;

  procedure test003_array as
    cursor c_contract_master is
      select * from t_contract_master;
    ii      integer := 0;
    counter pls_integer := null;
  begin
    for v_master in c_contract_master loop
      ii := ii + 1;
      --dbms_output.put_line('------' || v_master.policy_id);
      git_contract_master(v_master.policy_id) := v_master;
      exit when ii > 10;
    end loop;
    dbms_output.put_line('first --- ' || git_contract_master(git_contract_master.first)
                         .policy_id);
    dbms_output.put_line('last --- ' || git_contract_master(git_contract_master.last)
                         .policy_id);
  
    counter := git_contract_master.first;
    while counter is not null loop
      dbms_output.put_line('index --- ' || counter);
      counter := git_contract_master.next(counter);
    end loop;
  end;
  
  procedure test003_array2 as
    TYPE v_array_type IS TABLE OF varchar2(10) INDEX BY PLS_INTEGER;
    v_array v_array_type;
  begin
    
    v_array(1) := 'A';
    dbms_output.put_line(case when v_array.exists(1) then 'Y' else 'N' end);
    v_array.delete(1);
    dbms_output.put_line(case when v_array.exists(1) then 'Y' else 'N' end);
    
    v_array(1) := 'A';
    v_array(2) := 'B';
    v_array(3) := 'C';
    dbms_output.put_line(v_array.count);
    
    v_array.delete;
    dbms_output.put_line(v_array.count);
  end;

  function split(i_list in varchar2, i_delimiter in varchar2 := '-')
    return split_tbl
    pipelined is
    m_idx   pls_integer;
    m_list  varchar2(32767) := i_list;
    m_value varchar2(32767);
  begin
    loop
      m_idx := instr(m_list, i_delimiter);
      if m_idx > 0 then
        pipe row(substr(m_list, 1, m_idx - 1));
        m_list := substr(m_list, m_idx + length(i_delimiter));
      else
        pipe row(m_list);
        exit;
      end if;
    end loop;
  end split;

  function to_pipe2(data varchar2) return table_Round_param
    pipelined as
    arry array_varchar;
    dd   Round_Param;
    key  varchar2(20);
    val  varchar2(20);
  begin
    arry('aa1') := 'AA2';
    arry('bb1') := 'BB2';
    arry('cc1') := 'CC2';
  
    key := arry.first;
    while key is not null loop
      val            := arry(key);
      dd.param_name  := key;
      dd.param_value := val;
      --dbms_output.put_line('...'||key||'-'||val);
      pipe row(dd);
      key := arry.next(key);
    end loop;
  end to_pipe2;

  function to_pipe return table_Round_param
    pipelined as
    dd Round_Param;
  begin
    dd.param_name  := 'aa';
    dd.param_value := 'AA';
    pipe row(dd);
    dd.param_name  := 'bb';
    dd.param_value := 'BB';
    pipe row(dd);
  end to_pipe;

  procedure test004_pipe as
    text varchar2(1000);
  begin
    text := 'aaa-bbb-ccc-ddd-eee';
    for cr in (select column_value from table(split(text))) loop
      dbms_output.put_line('...' || cr.column_value);
    end loop;
  end;

  procedure test005_pipe as
    arry array_varchar;
    trp  table_Round_param;
  begin
    arry('aa') := 'AA';
    arry('bb') := 'BB';
    arry('cc') := 'CC';
  
    for cr in (select * from table(to_pipe2('aaa'))) loop
      dbms_output.put_line('...' || cr.param_name || '-' || cr.param_value);
    end loop;
  end;

  procedure test006_bulkCollect as
    type array_list_id is table of number;
    v_list_id array_list_id;
  begin
    select list_id bulk collect
      into v_list_id
      from t_prem_arap
     where rownum < 10;
  
    for i in 1 .. v_list_id.count loop
      dbms_output.put_line(v_list_id(i));
    end loop;
  end;

  procedure test007_bulkCollect as
    type data_inf is record(
      data1 number,
      data2 number);
    type data_inf_array is table of data_inf;
    list data_inf_array;
    cursor c_prem_arap is
      select t.list_id, t.branch_id from t_prem_arap t where rownum < 20;
  begin
    open c_prem_arap;
  
    if c_prem_arap%notfound then
      close c_prem_arap;
      return;
    end if;
  
    fetch c_prem_arap bulk collect
      into list;
    close c_prem_arap;
  
    for i in 1 .. list.count loop
      dbms_output.put_line(list(i).data1 || '..' || list(i).data2);
    end loop;
  end;

  procedure test008_loop as
    data1 number;
    data2 number;
    cursor c_prem_arap is
      select t.list_id, t.branch_id from t_prem_arap t where rownum < 20;
  begin
    open c_prem_arap;
  
    if c_prem_arap%notfound then
      close c_prem_arap;
      return;
    end if;
  
    loop
      fetch c_prem_arap
        into data1, data2;
      exit when c_prem_arap%notfound;
      dbms_output.put_line(data1 || '..' || data2);
    end loop;
    close c_prem_arap;
  end;

  procedure test009_loop as
    data1 number;
    data2 number;
    cursor c_prem_arap is
      select t.list_id, t.branch_id from t_prem_arap t where rownum < 20;
  begin
    open c_prem_arap;
    fetch c_prem_arap
      into data1, data2;
    if c_prem_arap%notfound then
      dbms_output.put_line('end');
      close c_prem_arap;
    end if;
    dbms_output.put_line('a' || data1 || '..' || data2);
  
    loop
      fetch c_prem_arap
        into data1, data2;
      exit when c_prem_arap%notfound;
      dbms_output.put_line('b' || data1 || '..' || data2);
    end loop;
  end;

  procedure test010_exception as
    v_policy_id number := 0;
  begin
    begin
      select policy_id
        into v_policy_id
        from t_contract_master
       where 1 != 1;
    exception
      when others then
        v_policy_id := -1;
    end;
    begin
      select policy_id
        into v_policy_id
        from t_contract_master
       where 1 != 1;
    exception
      when no_data_found then
        v_policy_id := -1;
    end;
    dbms_output.put_line('has_error : ' || v_policy_id);
  end;

  procedure test_exception as
  begin
    raise_application_error(-20100, 'test error occur!');
  end;

  procedure p_add_message(i_str_data in varchar2, o_str_id out varchar2) as
    v_count number;
  begin
    select 'MSG_' || s_string_resource__msg_id.nextval
      into o_str_id
      from dual;
  
    insert into t_string_resource
      (str_id, lang_id, str_data, org_table, update_time)
    values
      (o_str_id, 311, i_str_data, 'T_MESSAGE', sysdate);
  
    dbms_output.put_line('id = ' || o_str_id);
  end;

  procedure test_bulk_collect as
    type ARRAY_LIST_ID is TABLE of number;
    M_LIST_ID ARRAY_LIST_ID;
    M_ITEM_ID ARRAY_LIST_ID;
  begin
    select tpa.list_id, tpa.item_id bulk collect
      into M_LIST_ID, M_ITEM_ID
      from t_prem_arap tpa
     where rownum < 50;
  
    if M_ITEM_ID.count > 0 then
      for i in 1 .. M_ITEM_ID.count loop
        dbms_output.put_line('id = ' || M_ITEM_ID(i) || ',' ||
                             M_LIST_ID(i));
      end loop;
    end if;
  end;
  
  
  procedure test_bulk_collect2 as
    type prem_info is record(
      list_id t_prem_arap.list_id%type,
      fee_type t_prem_arap.fee_type%type,
      product_id t_prem_arap.product_id%type);

    type prem_info_t is table of prem_info;
    prem_list prem_info_t;
    
    cursor c_prem_list is
      select list_id,
             fee_type,                   
             product_id
        from t_prem_arap a
       where rownum < 20;
  begin
    open c_prem_list;
    fetch c_prem_list bulk collect into prem_list;
    close c_prem_list;
  
    if prem_list.count > 0 then
      for i in 1 .. prem_list.count loop
        dbms_output.put_line(prem_list(i).list_id || ',' || prem_list(i).fee_type || ',' || prem_list(i).product_id);
      end loop;
    end if;
  end;
  

  procedure test011_cursor as
    cursor c_product is
      select item_id from t_contract_product t where rownum < 10;
    v_item_id number;
  begin
    open c_product;
    loop
      fetch c_product
        into v_item_id;
      exit when c_product%notfound;
      dbms_output.put_line('item_id : ' || v_item_id);
    end loop;
    close c_product;
  end;

  procedure test012_executeImmediate as
    v_result varchar(100);
  begin
    EXECUTE IMMEDIATE 'BEGIN :1:= ''aa'' ; END;'
      USING OUT v_result;
    --dbms_output.put_line(v_result);
    EXECUTE IMMEDIATE 'BEGIN dbms_output.put_line(''' || v_result ||
                      '''); END;';
  end;

  procedure test013_makeSql as
    v_sql varchar(500);
    type r_ids is ref cursor;
    c_arap  r_ids;
    v_data1 varchar(100);
    v_data2 varchar(100);
  begin
    v_sql := ' select list_id, fee_amount  from t_prem_arap where rownum < ''10'' ';
    open c_arap for v_sql;
  
    if c_arap%notfound then
      close c_arap;
      return;
    end if;
  
    loop
      fetch c_arap
        into v_data1, v_data2;
      exit when c_arap%notfound;
      dbms_output.put_line('data = ' || v_data1 || ',' || v_data2);
    end loop;
    close c_arap;
  end;

  procedure test014_savepoint as
  begin
    savepoint regular_confirm_point;
    rollback to regular_confirm_point;
  end;

  procedure test015_record as
    TYPE T1 IS RECORD(
      p_branch  VARCHAR2(20),
      p_account VARCHAR2(30));
    TYPE t2 IS VARRAY(20) OF t1;
    t3 t2 := t2(); --- initialize to non-null, empty array
    t  NUMBER := 1;
  BEGIN
    FOR j IN (SELECT * FROM t_contract_master t WHERE ROWNUM <= 10) LOOP
      DBMS_OUTPUT.PUT_LINE('YUDHI # ' || J.Policy_Id);
      t3.extend(); --- add element to end of array
      t3(t).p_branch := j.Policy_Id;
      t := t + 1; --- presumably you meant to increment your counter?
    END LOOP;
  END;

  procedure test016_makeSql as
    v_cusror_sql varchar2(200);
    type ty_policy_cursor is ref cursor;
    policy_cursor   ty_policy_cursor;
    v_cusror_param1 number;
    v_cusror_param2 number;
    v_item_id       number;
    v_product_id    number;
  begin
    v_cusror_sql    := 'select item_id, product_id ' ||
                       ' from t_contract_product ' ||
                       ' where policy_id = :i_policy_id ' ||
                       ' and item_id = :i_item_id ';
    v_cusror_param1 := 200;
    v_cusror_param2 := 200;
    open policy_cursor for v_cusror_sql
      using v_cusror_param1, v_cusror_param2;
  
    if policy_cursor%notfound then
      close policy_cursor;
      return;
    end if;
  
    loop
      fetch policy_cursor
        into v_item_id, v_product_id;
      exit when policy_cursor%notfound;
      dbms_output.put_line('item_id : ' || v_item_id || ',' ||
                           v_product_id);
    end loop;
    close policy_cursor;
  end;

  procedure test017_goto is
  begin
    dbms_output.put_line('aaa');
    dbms_output.put_line('aaa1');
    dbms_output.put_line('aaa2');
  
    goto testLabel;
    dbms_output.put_line('aaa3');
  
    goto testLabel2;
    dbms_output.put_line('aaa4');
  
    <<testLabel>>
    dbms_output.put_line('aaa5');
    dbms_output.put_line('aaa6');
  
    <<testLabel2>>
    dbms_output.put_line('aaa7');
    dbms_output.put_line('aaa8');
  end;

  procedure test018_booleanToInteger is
  begin
    dbms_output.put_line('true = ' || sys.diutil.bool_to_int(true));
    dbms_output.put_line('false = ' || sys.diutil.bool_to_int(false));
  end;

  procedure p_msg(i_msg in varchar2) is
  begin
    dbms_output.put_line(i_msg);
    pkg_pub_scd_ci.p_log_info(i_msg);
  end;

  procedure test019_connectBy is
    V_ORGAN_ID number := 10101;
    type ARRAY_ID is TABLE of varchar2(40);
    M_PARENT_ORGAN ARRAY_ID;
    V_MATCH_COUNT  number;
  begin
    select a.organ_id BULK COLLECT
      into M_PARENT_ORGAN
      from t_org a
     start with a.organ_id = V_ORGAN_ID
    connect by a.organ_id = prior a.parent_id;
    for i in 1 .. M_PARENT_ORGAN.count loop
      dbms_output.put_line('[' || i || ']=' || M_PARENT_ORGAN(i));
      if V_MATCH_COUNT > 0 then
        exit;
      end if;
    end loop;
  end;

  function current_time_ms return number is
    out_result number;
  begin
    select extract(day from(sys_extract_utc(systimestamp) -
                        to_timestamp('1970-01-01', 'YYYY-MM-DD'))) *
           86400000 +
           to_number(to_char(sys_extract_utc(systimestamp), 'SSSSSFF3'))
      into out_result
      from dual;
    return out_result;
  end current_time_ms;

  procedure test_for_traceBug is
    cursor c_cur is(
      select distinct policy_id
        from t_batch_multi_streaming
       where TRANS_CODE = 'RENEW_EXTRACTION');
  begin
    pkg_pub_app_context.P_SET_APP_USER_ID(401);
    dbms_output.enable(100000000);
    for c_data in c_cur loop
      dbms_output.put_line(c_data.policy_id);
      begin
        pkg_ls_pm_subprem_extraction.p_renew_extract_r(c_data.policy_id);
      exception
        when others then
          dbms_output.put_line(c_data.policy_id || ' -- ' || sqlerrm);
      end;
    end loop;
  end;

  procedure test020_showErrorStack as
  begin
    raise_application_error(-20100, 'xxxxxxxxxxxxxxxx');
  exception
    when others then
      rollback;
      dbms_output.put_line('errmsg:' || sqlerrm || ' , stack : ' ||
                           dbms_utility.format_error_backtrace);
  end;

  procedure test021_showStack as
    v_str varchar2(1000);
  begin
    DBMS_DEBUG.PRINT_BACKTRACE(v_str);
    dbms_output.put_line('stack1 : ' || DBMS_UTILITY.FORMAT_CALL_STACK);
    dbms_output.put_line('stack2 : ' || v_str);
  end;

  
  FUNCTION F_LENGTHB(I_STR VARCHAR2) RETURN NUMBER IS
    M_OFFSET  NUMBER := 1;
    M_TMP_STR VARCHAR2(500);
    M_LENGTH  NUMBER := 0;
  BEGIN
    IF I_STR IS NULL THEN
      RETURN 0;
    END IF;
    LOOP
      EXIT WHEN M_OFFSET > LENGTH(I_STR);
      M_TMP_STR := SUBSTR(I_STR, M_OFFSET, 1);
      M_OFFSET  := M_OFFSET + 1;
      IF LENGTHB(M_TMP_STR) > 1 THEN
        M_LENGTH := M_LENGTH + 1;
      END IF;
      M_LENGTH := M_LENGTH + 1;
    END LOOP;
    RETURN M_LENGTH;
  END F_LENGTHB;

  
  function f_pad_str(i_str      varchar2,
                     i_len      number,
                     i_pad_char char,
                     i_LorR     char) return varchar2 as
    v_rtn_str varchar2(4000);
    v_str     varchar2(2000);
    v_count   number;
  begin
    v_str := i_str;
    if v_str is null then
      v_str   := '';
      v_count := i_len;
    else
      v_count := i_len - F_LENGTHB(v_str);
    end if;
    for i in 1 .. v_count loop
      v_rtn_str := v_rtn_str || i_pad_char;
    end loop;
    if i_LorR in ('l', 'L') then
      v_rtn_str := v_rtn_str || v_str;
    elsif i_LorR in ('r', 'R') then
      v_rtn_str := v_str || v_rtn_str;
    end if;
    return v_rtn_str;
  end;

  function f_use_indicate_date(i_policy_id number,
                               i_item_id   number,
                               i_due_date  date)
    return t_noclaim_discnt_allocate%rowtype as
    v_due_date                date := i_due_date;
    v_noclaim_discnt_allocate t_noclaim_discnt_allocate%rowtype;
  
    cursor c_cur(cur_due_date date) is
      select *
        from t_noclaim_discnt_allocate t
       where item_id = i_item_id
         and t.allocate_date <= cur_due_date
         and add_months(t.allocate_date, 12) > cur_due_date;
  begin
    if i_due_date is not null then
      v_due_date := i_due_date;
    else
      pkg_ls_pub_collection.p_get_git_contract_extend(i_item_id);
      v_due_date := pkg_ls_pub_collection.git_contract_extend(i_item_id)
                    .due_date;
    end if;
  
    for c_data in c_cur(v_due_date) loop
      v_noclaim_discnt_allocate := c_data;
    end loop;
    return v_noclaim_discnt_allocate;
  end;

  function f_use_indicate_date2(i_policy_id number,
                                i_item_id   number,
                                i_due_date  date)
    return t_noclaim_discnt_allocate%rowtype as
    v_due_date                date := i_due_date;
    v_noclaim_discnt_allocate t_noclaim_discnt_allocate%rowtype;
  
    cursor c_cur(cur_due_date date) is
      select *
        from t_noclaim_discnt_allocate t
       where item_id = i_item_id
         and t.allocate_date <= cur_due_date
         and add_months(t.allocate_date, 12) > cur_due_date;
  begin
    if i_due_date is not null then
      v_due_date := i_due_date;
    else
      pkg_ls_pub_collection.p_get_git_contract_extend(i_item_id);
      v_due_date := pkg_ls_pub_collection.git_contract_extend(i_item_id)
                    .due_date;
    end if;
  
    open c_cur(v_due_date);
    loop
      fetch c_cur
        into v_noclaim_discnt_allocate;
      exit when c_cur%notfound;
    end loop;
    return v_noclaim_discnt_allocate;
  end;

  function f_get_tw_date_to_oracle_date(i_date_str varchar2) return date as 
    v_yyy  varchar2(4);
    v_mm  varchar2(2);
    v_dd  varchar2(2);
    v_type varchar(1);
    v_rtn_date date;
  begin
    if i_date_str is null or trim(i_date_str) = '' then 
      return v_rtn_date;
    end if;
    
    select
      DECODE(replace(translate(i_date_str,'1234567890','##########'),'#'),NULL, 'T', 'F')
      into v_type
    from dual;
  
    if v_type = 'F' then
      return v_rtn_date;
    end if;
  
    v_yyy := to_char(to_number(substr(i_date_str, 1, 3)) + 1911);
    v_mm := substr(i_date_str, 4, 2);
    v_dd := substr(i_date_str, 6, 2);
    v_rtn_date := to_date(v_yyy || v_mm || v_dd, 'yyyymmdd');
    return v_rtn_date;
  exception
        when others then
          dbms_output.put_line('[' || i_date_str || '] -- ' || sqlerrm);
          --raise_application_error(-20100, '[' || i_date_str || '] -- ' || sqlerrm);
          return v_rtn_date;
  end;  
end PKG_For_test;
/
