
import csv
from enum import Enum
from inspect import getmembers
import re

import openpyxl
from openpyxl.workbook.workbook import Workbook

from gtu.base import equalUtil
from gtu.datetime import dateUtil
from gtu.io import fileUtil
from gtu.openpyxl_test.ex1.janna.excel_test_rpt_004_special_enum import AgeDef, RegionGroupDetail
from gtu.openpyxl_test.ex1.janna.excel_test_rpt_37 import RegionDefEnum, TaoRecac
from gtu.reflect import checkSelf
from gtu.string import stringUtil


HAS_G8_TAO = False  # 是否是103年以前(不含)的資料

rowIdMapping = {}

rowIdMapping2 = {}


def getDistinctRegion():
    lst = set()
    for i, name in enumerate(RegionGroupDetail.__members__, 0) :
        val = re.sub(r"[a-zA-Z]", "", name)
        lst.add(val)
    return lst


def getAgeDef(sheet):
    map = {}
    for i, cell in enumerate(sheet[2], 0):
        strVal = str(cell.value)
        ageDef = AgeDef.getAgeDef(strVal);
        if ageDef != "":
            map[i] = ageDef
    return map


class CellDef():    
    def __init__(self, value, rowIndex, cellIndex, defMap, sheetName, sheetIndex):
        self.value = value
        self.rowIndex = rowIndex
        self.cellIndex = cellIndex
        self.sheetIndex = sheetIndex
        if self._getRowIdMapping(sheetIndex).__contains__(rowIndex):
            self.region = self._getRowIdMapping(sheetIndex).get(rowIndex)
        else:
            self.region = ""
        self.type = self._getRowIdMapping2(rowIndex)
        if defMap.__contains__(cellIndex) :
            self.age = defMap[cellIndex]
        else:
            self.age = ""
        self.sheet = sheetName
        self.valid = self._isValidValue() 
        self.hashDef = equalUtil.hashInclude(self, ["valid", "type", "age", "region"])
    
    def _getRowIdMapping(self, sheetIndex):
        return rowIdMapping
        
    def _getRowIdMapping2(self, rowIndex):
        for k, v in rowIdMapping2.items():
            if v.__contains__(rowIndex):
                return k
        return ""
            
    def _isValidValue(self):
        if self.age == '' :
            return False
        if self.type not in ('M', 'F') :
            return False
        if self.region == '':
            return False
        return True
            
    def __repr__(self):
        return "sheet={}, row[{}:{}], val={}, region={}, type={}, age={}, ".format(\
            self.sheet, self.rowIndex, self.cellIndex, self.value, self.region, self.type, self.age) 

    
def getExcelRow(yyy, row):
    map = {}
    
    f = lambda x: 1 if x == 'M' else 2
    
    map[0] = yyy
    map[1] = row.region
    map[2] = row.region  # ADMIN_OFFICE_CODE
#     map[3] = f(row.type)
    map[3] = row.region  # SITE_ID
    map[4] = 1 if row.type == 'M' else 2
    
    map[5] = "5"
    
    map[6] = str(row.age).rjust(3, '0')
    map[7] = row.value
    
    lst = []
    sorted(map)
    for k, v in map.items():
        lst.append(v)
    return lst
    

def writeContent(yyy, lst):
    wb2 = Workbook()
    ws = wb2.active
    ws.append(["STATISTIC_YYY", "REGION", "ADMIN_OFFICE_CODE", "SITE_ID", "GENDER", "AGE_TYPE", "AGE", "CNT"])
    for i, row in enumerate(lst, 0):
        if row.valid == True:
            rowArry = getExcelRow(yyy, row)
            ws.append(rowArry)
    wb2.save(fileUtil.getDesktopDir() + yyy + "_4.xlsx");
    
    
def writeContentCSV(yyy, lst):
    with open(fileUtil.getDesktopDir() + yyy + "_4.csv", 'w', newline='', encoding='utf-8') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',',
                                quotechar='"', quoting=csv.QUOTE_MINIMAL)
        spamwriter.writerow(["STATISTIC_YYY", "REGION", "ADMIN_OFFICE_CODE", "SITE_ID", "GENDER", "AGE_TYPE", "AGE", "CNT"])
        for i, row in enumerate(lst, 0):
            if row.valid == True:
                rowArry = getExcelRow(yyy, row)
                spamwriter.writerow(rowArry)
                

def getSheet(filePath):
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
    return wb.get_sheet_by_name("年中五歲")

def main(filePath, yyy):
    lst = []
    
    sheet = getSheet(filePath)
        
    regionMap = RegionGroupDetail.getRegionDefMap(sheet, -2)
    if len(regionMap) != 0:
        global rowIdMapping
        rowIdMapping = regionMap
        global rowIdMapping2
        rowIdMapping2 = RegionGroupDetail.getTMFDefMap(regionMap)
    
    defMap = getAgeDef(sheet);
    print("ageDef", defMap)
    
    startTime = dateUtil.unix_time_millis()
    for i, row in enumerate(sheet, 1):
        for j, cell in enumerate(row, 0):
            newVal = CellDef(cell.value, i, j, defMap, "年中五歲", 0)
            if not isDuplicate(lst, newVal) :
                lst.append(newVal)
    endTime = dateUtil.unix_time_millis() - startTime
    print("during", endTime)
            
    # writeContent(yyy, lst)
    writeContentCSV(yyy, lst)
    

def isDuplicate(lst, single):
    for i, row in enumerate(lst, 0):
        if row.hashDef == single.hashDef and \
            equalUtil.equalsInclude(row, single, ["type", "age", "region"]):
            return True
    return False
       

if __name__ == '__main__' :
    main("C:/Users/gtu001/Desktop/db_data_歷年/101-105/105_年刊/T-年中.xlsx", "105")
    print("done...")
        
        
