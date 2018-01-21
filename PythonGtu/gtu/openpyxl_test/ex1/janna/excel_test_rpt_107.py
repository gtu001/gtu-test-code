
import csv
from enum import Enum
import functools
from inspect import getmembers
import re

import openpyxl
from openpyxl.workbook.workbook import Workbook

from gtu.base import equalUtil
from gtu.collection import orderedClass
from gtu.datetime import dateUtil
from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.openpyxl_test import excelUtil
from gtu.openpyxl_test.ex1.janna.excel_test_rpt_004_special_enum import AgeDef, RegionGroupDetail
from gtu.openpyxl_test.ex1.janna.excel_test_rpt_37 import RegionDefEnum, TaoRecac
from gtu.reflect import checkSelf, toStringUtil
from gtu.string import stringUtil


rowIdMapping = {}

rowIdMapping2 = {}

class RegionNotFound(Exception):
    def __init__(self, message):
        Exception.__init__(self, str(message))
class SexNotFound(Exception):
    def __init__(self, message):
        Exception.__init__(self, str(message))
class MonthNotMatch(Exception):
    def __init__(self, message):
        Exception.__init__(self, str(message))
        
class RowDef(orderedClass.OrderedClass):    
    pk = ["statistic_yyy", "statistic_mm", "region", "gender"]
    isYyymm = False
    def __init__(self, row, yyy, rowIndex, sheetName):
        orderedClass.OrderedClass.__init__(self)
        if RowDef.isYyymm :
            self.statistic_yyymm = yyy + self.getMm(sheetName)  # 統計年月
            RowDef.pk = ["statistic_yyymm", "region", "gender"]
        else :
            self.statistic_yyy = yyy
            self.statistic_mm = self.getMm(sheetName) 
            RowDef.pk = ["statistic_yyy", "statistic_mm", "region", "gender"]
        self.region = self.getRegion(rowIndex)  # 行政區域代碼
        self.gender = self.getSex(rowIndex)  # 性別代碼
        self.birth_total = RowDef.getv(excelUtil.getCellValue("d", row))  # 嬰兒出生數_計
        self.birth_legal = RowDef.getv(excelUtil.getCellValue("e", row))  # 嬰兒出生數—婚生
        self.birth_illegal_recognized = RowDef.getv(excelUtil.getCellValue("f", row))  # 嬰兒出生數—非婚生:己認領
        self.birth_illegal_unrecognized = RowDef.getv(excelUtil.getCellValue("g", row))  # 嬰兒出生數—非婚生:未認領
        self.helpless_child = RowDef.getv(excelUtil.getCellValue("h", row))  # 嬰兒出生數—無依兒童
        self.death = RowDef.getv(excelUtil.getCellValue("i", row))  # 死亡人數
        self.marry_pair = RowDef.getv(excelUtil.getCellValue("j", row))  # 結婚對數
        self.divorce_pair = RowDef.getv(excelUtil.getCellValue("k", row))  # 離婚對數
        self.in_foreign = RowDef.getv(excelUtil.getCellValue("m", row))  # 戶籍遷入國內
        self.out_foreign = RowDef.getv(excelUtil.getCellValue("n", row))  # 戶籍遷出國外
        self._hash = equalUtil.hashInclude(self, RowDef.pk)
        self._rowIndex = rowIndex
        self._sheetName = sheetName
        
    def getMm(self, sheetName):
        mth = re.match(r"^T\d+\-(\d+)", sheetName)
        if mth:
            return mth.group(1)
        raise MonthNotMatch(sheetName)

    def getRegion(self, rowIndex):
        region = rowIdMapping.get(rowIndex, None)
        if not region :
            raise RegionNotFound("無region : " + str(rowIndex))
        return region
    
    def getSex(self, rowIndex):
#         男:1 |女:2
        f = lambda x: '1' if x == 'M' else ('2' if x == 'F' else "")
        for k, v in rowIdMapping2.items() :
            if v.__contains__(rowIndex):
                return f(k)
        raise SexNotFound("無sex : " + str(rowIndex));
    
    def isValid(self):
        if self.region == '' :
            self.failType = "REGION"
            return False
        if self.gender == '' :
            self.failType = "GENDER"
            return False
        if not re.match(r"^T\d+\-\d+$", self._sheetName) :
            self.failType = "SHEET"
            return False
        return True
    
    @classmethod
    def getv(cls, val):
        if val in ('-', '─', ''):
            return 0
        return val
    
    def __repr__(self):
        return toStringUtil.toString(self, escapeLst=["_keys"], order=self._keys)
        
# c 性別
# d 出生計
# e 出生婚生
# f 出生分婚生以認領
# g 出生分婚生未認領
# h 出生無依兒童
# i 死亡
# j 結婚對數
# k 離婚對數
# l 國際戶籍遷徙淨戶籍遷徙數
# m 國際戶籍遷徙戶籍遷入國內
# n 國際戶籍遷徙戶籍遷出國外
# o 自國外
# p 初設戶籍

    
def writeToCSV(lst, excludeLst):
    suffixFileName = 'yyymm' if RowDef.isYyymm else 'yyySplitMm' 
    keys = [x for x in lst[0].keys() if x not in excludeLst]
    csvTitles = [k.upper() for k in keys]
    file = fileUtil.getDesktopDir() + "RCDFZ303_data_forJanna_" + suffixFileName + ".csv"
    with open(file, 'w', newline='', encoding='utf-8') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',',
                                quotechar='"', quoting=csv.QUOTE_MINIMAL)
        spamwriter.writerow(csvTitles)
        for i, row in enumerate(lst, 0):
            vlst = list()
            for k in keys :
                v = row.__dict__[k]
                vlst.append(v)
            spamwriter.writerow(vlst)
                

def main(fileArray, yyy):
    lst = []
    
    for filePath in fileArray :
        wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
        for sheetName in wb.get_sheet_names() :
            sheet = wb.get_sheet_by_name(sheetName)
                
            regionMap = RegionDefEnum.getRegionDefMap(sheet, -1)
            if len(regionMap) != 0:
                global rowIdMapping
                rowIdMapping = regionMap
                global rowIdMapping2
                rowIdMapping2 = RegionGroupDetail.getTMFDefMap(regionMap)
                
            count = 0
            failCount = 0
            failLst = list()
            for i, row in enumerate(sheet, 1):
                try:
                    newVal = RowDef(row, yyy, i, sheetName)
                    if newVal.isValid() :
                        constraintCheck(newVal, lst)
                        lst.append(newVal)
                        count += 1
                    else :
                        failCount += 1
                        failLst.append(newVal)
                except RegionNotFound as ex :
                    print(str(ex))
                except SexNotFound as ex :
                    print(str(ex))
                except Exception as ex :
                    errorHandler.printStackTrace()
                    exit(0)
                    
            print("#### sheet = {}, success = {}, fail = {}".format(sheetName, count, failCount))
                    
    sortLst(lst) 
            
    writeToCSV(lst, ["_hash", "_rowIndex", "_sheetName", ])
    
    
def sortLst(lst):
    if RowDef.isYyymm :
        lst.sort(key=lambda x : [x.statistic_yyymm, x.region, x.gender], reverse=False)
    else :
        lst.sort(key=lambda x : [x.statistic_yyy, x.statistic_mm, x.region, x.gender], reverse=False)


def constraintCheck(row, lst):
    for i, r in enumerate(lst, 0):
        if r._hash == row._hash and equalUtil.equalsInclude(r, row, RowDef.pk):
            raise Exception(str(i) + " : " + str(r) + " <-> " + str(row))


if __name__ == '__main__' :
    RowDef.isYyymm = False
    
    fileArry = [
        "C:/Users/gtu001/Desktop/db_data_歷年/S107-29/RCRP0S107.xlsx",
        "C:/Users/gtu001/Desktop/db_data_歷年/S107-29/RCRP0S207.xlsx",
        "C:/Users/gtu001/Desktop/db_data_歷年/S107-29/RCRP0S307.xlsx",
        "C:/Users/gtu001/Desktop/db_data_歷年/S107-29/RCRP0S407.xlsx",
        ]
    main(fileArry, "105")
    print("done...")
        
        
