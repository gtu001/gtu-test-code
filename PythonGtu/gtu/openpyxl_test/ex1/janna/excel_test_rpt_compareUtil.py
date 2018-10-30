
from _decimal import Decimal
from collections import OrderedDict
import csv
from enum import Enum
import inspect
import numbers
import os
from pathlib import Path
import re

import openpyxl

from gtu.collection.orderedClass import OrderedClass
from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.number import numberUtil
from gtu.openpyxl_test import excelUtil
from gtu.reflect import checkSelf
from gtu.regex import regexReplace
from gtu.string import stringUtil
from gtu.util import rangeUtil


def getRangeWarpper(val1, val2):
    start = min(val1, val2)
    end = max(val1, val2)
    return rangeUtil.RangeWrapper(range(start, end))


def fixCellValue(strVal):
    strVal = strVal.strip()
    rtnVal = ""
    for (i, v) in enumerate(strVal):
        if strVal[i] == ' ' or strVal[i] == '　' :
            continue
        rtnVal += strVal[i]
    return rtnVal


def fixCellValue_Zero(strVal):
    try:
        if int(strVal) == 0:
            return ""
    except :
        return strVal 
    
    
def isNumberEqual(val1, val2):
    try:
        if float(val1) == float(val2) :
            return True
    except :
        return False
        
def mainDetail(file1, file2):
    wb1 = openpyxl.load_workbook(file1, data_only=True)
    wb2 = openpyxl.load_workbook(file2, data_only=True)
    
    maxSheetRng = getRangeWarpper(len(wb1.get_sheet_names()), len(wb2.get_sheet_names()))
    
    for sh_idx in range(0, maxSheetRng.left) :
        s1Name = wb1.get_sheet_names()[sh_idx];
        s2Name = wb2.get_sheet_names()[sh_idx];
        
        print("開始比較 : ", sh_idx, s1Name, s2Name)
        
        sheet1 = wb1.get_sheet_by_name(s1Name)
        sheet2 = wb2.get_sheet_by_name(s2Name)
        
        maxRowRng = getRangeWarpper(sheet1.max_row, sheet2.max_row)
        maxColRng = getRangeWarpper(sheet1.max_column, sheet2.max_column)
        
        for row_idx in range(1, maxRowRng.left) :
            row1 = excelUtil.getRows(sheet1)[row_idx - 1]
            row2 = excelUtil.getRows(sheet2)[row_idx - 1]
            
            for col_idx in range(1, maxColRng.left) :
                cell1Val = excelUtil.getCellValue(col_idx, row1)
                cell2Val = excelUtil.getCellValue(col_idx, row2)
                cell1Val = fixCellValue(cell1Val)
                cell2Val = fixCellValue(cell2Val)
                cell1Val = fixCellValue_Zero(cell1Val)
                cell2Val = fixCellValue_Zero(cell2Val)
    
                if not isNumberEqual(cell1Val, cell2Val) and cell1Val != cell2Val:
                    colEnglish = excelUtil.cellEnglishToPos_toStr(col_idx + 1)
                    print("diff ", colEnglish + str(row_idx), "c1=", cell1Val, "c2=", cell2Val)
        
#         openpyxl.worksheet.worksheet.Worksheet
    

if __name__ == '__main__':
    file1 = fileUtil.getDesktopDir() + os.sep + "RCRP0C210_C8_1.XLSX"
    file2 = fileUtil.getDesktopDir() + os.sep + "RCRP0C210_I8_1.XLSX"
    mainDetail(file1, file2)
    print("done...")
