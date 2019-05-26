
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
from gtu.number import numberUtil


HAS_G8_TAO = False  # 是否是103年以前(不含)的資料

rowIdMapping = {}

rowIdMapping2 = {}


def getAgeDef(sheet):
    map = {}
    for i, cell in enumerate(sheet[3], 0):#第三row
        if cell.value != None:
            strVal = str(cell.value).strip()
            if not stringUtil.hasChinese(strVal):
                map[i] = strVal
                if len(strVal) > 3:
                    raise Exception("錯誤age : " + strVal)
    return map


class CellDef():    
    def __init__(self, value, rowIndex, cellIndex, defMap, sheetName, sheetIndex):
        self.value = numberUtil.fixdScale(value, 3)
        self.rowIndex = rowIndex
        self.cellIndex = cellIndex
        self.sheetIndex = sheetIndex
        if self._getRowIdMapping(sheetIndex).__contains__(rowIndex):
            self.region = self._getRowIdMapping(sheetIndex).get(rowIndex)
        else:
            self.region = ""
        self.type = self._getRowIdMapping2(rowIndex) #gender
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
        if self.type not in ('M', 'F', 'T') :
            return False
        if self.region == '':
            return False
        if not numberUtil.isNumber(self.value) :
            return False
        return True
            
    def __repr__(self):
        return "sheet={}, row[{}:{}], val={}, region={}, type={}, age={}, ".format(\
            self.sheet, self.rowIndex, self.cellIndex, self.value, self.region, self.type, self.age) 


def _getGender(val):
    if val == 'M' :
        return 1
    elif val == 'F' :
        return 2
    elif val == 'T' :
        return 0
    else :
        raise Exception("找不到gender : " + val)
    
    
def getExcelRow(yyy, row):
    map = {}
    
    f = lambda x: 1 if x == 'M' else 2
    
    map[0] = yyy #STATISTIC_YYY
    map[1] = row.region #REGION
    #map[2] = 1 if row.type == 'M' else 2 #GENDER
    map[2] = _getGender(row.type) #GENDER
    map[3] = row.age #AGE
    map[4] = row.value #DEATHS_RATES
    
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
    filename = fileUtil.getDesktopDir() + "GTU_RCRP0639_" + yyy + "_4.xlsx"
    print("xlsx = ", filename)
    wb2.save(filename);
    
    
def writeContentCSV(yyy, lst):
    filename = fileUtil.getDesktopDir() + "GTU_RCRP0639_" + yyy + "_4.csv"
    print("csv = ", filename)
    with open(filename, 'w', newline='') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',',
                                quotechar='"', quoting=csv.QUOTE_MINIMAL)
        spamwriter.writerow(["STATISTIC_YYY", "REGION", "GENDER", "AGE", "DEATHS_RATES"])
        for i, row in enumerate(lst, 0):
            if row.valid == True:
                rowArry = getExcelRow(yyy, row)
                spamwriter.writerow(rowArry)
                

def getSheet(filePath):
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
    return wb.get_sheet_by_name("原來")


def main(filePath, yyy):
    lst = []
    
    sheet = getSheet(filePath)
        
    regionMap = RegionGroupDetail.getRegionDefMap(sheet, -1)
    if len(regionMap) != 0:
        global rowIdMapping
        rowIdMapping = regionMap
        global rowIdMapping2
        rowIdMapping2 = RegionGroupDetail.getTMFDefMap(regionMap)
    
    defMap = getAgeDef(sheet);
    print("ageDef", defMap)
    #print("rowIdMapping", rowIdMapping)
    #print("rowIdMapping2", rowIdMapping2)
    
    startTime = dateUtil.unix_time_millis()
    for i, row in enumerate(sheet, 1):
        for j, cell in enumerate(row, 0):
            newVal = CellDef(cell.value, i, j, defMap, "原來", 0)
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
#     main("C:/SA/綠色便民案/年刊季刊/06.開發文件/myTest/639/RCRP0C639_一頁.xlsx", "105")
    main("C:/Users/gtu00/OneDrive/Desktop/RCRP0C639_一頁.xlsx", "105")
    print("done...")
        
        
