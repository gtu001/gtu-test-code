
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
from gtu.regex import regexReplace
import os


        
def mainDetail(file1, file2):
    wb1 = openpyxl.load_workbook(file1, data_only=True)
    wb2 = openpyxl.load_workbook(file2, data_only=True)
    
    maxSheetCount = min(len(wb1.get_sheet_names()), len(wb2.get_sheet_names()))
    
    for sh_idx in range(0, maxSheetCount) :
        s1Name = wb1.get_sheet_names()[sh_idx];
        s2Name = wb2.get_sheet_names()[sh_idx];
        
        sheet1 = wb1.get_sheet_by_name(s1Name)
        sheet2 = wb2.get_sheet_by_name(s2Name)
        
        maxRowIndex = min(sheet1.max_row, sheet2.max_row)
        
        for row_idx in range(1, maxRowIndex) :
            print(row_idx)
            
            row1 = list(sheet1.rows)[row_idx]
            row2 = list(sheet2.rows)[row_idx]
    
            print(row1)
            print(row2)
#         checkSelf.checkMembers(sheet1)
        
#         openpyxl.worksheet.worksheet.Worksheet
    

if __name__ == '__main__':
    file1 = fileUtil.getDesktopDir() + os.sep + "RCRP0C210_C8_1.XLSX"
    file2 = fileUtil.getDesktopDir() + os.sep + "RCRP0C210_I8_1.XLSX"
    mainDetail(file1, file2)
    print("done...")
