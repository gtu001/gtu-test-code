
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
        self.Key = excelUtil.getCellValue("A", row, startByZero=True).replace("\n", "")
        self.CHT = excelUtil.getCellValue("B", row, startByZero=True).replace("\n", "")
        self.ENU = excelUtil.getCellValue("C", row, startByZero=True).replace("\n", "")
        self.IND = excelUtil.getCellValue("D", row, startByZero=True).replace("\n", "")
        self.TH = excelUtil.getCellValue("E", row, startByZero=True).replace("\n", "")
        self.VN = excelUtil.getCellValue("F", row, startByZero=True).replace("\n", "")
        CellDef.staticNo += 1
    def __repr__(self):
        return toStringUtil.toString(self, escapeLst = ["_keys"], order = self._keys)

        
def mainDetail(filePath):
    lst = list()
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
    sheet = wb.get_sheet_by_name("FMS文本20200212")
    for i, row in enumerate(sheet, 1):
        # if i == 1 :
        #     continue
        lst.append(CellDef(row))
    return lst



def main(filePath) :
    lst = mainDetail(filePath)
    
    for i, row in enumerate(lst, 0):
        print(i, row)



if __name__ == '__main__':
    excelFile = "/media/gtu001/OLD_D/workstuff/andy_Projects/repository1/SvnSpaceBase/弋揚Eup/國際版車隊APP/FMS文本20200212.xlsx"
    main(excelFile)
    print("done...")


