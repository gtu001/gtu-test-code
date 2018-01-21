import pymysql
from sqlalchemy.engine import create_engine
from sqlalchemy.sql.schema import MetaData, Table, Column
from sqlalchemy.sql.sqltypes import Integer, String, DateTime

from gtu.error import errorHandler
from gtu.reflect import checkSelf


if __name__ == '__main__' :
    engine = create_engine('mysql+pymysql://localhost:3306/test?charset=utf8mb4', pool_recycle=3600)
    
    conn = engine.connect()
    conn = conn.execution_options(
#         isolation_level="READ COMMITTED",
        isolation_level="READ_UNCOMMITTED",
        schema_translate_map={None: ""},
        connect_args={"encoding": "utf8"},
    )
    
    meta = MetaData()
    users_table = Table('user_info', meta,
        Column('id', Integer, primary_key=True),
        Column('create_date', DateTime),
        Column('create_user', String(50)),
        Column('email', String(50)),
        Column('last_login_date', Integer),
        Column('login_time_count', String(50)),
        Column('password', String(50)),
        Column('user_id', String(50)),
        Column('user_name', String(50)),
    )
    
    result = conn.execute("select * from user_info")
    print(result._metadata)
    
    for i, row in enumerate(result, 0):
#         checkSelf.checkMembers(row)
        for k, v in row.items() :
            print(k, v)        
            
    try:
        trans = conn.begin()
        conn.execute(users_table.update() \
                     .where(users_table.c.id == 99 and users_table.c.id == 99) \
                     .values({"id":99,
                              "create_date":"20171111121212",
                              "create_user":"python",
                              "email":"python@test.com",
                              "last_login_date":"20171111",
                              "login_time_count":1,
                              "password":"1234",
                              "user_id":"python_test",
                              "user_name":"python_test",
                              })) 
        trans.commit()
    except Exception as ex :
        errorHandler.printStackTrace()
        trans.rollback()
    
    print("done...")
