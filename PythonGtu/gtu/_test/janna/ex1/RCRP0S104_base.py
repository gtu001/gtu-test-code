from enum import Enum
import re

from gtu.enum import enumUtil
from gtu.io import fileUtil
from gtu.reflect import checkSelf, toStringUtil
from gtu.regex import regexUtil
from gtu.string import stringUtil
from gtu.enum.enumUtil import EnumHelper
import openpyxl
import pandas as pd
from gtu.error import errorHandler

import numpy as np 
from gtu.data_science.numpy import numpyUtil
from gtu.collection import listUtil

ERROR_FILE = list()

def isDataLine(line):
    #數字長度大於四
    ptn = re.compile(r"[\d\,\.\-\－]+")
    mth = ptn.findall(line)
    #沒中英文
    ptn = re.compile("a-zA-Z")
    
    if len(mth) >= 4 and \
        not stringUtil.hasChinese(line) and \
        ptn.search(line) is None :
        return True
    return False
        


def isStartLine(line):
    return isDataLine(line)


def isEndOfTable(line):
    return isDataLine(line) == False


class  PdfOnePage :
    STATICID = 65
    
    def __init__(self):
        self.lst = list()
        self.identity = chr(PdfOnePage.STATICID)
        PdfOnePage.STATICID += 1
        
    def __repr__(self):
        return toStringUtil.toString(self)
             

def getPdfTable(textContent):
    lst = list()
    tmpPage = PdfOnePage()
    startLineNumber = -1
    
    for (i, line) in enumerate(textContent.splitlines()):
        if startLineNumber == -1 and isStartLine(line) :
            startLineNumber = i
            tmpPage.lst.append(line)
            print("\t\t append : " + line)
            print("find start : " , startLineNumber , "\t" , line)
        elif startLineNumber != -1 :
            if isEndOfTable(line):
                lst.append(tmpPage)
                tmpPage = PdfOnePage()
                print("find end : ", (i + 1)  , " \t" , line)
                startLineNumber = -1
            else :
                tmpPage.lst.append(line)
                print("\t\t append : " + line)
                
    '''移除掉空物件'''                
    def chk(row):
        return len(row.lst) != 0

    lst = list(filter(chk, lst))
    return lst




def toRowDataLst(rowStr):
    arry = rowStr.strip().split(" ")
    isCountyTitleNeed = len(arry) > 5
    
    for i in range(0, len(arry)):
        arry[i] = fixNegativeNumber(arry[i])
        
    return (arry, isCountyTitleNeed)


#修正 負值
def fixNegativeNumber(numberStr):
    ptn = re.compile(r"^([0-9\,\.]+)[－-]$")
    mth = ptn.match(numberStr)
    if mth is not None :
        return "-" + mth.group(1);
    return numberStr



def createExcel(lst, targetXls):
    if len(lst) == 0 :
        ERROR_FILE.append(targetXls)
        return
    
    tabRegion = EnumHelper("gtu._test.janna.ex1.RCRP0S102.__TableRegion")
    wb = openpyxl.Workbook()
    
    for (i, table) in enumerate(lst):
        sheet = wb.create_sheet("工作表" + str(i + 1))
        for (j, row) in enumerate(table.lst):
#             reg = tabRegion.get(j)
            rowArry, isCountyTitleNeed = toRowDataLst(row)
#             print(">>> ", i, reg.chs, reg.eng, rowArry)
            rowData = list()
#             rowData.append(reg.chs) 
#             rowData.append(reg.eng) 
            rowData.extend(rowArry)
            sheet.append(rowData)
         
    wb.save(targetXls)



def getTargetXls(file):
    name = "janna_" + fileUtil.getNoSubName(file) + ".xlsx"
    newFile = fileUtil.getDesktopDir() + name
    print("create file = ", newFile)
    return newFile



def main(file):
    textContent = fileUtil.loadFile(file)
    print("textContent size = ", len(textContent))

    lst = getPdfTable(textContent)
    print("pdfTable size = ", len(lst))
    
    targetXls = getTargetXls(file)
    createExcel(lst, targetXls)



if __name__ == '__main__' :
    file = "C:/Users/gtu00/OneDrive/Desktop/秀娟0501/new_pdf"
    fileList = list()
    fileUtil.searchFilefind(file, ".*\.txt", fileList)
    
    for f in fileList :
        main(f)

    print("done..")

