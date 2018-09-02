import pymysql
from pymysql.cursors import DictCursor


class MysqlTest ():
    def __init__(self):
        self.db = pymysql.Connect(host='localhost', \
                             user='sa', \
                             password='', \
                             db='test', \
                             charset='utf8', \
                             cursorclass=pymysql.cursors.DictCursor\
                             )

    def queryForList(self, sql):
        db = self.db
        db.autocommit(False)
        db.set_charset("UTF8")
        cursor = db.cursor()
        db.begin()
        lst = list()
        try:
            dataCount = cursor.execute(sql)
            print("總筆數", dataCount)
            while True:
                row = cursor.fetchone()
                if row == None:
                    break
                lst.append(row)
            db.commit()
        except Exception as ex :
            db.rollback()
            print(ex)
        finally:
            db.close()
            return lst
    
    

if __name__ == '__main__':
    t = MysqlTest()
    lst = t.queryForList("select * from  user_info")
    for i,row in enumerate(lst, 0):
        print(i, row)