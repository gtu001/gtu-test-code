import re

from gtu.number import numberUtil
from gtu.regex import regexReplace

'''
from gtu.string import stringUtil
'''

def getChinese(strVal):
    matchList = re.finditer("[\u4e00-\u9fa5]", strVal)
    rtnStr = ""
    for mth in matchList:
        rtnStr += mth.group()
    return rtnStr


def isChinese(strVal):
    prog = re.compile("^[\u4e00-\u9fa5]+$", re.DOTALL | re.MULTILINE)
    result = prog.match(strVal)
    if result == None:
        return False
    return True;


def hasChinese(strVal):
    prog = re.compile("[\u4e00-\u9fa5]+", re.DOTALL | re.MULTILINE)
    result = prog.search(strVal)
    if result == None:
        return False
    return True;


def replace(line, ptn, to):
    #line = re.sub(r"</?\[\d+>", "", line)
    line = re.sub(ptn, to, line)
    return line


def trimSpace(chs):
    chs = chs.replace('　', '').replace(' ', '').strip()
    chs = re.sub(r"\\s", "", chs)
    return chs


def trimToEmpty(chs):
    if chs is None :
        return ""
    elif type(chs) == str :
        return chs.strip()
    else :
        return str(chs).strip()
    


def isBlank(chs):
    if chs == None :
        return True
    chs2 = chs.strip()
    return len(chs2) == 0


def isNotBlank(chs):
    return not isBlank(chs) 


def leftPad(strVal, length, padChar):
    return strVal.rjust(length, padChar)


def rightPad(strVal, length, padChar):
    return strVal.ljust(length, padChar)


def isNumber(s):
    return numberUtil.isNumber(s)


def concat(*arry):
    strValue = ""
    for (i, obj) in enumerate(arry) :
        if type(obj).__name__ == 'str':
            strValue += obj
        else:
            strValue += str(obj)
    return strValue


def isNoneType(s):
    return s is None


def javaParameterToDbColumn(strVal, delimit="_") :
    strVal = strVal.strip()
    length = len(strVal)
    sb = ""
    for i in range(0, length) :
        if strVal[i].islower() and i + 1 < length and strVal[i + 1].isupper() :
            sb += strVal[i].lower() + delimit
        else:
            sb += strVal[i].lower()
    return sb


def dbColumnToJavaParameter(strVal, delimit="_") :
    strVal = strVal.strip()
    length = len(strVal)
    sb = ""
    toUpperCase = False
    for i in range(0, length) :
        if strVal[i] == delimit :
            toUpperCase = True
            continue
        if not toUpperCase :
            sb += strVal[i].lower()
        else :
            sb += strVal[i].upper()
            toUpperCase = False
    return sb


if __name__ == '__main__' :
    strVal = "aaa_bbb_cccc_dddd"
    result = dbColumnToJavaParameter(strVal)
    print(result)
    print("done..")
    