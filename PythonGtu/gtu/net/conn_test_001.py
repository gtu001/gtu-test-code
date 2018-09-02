import io
import sys

from gtu.decode import decodeUtil
import http.client as httplib
from gtu.io import fileUtil


def getUrlData():
    # http://www.twse.com.tw/exchangeReport/MI_INDEX?response=csv&date=20171003&type=ALLBUT0999
    httpServ = httplib.HTTPConnection("www.twse.com.tw", 80, timeout=10)
    httpServ.connect()
    httpServ.request('GET', "/exchangeReport/MI_INDEX?response=csv&date=20171003&type=ALLBUT0999")
    
    response = httpServ.getresponse()
    if response.status == httplib.OK:
        print("Output from HTML request")
        rtnVal = decodeUtil.decodeChs(response.read(), 0)
    
    httpServ.close()
    return rtnVal

def removeEmptyLine(strVal):
    rtnVal = ""
    f = io.StringIO(strVal)
    for i, line in enumerate(f, 1):
        rtnVal += removeChangeLine(line)
    f.close()
    return rtnVal

def removeChangeLine(strVal):
    '''\r\n同時出現時, 會被當成空兩行, 故只保留\n'''
    while strVal.endswith(('\n', '\r')):
        strVal = strVal[0: -1]
    return strVal + "\n"

def writeCsv(strVal):
    f = open(fileUtil.getDesktopDir() + "/test.csv", 'w', encoding=decodeUtil.getChsCodeType(0))
    f.write(strVal)
    f.close()

def __main__():
    rtnStr = getUrlData()
    rtnStr = removeEmptyLine(rtnStr)
    
    writeCsv(rtnStr)
    
    f = io.StringIO(rtnStr)
    for i, line in enumerate(f, 1):
        print("line--", i, line)
    f.close()
        
__main__()

