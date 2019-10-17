
from gtu.reflect import checkSelf

from threading import Thread

from gtu.util import queue_test_001
from gtu.string import stringUtil

import sys
import time
import re
from enum import Enum
from gtu.io import fileUtil
from gtu.error import errorHandler
from abc import ABCMeta, abstractmethod

from gtu.io import LogWriter

log = LogWriter.LogWriter()


def getIgnoreCaseText(strVal, ignoreCase) :
    if ignoreCase :
        strVal = strVal.lower()
    return strVal


def checkFile(file, main_find_str, second_finds, ignoreCase, encoding) :
    main_find_str = getIgnoreCaseText(main_find_str, ignoreCase)
    main_find_str = main_find_str.strip()

    fileContent = dict()
    secondFindsMap = dict()

    for i,v in enumerate(second_finds) :
        secondFindsMap[v] = None

    with open(file, "r", encoding=encoding, buffering=30) as fs :
        try :
            for num, line in enumerate(fs) :
                fileContent[num] = line
        except UnicodeDecodeError as ex :
            print("檔案編碼失敗 : ", str(file))

    for i,num in enumerate(fileContent.keys()) :
        line = fileContent[num]
        line = getIgnoreCaseText(line, ignoreCase)
        if main_find_str in line :
            for i,v in enumerate(second_finds) :
                matchLineLst = checkFileDetail(fileContent, num, v, ignoreCase)
                if len(matchLineLst) != 0 :
                    secondFindsMap[v] = matchLineLst
    return secondFindsMap



def checkFileDetail(fileContent, lineNumber, mSecondFindDef, ignoreCase) :
    matchLineLst = list()
    for num in range(lineNumber - mSecondFindDef.relativeLineNumber, lineNumber + mSecondFindDef.relativeLineNumber) :
        if num in fileContent :
            line = fileContent[num]
            line = getIgnoreCaseText(line, ignoreCase)
            
            if not mSecondFindDef.isRegex :
                findStr = getIgnoreCaseText(mSecondFindDef.findStr, ignoreCase)
                if findStr in line :
                    matchLineLst.append(num)
            else :
                mth = mSecondFindDef.findPtn.search(line)
                if mth :
                    matchLineLst.append(num)
    return matchLineLst



def checkSecondFindsMapCondition(secondFindsMap, isAnd) :
    if isAnd :
        for i,key in enumerate(secondFindsMap.keys()) :
            if secondFindsMap[key] is not None and len(secondFindsMap[key]) == 0:
                return False
        return True
    else :
        for i,key in enumerate(secondFindsMap.keys()) :
            if secondFindsMap[key] is not None and len(secondFindsMap[key]) != 0:
                return True
        return False


def main(dir_path, main_find_str, second_finds, ignoreCase, encoding, isAnd) :
    fileLst = list()
    fileUtil.searchFileMatchs(dir_path, r'.*', fileLst, debug=False, ignoreSubFileNameLst=["jar", "class"])
    for i,f in enumerate(fileLst) :
        print("start ", str(f))
        secondFindsMap = checkFile(f, main_find_str, second_finds, ignoreCase, encoding)
        if checkSecondFindsMapCondition(secondFindsMap, isAnd) != 0:
            log.writeline(f, secondFindsMap)
    pass


class SecondFindDef () :
    def __init__(self, findStr, relativeLineNumber, isRegex, ignoreCase) :
        self.findStr = findStr
        self.isRegex = isRegex
        self.relativeLineNumber = relativeLineNumber
        if isRegex :
            flags = 0
            if ignoreCase :
                flags |= re.I
            self.findPtn = re.compile(findStr, flags)

    def __str__(self) :
        return "(strFind:" + self.findStr + ")"

    def __repr__(self) :
        return "(reprFind:" + self.findStr + ")"



if __name__ == '__main__' :
    dir_path = "D:/workspace/inv-Fund-web-Query"
    main_find_str = "productType"
    ignoreCase = True
    second_finds = [
        SecondFindDef("[\"\']0[\"\']", 3, True, ignoreCase),
        SecondFindDef("check", 3, False, ignoreCase),
    ]
    encoding = "UTF8"
    isAnd = True
    main(dir_path, main_find_str, second_finds, ignoreCase, encoding, isAnd)
    print("done..")