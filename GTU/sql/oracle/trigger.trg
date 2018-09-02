--UTF8 文件
CREATE OR REPLACE TRIGGER TRI_NOCLAIM_DISCNT_ALLOCATE__BIU_AUD
  BEFORE INSERT OR UPDATE ON T_NOCLAIM_DISCNT_ALLOCATE
  FOR EACH ROW
DECLARE
  --Variable declaration
  m_curr_date DATE;
  m_opt_id NUMBER;
BEGIN
  --Get the user id.
  --This will provide user id based on request.If the request is from UI,it will provide
  --end user id else gives database access user id.
  m_opt_id := pkg_pub_app_context.f_get_current_user;
  --Get the system date.
  m_curr_date := pkg_pub_app_context.F_GET_USER_LOCAL_TIME;
  --Based on the operation, insert the fields either INSERTED_BY or  UPDATED_BY
  IF inserting then
    :new.inserted_by := m_opt_id;
    :new.INSERT_TIME := m_curr_date;
    :new.insert_timestamp := sysdate;
  END IF;

  if updating or inserting then
    :new.updated_by := m_opt_id;
    :new.UPDATE_TIME := m_curr_date;
    :new.update_timestamp := sysdate;
  END IF;
END;
/
