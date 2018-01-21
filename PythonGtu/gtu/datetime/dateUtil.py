import time
import datetime
import calendar

def currentDate():
    '''現在時間'''
    return time.strftime('%Y%m%d')

def currentDatetime():
    '''現在時間'''
    return time.strftime("%Y%m%d%H%M%S")

def unix_time_millis():
    '''時間long型態'''
    # return int(datetime.datetime.now().strftime("%s")) * 1000 
    return int(time.mktime(datetime.datetime.now().timetuple()) * 1000)

def dayOfWeek():
    '''禮拜幾'''
    return calendar.day_name[datetime.datetime.today().weekday()]

def dayOfWeek_fromDt(dt):
    '''dt = yyyy/MM/dd'''
    year, month, day = (int(x) for x in dt.split('/'))    
    ans = datetime.date(year, month, day)
    return ans.strftime("%A")
    
def dayDiff(yyyyMMdd):
    '''差異天數'''
    return (datetime.datetime.now() - \
    datetime.datetime.strptime(yyyyMMdd, "%Y%m%d")).days


if __name__ == '__main__' :
    print(currentDate())
    print(currentDatetime())
    print(unix_time_millis())
    print(dayOfWeek())
    print(dayOfWeek_fromDt('2017/09/10'))
    print(dayDiff("20170920"))





