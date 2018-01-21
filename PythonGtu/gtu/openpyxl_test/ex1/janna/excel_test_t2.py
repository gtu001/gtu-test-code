
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


class CellDefT2(OrderedClass) :
    def __init__(self, row, yyy, mm):
        OrderedClass.__init__(self)
        self.statistic_yyy = yyy  # 統計年月
        self.statistic_mm = mm  # 統計年月
        region = RegionDefEnum.findRegion(excelUtil.getCellValue("a", row))  # 行政區域代碼
        if region == None :
            raise Exception("region無法取得 :" + excelUtil.getCellValue("a", row)) 
        else:
            self.region = region
        self.people_tot = rnd(excelUtil.getCellValue("b", row))  # 本月底人口數
        self.people_tot_m = rnd(excelUtil.getCellValue("c", row))  # 本月底人口數_男
        self.people_tot_f = rnd(excelUtil.getCellValue("d", row))  # 本月底人口數_女
        self.birth_cnt = rnd(excelUtil.getCellValue("e", row))  # 本月份出生數
        self.death_cnt = rnd(excelUtil.getCellValue("f", row))  # 本月份死亡數
        self.increase_cnt = rnd(excelUtil.getCellValue("g", row))  # 本月份增加數
        self.increase_rate = rnd(excelUtil.getCellValue("h", row))  # 本月份增加率
        self.last_mon_people_cnt = rnd(excelUtil.getCellValue("i", row))  # 上月底人口數
        self.last_mon_increase_cnt = rnd(excelUtil.getCellValue("j", row))  # 較上月底增加數
        self.last_mon_increase_rate = rnd(excelUtil.getCellValue("k", row))  # 較上月底增加率
        self.last_year_dec_people_cnt = rnd(excelUtil.getCellValue("l", row))  # 去年12月人口數
        self.last_year_dec_increase_cnt = rnd(excelUtil.getCellValue("m", row))  # 較去年12月增加數
        self.last_year_dec_increase_rate = rnd(excelUtil.getCellValue("n", row))  # 較去年12月增加率
        self.last_year_people_cnt = rnd(excelUtil.getCellValue("o", row))  # 去年同月人口數
        self.last_year_increase_cnt = rnd(excelUtil.getCellValue("p", row))  # 較去年同月增加數
        self.last_year_increase_rate = rnd(excelUtil.getCellValue("q", row))  # 較去年同月增加率
    
#     T2
# a = 區域別
# b = 本月底人口數合計
# c = 本月底人口數男
# d = 本月底人口數女
# e = 本月份自然增加數 出生
# f = 本月份自然增加數 死亡
# g = 本月份自然增加數 實數
# h = 本月份自然增加數 折合年增加率
# i = 與上月底人口數比較 上月底人口數
# j = 與上月底人口數比較 較上月底人口增加數 實數
# k = 與上月底人口數比較 較上月底人口增加數 折合年增加率
# l = 與上年12月底人口數比較 上年12月底人口數
# m = 與上年12月底人口數比較 較上年12月底人口增加數 實數
# n = 與上年12月底人口數比較 較上年12月底人口增加數 折合年增加率
# o = 與上年同月底人口數比較 上年同月底人口數
# p = 與上年同月底人口數比較 較上年同月底人口增加數 實數
# q = 與上年同月底人口數比較 較上年同月底人口增加數 折合年增加率


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
    

def writeToCSV(lst, yyy):
    if len(lst) == 0 :
        raise Exception("無資料 !")
    keys = lst[0].keys()
    csvTitles = [k.upper() for k in keys]
    file = fileUtil.getDesktopDir() + yyy + "_T2.csv"
    file2 = fileUtil.getDesktopDir() + yyy + "_T2_comma.csv"
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



def main(fileLst, yyy):
    lst = list()
    
    for filePath in fileLst :
        mainDetail(filePath, yyy, lst)
        print("##", len(lst))
    
    writeToCSV(lst, yyy)
    
    
def mainDetail(filePath, yyy, lst):
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
    sheetsName = filter(lambda x : x.startswith("T2"), wb.get_sheet_names())
    for name in sheetsName :
        print(name)
        sheet = wb.get_sheet_by_name(name)
        mm = getMm(name, "T2")
        for i, row in enumerate(sheet, 1):
#             print("process row ", i)
            region = excelUtil.getCellValue("a", row)
            if i >= 11 and stringUtil.isNotBlank(str(region)) :
                try:
                    lst.append(CellDefT2(row, yyy, mm))
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
     ]
    main(fileLst, "105")
    print("done...")
