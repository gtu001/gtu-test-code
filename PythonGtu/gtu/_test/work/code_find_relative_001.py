
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
    masterLineNumberLst = list()

    for i,v in enumerate(second_finds) :
        secondFindsMap[v] = None

    with open(file, "r", encoding=encoding, buffering=30) as fs :
        try :
            for idx, line in enumerate(fs) :
                fileContent[idx + 1] = line
        except UnicodeDecodeError as ex :
            print("檔案編碼失敗 : ", str(file))
            fileContent.clear()

    for i,lineNumber in enumerate(fileContent.keys()) :
        line = fileContent[lineNumber]
        line = getIgnoreCaseText(line, ignoreCase)
        if main_find_str in line :
            if len(second_finds) == 0 :
                masterLineNumberLst.append(lineNumber)
            else :
                for i,v in enumerate(second_finds) :
                    matchLineLst = checkFileDetail(fileContent, lineNumber, v, ignoreCase)
                    if len(matchLineLst) != 0 :
                        print("## findMatch : ", file, " --> lineNumber : ", matchLineLst)
                        secondFindsMap[v] = matchLineLst
                        masterLineNumberLst.append(lineNumber)
    return (masterLineNumberLst, secondFindsMap)



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
    if len(secondFindsMap) == 0 :
        return True
    allLst = []
    for i,key in enumerate(secondFindsMap.keys()) :
        if secondFindsMap[key] is not None and len(secondFindsMap[key]) != 0:
            allLst.append(True)
        else :
            allLst.append(False)
    if isAnd :
        if False in allLst :
            return False
        return True
    else :
        if True in allLst :
            return True
        return False


def main(dir_path, main_find_str, subFileName, second_finds, ignoreCase, encoding, isAnd) :
    filePattern = '.*'
    if not stringUtil.isBlank(subFileName) :
        filePattern = '.*\.' + subFileName
    print("#### filePattern : ", filePattern)

    fileLst = list()
    fileUtil.searchFileMatchs(dir_path, filePattern, fileLst, debug=False, ignoreSubFileNameLst=["jar", "class"])
    
    for i,f in enumerate(fileLst) :
        print("start ", str(f))
        (masterLineNumberLst, secondFindsMap) = checkFile(f, main_find_str, second_finds, ignoreCase, encoding)
        
        if len(masterLineNumberLst) == 0 :
            continue

        if checkSecondFindsMapCondition(secondFindsMap, isAnd) != 0:
            log.writeline(f, secondFindsMap)

            # 紀錄檔案該行
            writeFilesRelativeLines(f, masterLineNumberLst, secondFindsMap, log)
    pass


def writeFilesRelativeLines(file, masterLineNumberLst, secondFindsMap, log) :
    contentLst = fileUtil.loadFile_asList(file)
    if len(secondFindsMap) == 0 :
        for i, lineNumber in enumerate(masterLineNumberLst) :
            if lineNumber - 1 < len(contentLst):
                prefix = "M"
                log.writeline("\t" + prefix + "[" + str(lineNumber) + "]" + contentLst[lineNumber - 1])
    else : 
        for i, k in enumerate(secondFindsMap) :
            lineLst = secondFindsMap[k]
            for ii, lineNumber in enumerate(lineLst) :
                findLines = contentLst[lineNumber - 1 - 2 : lineNumber - 1 + 2]
                lineNumberArry = list(range(lineNumber - 2, lineNumber + 2))
                for iii,line in enumerate(findLines) :
                    prefix = " "
                    if lineNumberArry[iii] in masterLineNumberLst :
                        prefix = "M" #主
                    elif lineNumber == lineNumberArry[iii] :
                        prefix = "S" #次
                    log.writeline("\t" + prefix + "[" + str(lineNumberArry[iii]) + "]" + line)


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
    dir_path = "D:/work_tool/Z-Code"
    main_find_str = "Pay_Detail"
    subFileName = "xml"
    ignoreCase = True
    second_finds = [
        # findStr, relativeLineNumber, isRegex, ignoreCase
        SecondFindDef("insert", 3, False, ignoreCase),
    ]
    encoding = "UTF8"
    isAnd = True
    main(dir_path, main_find_str, subFileName, second_finds, ignoreCase, encoding, isAnd)
    print("done..")
