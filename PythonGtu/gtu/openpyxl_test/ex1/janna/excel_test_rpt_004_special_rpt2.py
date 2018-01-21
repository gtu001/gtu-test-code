
import csv
from enum import Enum
from inspect import getmembers
import re

import openpyxl
from openpyxl.workbook.workbook import Workbook

from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.openpyxl_test.ex1.janna import excel_test_rpt_04
from gtu.openpyxl_test.ex1.janna.excel_test_rpt_004_special_enum import AgeDef, RegionGroupDetail
from gtu.openpyxl_test.ex1.janna.excel_test_rpt_04 import AgeGroup, CellDef2
from gtu.openpyxl_test.ex1.janna.excel_test_rpt_37 import RegionDefEnum, TaoRecac
from gtu.reflect import checkSelf
from gtu.string import stringUtil


HAS_G8_TAO = False  # 是否是103年以前(不含)的資料

rowIdMapping = {}

rowIdMapping2 = {}


def getDistinctRegion():
    lst = set()
    for i, name in enumerate(RegionDefEnum.__members__, 0) :#RegionDefEnum RegionGroupDetail
        val = re.sub(r"[a-zA-Z]", "", name)
        lst.add(val)
    return lst


def getAgeDef(sheet):
    map = {}
    for i, cell in enumerate(sheet[2], 0):
        strVal = str(cell.value)
        try:
            if strVal.__contains__("100"):
                strVal = "100"
            int(strVal)
            map[i] = strVal
        except Exception as ex:
#             errorHandler.printStackTrace2(ex);
            pass
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


def isCellDef2(row):
    return str(type(row)).__contains__("gtu.openpyxl_test.ex1.janna.excel_test_rpt_04.CellDef2")


def getExcelRow(yyy, row):
    map = {}
    
    f = lambda x: 1 if x == 'M' else 2
    
    map[0] = yyy
    map[1] = row.region
    map[2] = row.region  # ADMIN_OFFICE_CODE
#     map[3] = f(row.type)
    map[3] = row.region  # SITE_ID
    map[4] = 1 if row.type == 'M' else 2
    
    map[5] = "5" if isCellDef2(row) else '1'  
    
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
                
                
def writeContentCSV2(yyy, lst):
    with open(fileUtil.getDesktopDir() + yyy + "_4_五年組.csv", 'w', newline='', encoding='utf-8') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',',
                                quotechar='"', quoting=csv.QUOTE_MINIMAL)
        spamwriter.writerow(["STATISTIC_YYY", "REGION", "ADMIN_OFFICE_CODE", "SITE_ID", "GENDER", "AGE_TYPE", "AGE", "CNT"])
        for i, row in enumerate(lst, 0):
            if row.valid == True and isCellDef2(row):
                rowArry = getExcelRow(yyy, row)
                spamwriter.writerow(rowArry)
                
                
def getSheet(filePath):
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
    return wb.get_sheet_by_name("年中單一")


def caculateAgeGroup(lst):
    newLst = []
    sexLst = ['M', 'F']
    regionLst = getDistinctRegion()
    for i, region in enumerate(regionLst, 0):
        for j, sex in enumerate(sexLst, 0):
            for i, name in enumerate(AgeGroup.__members__, 0):
                e = AgeGroup[name]
#                 print(i, e.name, e.fromAge, e.toAge)
                newVal = e.sumRegionValue(region, sex, lst)
                newLst.append(CellDef2(e.name, sex, region, newVal))
    return newLst


def main(filePath, yyy):
    lst = []
    
    sheet = getSheet(filePath)
        
    regionMap = RegionDefEnum.getRegionDefMap(sheet, -1)
    if len(regionMap) != 0:
        global rowIdMapping
        rowIdMapping = regionMap
        global rowIdMapping2
        rowIdMapping2 = RegionDefEnum.getTMFDefMap(regionMap)
    
    defMap = getAgeDef(sheet);
    print("ageDef", defMap)
    
    for i, row in enumerate(sheet, 1):
        for j, cell in enumerate(row, 0):
            newVal = CellDef(cell.value, i, j, defMap, "年中單一", 0)
            lst.append(newVal)
            
    newLst = caculateAgeGroup(lst)
    lst.extend(newLst)
    
    # writeContent(yyy, lst)
    writeContentCSV(yyy, lst)
    
    writeContentCSV2(yyy, lst)
    

if __name__ == '__main__' :
#     main("C:/Users/gtu001/Desktop/db_data_歷年/96-100/96-年中.xlsx", "96")
#     main("C:/Users/gtu001/Desktop/db_data_歷年/96-100/97-年中.xlsx", "97")
#     main("C:/Users/gtu001/Desktop/db_data_歷年/96-100/98-年中.xlsx", "98")
#     main("C:/Users/gtu001/Desktop/db_data_歷年/96-100/99-年中.xlsx", "99")
#     main("C:/Users/gtu001/Desktop/db_data_歷年/96-100/100-年中.xlsx", "100")

    main("C:/Users/gtu001/Desktop/db_data_歷年/101-105/105_年刊/T-年中.xlsx", "105")
    print("done...")
        
        
