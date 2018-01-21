
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
from gtu.string import stringUtil
from gtu.regex import replaceContent



def main(map):
    lst = list()
    
    dir = Path("C:/Users/gtu00/OneDrive/Desktop/\u5C0F\u5A1F")
    # checkSelf.checkMembers(dir)
    for i, f in enumerate(dir.iterdir(), 0) :
        print(i, f)
        mainDetail(f)
        
        
def mainDetail(filePath):
    filename = Path(filePath).name.replace(".xlsx", "")
    
    sheetIndex = ""
    mth = re.match("RCRP0S\d0(\d)", filename)
    if mth:
        sheetIndex = mth.group(1)
    else:
        raise Exception("找不到對應 : " + filename)
    
    wb = openpyxl.load_workbook(str(filePath), data_only=False)
    
    for sh_idx, sheetName in enumerate(wb.get_sheet_names(), 0) :
        sheet = wb.get_sheet_by_name(sheetName)
        for i, row in enumerate(sheet, 0):
            for cellIdx, cell in enumerate(row, 0):
                if cell.value is not None :
                    resetFormula(cell)
                    
    wb.save(fileUtil.getDesktopDir() + filename + "_New.xlsx");
    
    

def resetFormula(cell):
    text = str(cell.value)
    mth = re.match(r"\=ROUND.*\,(2)\)", text)
    if mth :
        prefix = text[:mth.start(1)]
        suffix = text[mth.end(1):]
        result = prefix + "5" + suffix
        print(result)
        cell.value = result
    else:
        pass
                    

if __name__ == '__main__':
    main(map)
    print("done...")
