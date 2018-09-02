
from _decimal import Decimal
from collections import OrderedDict
import csv
from enum import Enum
import inspect
import numbers
import re

import openpyxl

from gtu.collection.orderedClass import OrderedClass
from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.openpyxl_test import excelUtil
from gtu.string import stringUtil
from gtu.number import numberUtil


class RptStyle(Enum):
    T3 = 1 #出生
    T4 = 2 #死亡
    T5 = 3 #結婚
    T6 = 4 #離婚
    def __init__(self, code):
        self.code = code
    @classmethod
    def getMatchCode(cls, tx):
        tx = tx.upper()
        for i,name in enumerate(RptStyle.__members__, 0):
            e = RptStyle[name]
            if e.name == tx :
                return e.code
        raise Exception("找不到RptStyle : " + tx)

class CellDefT2(OrderedClass) :
    def __init__(self, row, yyy, mm, tx):
        OrderedClass.__init__(self)
        self.statistic_yyy = yyy  # 統計年月
        self.statistic_mm = mm  # 統計年月
        region = RegionDefEnum.findRegion(excelUtil.getCellValue("a", row))  # 行政區域代碼
        if region == None :
            raise Exception("region無法取得 :" + excelUtil.getCellValue("a", row)) 
        else:
            self.region = region
            
        self.stype = RptStyle.getMatchCode(tx)  # 統計類別 1.出生 2.死亡 3.結婚 4.離婚
        self.cnt = rnd(excelUtil.getCellValue("b", row))  # 個數
        self.unadjusted_rate = rnd(excelUtil.getCellValue("c", row))  # 未調整千分率
        self.unadjusted_increase = rnd(excelUtil.getCellValue("d", row))  # 未調整變動數
        self.season_rate = rnd(excelUtil.getCellValue("e", row))  # 季節調整千分率
        self.season_increase = rnd(excelUtil.getCellValue("f", row))  # 季節調整變動數
        self.cumulate_rate = rnd(excelUtil.getCellValue("g", row))  # 累積千分率
        self.cumulate_increase = rnd(excelUtil.getCellValue("h", row))  # 累積變動數
        # TODO
        self.unadjusted_increase = "0"
        self.season_increase = "0"
        self.cumulate_increase = "0"
        
# a = 區域別
# b = 出生數
# c = 未調整出生率 千分率
# d = 未調整出生率 變動率
# e = 季節調整出生率 千分率
# f = 季節調整出生率 變動率
# g = 1月至1月累積出生率 千分率
# h = 1月至1月累積出生率 變動率

class RegionDefEnum(Enum):
    Region1 = ("總計")
    RegionZ1 = ("臺閩地區")
    Region65000000 = ("新北市")
    Region63000000 = ("臺北市")
    Region68000000 = ("桃園市")
    RegionA68000000 = ("桃園縣")
    Region66000000 = ("臺中市")
    Region67000000 = ("臺南市")
    Region64000000 = ("高雄市")
    Region10000000 = ("臺灣省")
    Region10002000 = ("宜蘭縣")
    Region10004000 = ("新竹縣")
    Region10005000 = ("苗栗縣")
    Region10007000 = ("彰化縣")
    Region10008000 = ("南投縣")
    Region10009000 = ("雲林縣")
    Region10010000 = ("嘉義縣")
    Region10013000 = ("屏東縣")
    Region10014000 = ("臺東縣")
    Region10015000 = ("花蓮縣")
    Region10016000 = ("澎湖縣")
    
    Region10018000 = ("新竹市")
    Region10020000 = ("嘉義市")
    Region09 = ("福建省")
    Region09020000 = ("金門縣")
    Region09007000 = ("連江縣")
    # 舊資料
    RegionOld65000000 = ("臺北縣")
    RegionOld10002000 = ("宜蘭縣")
    RegionOld68000000 = ("桃園縣")
    RegionOld10004000 = ("新竹縣")
    RegionOld10005000 = ("苗栗縣")
    RegionOldZ66000000 = ("臺中縣")
    RegionOld10007000 = ("彰化縣")
    RegionOld10008000 = ("南投縣")
    RegionOld10009000 = ("雲林縣")
    RegionOld10010000 = ("嘉義縣")
    RegionOldZ67000000 = ("臺南縣")
    RegionOldZ64000000 = ("高雄縣")
    RegionOld10013000 = ("屏東縣")
    RegionOld10014000 = ("臺東縣")
    RegionOld10015000 = ("花蓮縣")
    RegionOld10016000 = ("澎湖縣")
    Region10017000 = ("基隆市")
    RegionOld10017000 = ("基隆市")
    RegionZ10017000 = ("基隆巿")
    RegionOld10018000 = ("新竹市")
    RegionZ10018000 = ("新竹巿")
    RegionOld66000000 = ("臺中市")
    RegionOld10020000 = ("嘉義市")
    RegionZ10020000 = ("嘉義巿")
    RegionOld67000000 = ("臺南市")
    RegionOld63000000 = ("臺北市")
    RegionOld64000000 = ("高雄市")
    RegionOld09 = ("福建省")
    RegionOld09020000 = ("金門縣")
    RegionOld09007000 = ("連江縣")

    def __init__(self, chs):
        self.chs = chs
    
    @staticmethod
    def findRegion(chs):
        chs = stringUtil.trimSpace(chs)
        chs = re.sub(r"[a-zA-Z]", "", chs)
        for i, name in enumerate(RegionDefEnum.__members__, 0):
            e = RegionDefEnum[name]
            if e.chs == chs :
                line = e.name
                line = re.sub(r"[a-zA-Z]", "", line)
                return line
        return None
    

def writeToCSV(lst, tx, yyy):
    keys = lst[0].keys()
    csvTitles = [k.upper() for k in keys]
    file = fileUtil.getDesktopDir() + yyy + "_" + tx + ".csv"
    file2 = fileUtil.getDesktopDir() + yyy + "_" + tx + "_comma.csv"
    with open(file, 'w', newline='', encoding='utf-8') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',',
                                quotechar='"', quoting=csv.QUOTE_MINIMAL)
        spamwriter.writerow(csvTitles)
        for i, row in enumerate(lst, 0):
            vlst = list()
            for k in keys :
                vlst.append(row.__dict__[k])
            spamwriter.writerow(vlst)
            
    writeCSVandAppendEndComma(file, file2)


def writeCSVandAppendEndComma(file, file2):
    f2 = open(file2, "w")
    f = open(file, 'r')
    while True:
        line = f.readline()
        line = line.replace("\n", "")
        if not line :
            break
        f2.write(line + ",\n")
    f2.flush()
    f2.close()



def main(fileLst, yyy, tx):
    lst = list()
    
    for filePath in fileLst :
        mainDetail(filePath, yyy, tx, lst)
    
    writeToCSV(lst, yyy, tx)
    
    
def mainDetail(filePath, yyy, tx, lst):
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
    sheetsName = filter(lambda x : x.startswith(tx), wb.get_sheet_names())
    for name in sheetsName :
        print(name)
        sheet = wb.get_sheet_by_name(name)
        mm = getMm(name, tx)
        for i, row in enumerate(sheet, 1):
#             print("process row ", i)
            region = excelUtil.getCellValue("a", row)
            if i >= 11 and stringUtil.isNotBlank(str(region)) :
                try:
                    lst.append(CellDefT2(row, yyy, mm, tx))
                except Exception as ex :
                    errorHandler.printStackTrace()
                    

def getMm(name, tx):
    mth = re.match(r"" + tx + "[-_](\d{2})", name, flags=0)
    if mth:
        return mth.group(1)
    raise Exception("無法取得 MM : " + name)
    
    
def rnd(strVal):
    return numberUtil.roundHalfUp4(strVal, 5)


if __name__ == '__main__':
    fileLst = \
    [
     "C:/Users/gtu001/Desktop/db_data_歷年/T2/105春-T1-T7.xlsx",
     "C:/Users/gtu001/Desktop/db_data_歷年/T2/105夏-T1-T7-新修正T3-6季節調整變動率等(1060216).xlsx",
     "C:/Users/gtu001/Desktop/db_data_歷年/T2/105秋-T1-T7-新修正T3-6季節調整變動率(1060216).xlsx",
    "C:/Users/gtu001/Desktop/db_data_歷年/T2/105冬-T1-T7公式.xlsx",
#         "G:/forJanna/T2 T3 T4 T5 T6/105春-T1-T7.xlsx",
#         "G:/forJanna/T2 T3 T4 T5 T6/105夏-T1-T7-新修正T3-6季節調整變動率等(1060216).xlsx",
#         "G:/forJanna/T2 T3 T4 T5 T6/105秋-T1-T7-新修正T3-6季節調整變動率(1060216).xlsx",
#         "G:/forJanna/T2 T3 T4 T5 T6/105冬-T1-T7公式.xlsx",
     ]
    main(fileLst, "105", "T3")
    main(fileLst, "105", "T4")
    main(fileLst, "105", "T5")
    main(fileLst, "105", "T6")
    print("done...")
