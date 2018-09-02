import pymysql
from pymysql.cursors import DictCursor


db = pymysql.Connect(host='localhost', \
                             user='sa', \
                             password='', \
                             db='test', \
                             charset='utf8', \
                             cursorclass=pymysql.cursors.DictCursor\
                             )

db.autocommit(False)
db.set_charset("UTF8")

cursor = db.cursor()

db.begin()
try:
    dataCount = cursor.execute("select * from user_info ")
    
    print("總筆數", dataCount)
    
    while True:
        row = cursor.fetchone()
        if row == None:
            break
        for i, key in enumerate(row):
            print(i, key, " = ", row[key])   
        print() 
        
    db.commit()
except Exception as ex :
    db.rollback()
    print(ex)
finally:
    db.close()
    
