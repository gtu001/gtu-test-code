
from _decimal import Decimal
from collections import OrderedDict
import csv
from enum import Enum
import inspect
import numbers
from pathlib import Path
import re

import openpyxl

from gtu.collection.orderedClass import OrderedClass
from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.number import numberUtil
from gtu.openpyxl_test import excelUtil
from gtu.reflect import checkSelf
from gtu.reflect import toStringUtil
from gtu.string import stringUtil


class CellDef(OrderedClass):
    staticNo = 1
    
    def __init__(self, row):
        OrderedClass.__init__(self)
        self.no = CellDef.staticNo
        self.vocabulary = excelUtil.getCellValue("c", row).replace("\n", "")
        self.chinese = excelUtil.getCellValue("g", row).replace("\n", "")
        self.mp3 = excelUtil.getCellValue("e", row).replace("\n", "")
        self.kk = excelUtil.getCellValue("j", row).replace("\n", "")
        self.sen1 = excelUtil.getCellValue("w", row).replace("\n", "")
        self.sen1_mp3 = excelUtil.getCellValue("x", row).replace("\n", "")
        self.sen1_chinese = excelUtil.getCellValue("z", row).replace("\n", "")
        self.dj = excelUtil.getCellValue("i", row).replace("\n", "")
        self.type = excelUtil.getCellValue("f", row).replace("\n", "")
        self.page = excelUtil.getCellValue("d", row).replace("\n", "")
        self.memo = excelUtil.getCellValue("o", row).replace("\n", "")
        CellDef.staticNo += 1
    
    def __repr__(self):
        return toStringUtil.toString(self, escapeLst = ["_keys"], order = self._keys)

def writeToCSV(lst):
    keys = lst[0].keys()
    csvTitles = [k.upper() for k in keys]
    file = fileUtil.getDesktopDir() + "tk_dictionary_items.csv"
    with open(file, 'w', newline='', encoding='utf-8') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter='\t',
                                quotechar='"', quoting=csv.QUOTE_MINIMAL)
        spamwriter.writerow(csvTitles)
        for i, row in enumerate(lst, 0):
            vlst = list()
            for k in keys :
                v = row.__dict__[k]
                vlst.append(v)
            spamwriter.writerow(vlst)


def main(filePath):
    lst = mainDetail(filePath)
    
    for i, row in enumerate(lst, 0):
        print(i, row)
    
    writeToCSV(lst)
    
        
        
def mainDetail(filePath):
    lst = list()
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
    sheet = wb.get_sheet_by_name("工作表1")
    for i, row in enumerate(sheet, 1):
        if i == 1 :
            continue
        lst.append(CellDef(row))
    return lst;


if __name__ == '__main__':
    main("D:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/openpyxl_test/ex1/janna/tk_dictionary_items.xlsx")
    print("done...")
