import time
import datetime
import calendar
from gtu.error import errorHandler

'''
from gtu.datetime import dateUtil
'''


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


def getForamtFromJava(formatStr):
    '''取得format用java版'''
    map = dict()
    
    map[ 'yyyy' ] = "%Y"
    map[ 'MM' ] = "%m"
    map[ 'dd' ] = "%d"
    map[ 'HH' ] = "%H"
    map[ 'mm' ] = "%M"
    map[ 'ss' ] = "%S"
    map[ 'SSSSSS' ] = "%f"
    
    repFormat = formatStr
    for key in map :
        repFormat = repFormat.replace(key, map[key])

    return repFormat



def getDatetime(dateStr, format):
    return datetime.datetime.strptime(dateStr, format)



def getDatetime_debug(dateStr, format, debugLabel):
    try:
        return datetime.datetime.strptime(dateStr, format)
    except:
        errorHandler.printStackTrace()
        raise AttributeError(debugLabel + " -> 無法處理 : " + dateStr + " -> " + format)



def getDatetimeByJavaFormat(dateStr, javaFormat, debugLabel=None):
    pyFormat = getForamtFromJava(javaFormat)
    if not debugLabel :
        return getDatetime(dateStr, pyFormat)
    else :
        return getDatetime_debug(dateStr, pyFormat, debugLabel)



def formatDatetime(datetimeObj, format):
    return datetimeObj.strftime(format)


def formatDatetimeByJavaFormat(datetimeObj, javaFormat):
    format = getForamtFromJava(javaFormat)
    return datetimeObj.strftime(format)

if __name__ == '__main__' :
    dateobj = getDatetimeByJavaFormat("2018/08/30 15:18:30.555555", "yyyy/MM/dd HH:mm:ss.SSSSSS")
    print("dateobj", dateobj)
    print("done...")

