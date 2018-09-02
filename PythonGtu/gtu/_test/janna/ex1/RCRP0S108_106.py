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

def isStartLine(line):
    ptn = re.compile(r"[\d]+\~[\d]+\s")
    mth = ptn.findall(line)
    if len(mth) >= 3 :
        return True
    return False


def isDataLine(line):
    ptn = re.compile(r"^[0-9\—\,\s]+$")
    mth = ptn.search(line)
    if mth is not None :
        return True
    return False


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
            startLineNumber = i + 1
#             print("find start : " , startLineNumber , "\t" , line)
        elif startLineNumber != -1 :
            if isEndOfTable(line):
                lst.append(tmpPage)
                tmpPage = PdfOnePage()
#                 print("find end : ", (i + 1)  , " \t" , line)
                startLineNumber = -1
            else :
                tmpPage.lst.append(line)
#                 print("\t\t append : " + line)
    
    for (i, obj) in enumerate(lst) :
        if (i % 2) == 0:
            colSize = 11
        else :
            colSize = 16
            
        print(colSize, obj.lst)
        
        obj.lst = sliceToMartixArry(obj.lst, colSize=colSize)
    return lst




# 11
# 16
def sliceToMartixArry(orginArry, rowSize=3, colSize=11):
    npArry = numpyUtil.createEmptyMartix(100, 100)
    
    colIndex = 0
    rowIndex = 0
    for i in range(0, len(orginArry), rowSize) :
        newArry = listUtil.fullLst(rowSize, "NA")
        tmpArry = orginArry[i : i + rowSize]
        listUtil.arraycopy(tmpArry, 0, newArry, 0, len(tmpArry))
        
        newArry = np.array(newArry)
        newArry = np.reshape(newArry, (rowSize, 1), order='C')
         
        npArry[rowIndex : rowIndex + rowSize, colIndex:colIndex + 1] = newArry
        colIndex += 1
        
        if colIndex >= colSize:
            colIndex = 0
            rowIndex += 3
            
    #取得邊界
    maxRow, maxCol = numpyUtil.getMaxRowCol(npArry)
    npArry = npArry[0:maxRow + 1, 0:maxCol + 1]
    numpyUtil.transferNoneType(npArry, "NA")
    
    #轉回str lst
    lst = list()
    for i, row in enumerate(npArry):
        try:
            rowStr = " ".join(row.tolist())
            lst.append(rowStr)
        except Exception as ex :
            errorHandler.printStackTrace2(ex)
            raise Exception("Error Row " + str(row))
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
    
    wb = openpyxl.Workbook()
    
    for (i, table) in enumerate(lst):
        sheet = wb.create_sheet("工作表" + str(i + 1))
        
        index = 0
        for (j, row) in enumerate(table.lst):
            rowArry, isNeedCountyName = toRowDataLst(row)
#             print(">>> ", i, reg.chs, reg.eng, rowArry)
            rowData = list()
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
    file = fileUtil.getCurrentDir() + "RCRP0S108_106.pdf.txt"
    main(file)
    print("done..")

