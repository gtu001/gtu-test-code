
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
from gtu.reflect import toStringUtil


class CellDef(OrderedClass) :

    def __init__(self, row, sheetname):
        OrderedClass.__init__(self)
        self.statistic_yyy = "106"  # 統計年月
        
        mth = re.match("T\d\-(\d+)", sheetname)
        if mth:
            self.statistic_mm = mth.group(1)
        else:
            raise Exception("找不到年 : " + sheetname)
        
        region = RegionDefEnum.findRegion(excelUtil.getCellValue("a", row))  # 行政區域代碼
        if region == None :
            raise Exception("region無法取得 :" + excelUtil.getCellValue("a", row)) 
        else:
            self.region = region
            
        self.people_tot = rnd(excelUtil.getCellValue("b", row))
        self.people_tot_m = rnd(excelUtil.getCellValue("c", row))
        self.people_tot_f = rnd(excelUtil.getCellValue("d", row))
        self.birth_cnt = rnd(excelUtil.getCellValue("e", row))
        self.death_cnt = rnd(excelUtil.getCellValue("f", row))
        self.increase_cnt = rnd(excelUtil.getCellValue("g", row))
        self.increase_rate = rnd(excelUtil.getCellValue("h", row))
        self.last_mon_people_cnt = rnd(excelUtil.getCellValue("i", row))
        self.last_mon_increase_cnt = rnd(excelUtil.getCellValue("j", row))
        self.last_mon_increase_rate = rnd(excelUtil.getCellValue("k", row))
        self.last_year_dec_people_cnt = rnd(excelUtil.getCellValue("l", row))
        self.last_year_dec_increase_cnt = rnd(excelUtil.getCellValue("m", row))
        self.last_year_dec_increase_rate = rnd(excelUtil.getCellValue("n", row))
        self.last_year_people_cnt = rnd(excelUtil.getCellValue("o", row))
        self.last_year_increase_cnt = rnd(excelUtil.getCellValue("p", row))
        self.last_year_increase_rate = rnd(excelUtil.getCellValue("q", row))

    def __repr__(self):
        return toStringUtil.toString(self)
        

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
    

def writeToCSV(lst):
    keys = lst[0].keys()
    csvTitles = [k.upper() for k in keys]
    file = fileUtil.getDesktopDir() + "RCRP0SX02_simple.csv"
    file2 = fileUtil.getDesktopDir() + "RCRP0SX02_simple_comma.csv"
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


def main(fileArry):
    lst = list()
    
    for i, filepath in enumerate(fileArry, 0) :
        mainDetail(filepath, lst)
        
    writeToCSV(lst)
    
    
class RptDefName(Enum):
    RCRP0S102 = (["T2-01", "T2-02", "T2-03"])
    RCRP0S202 = (["T2-04", "T2-05", "T2-06"])
    RCRP0S302 = (["T2-07", "T2-08", "T2-09"])
    RCRP0S402 = (["T2-10", "T2-11", "T2-12"])

    def __init__(self, arry):
        self.arry = arry
        
        
def mainDetail(filePath, lst):
    rpt = RptDefName[Path(filePath).name.replace("_106.xlsx", "")]
    print(rpt)
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
    for name in rpt.arry :
        sheet = wb.get_sheet_by_name(name)
        for i, row in enumerate(sheet, 1):
#             print("process row ", i)
            region = excelUtil.getCellValue("a", row)
            if i >= 11 and stringUtil.isNotBlank(str(region)) :
                try:
                    lst.append(CellDef(row, name))
                except Exception as ex :
                    errorHandler.printStackTrace()
                    
                    
def rnd(strVal):
    return numberUtil.roundHalfUp(strVal, 15)


if __name__ == '__main__':
    arry = [
#         "C:/Users/gtu00/OneDrive/Desktop/20180116\u79C0\u5A1F_\u8F49\u6A94\u7A0B\u5F0F/RCRP0S102.xlsx",
#         "C:/Users/gtu00/OneDrive/Desktop/20180116\u79C0\u5A1F_\u8F49\u6A94\u7A0B\u5F0F/RCRP0S202.xlsx",
#         "C:/Users/gtu00/OneDrive/Desktop/20180116\u79C0\u5A1F_\u8F49\u6A94\u7A0B\u5F0F/RCRP0S302.xlsx",
#         "C:/Users/gtu00/OneDrive/Desktop/20180116\u79C0\u5A1F_\u8F49\u6A94\u7A0B\u5F0F/RCRP0S402.xlsx",
        "C:/Users/gtu00/OneDrive/Desktop/20180423秀娟_轉檔程式/RCRP0S102_106.xlsx",
        "C:/Users/gtu00/OneDrive/Desktop/20180423秀娟_轉檔程式/RCRP0S202_106.xlsx",
        "C:/Users/gtu00/OneDrive/Desktop/20180423秀娟_轉檔程式/RCRP0S302_106.xlsx",
        "C:/Users/gtu00/OneDrive/Desktop/20180423秀娟_轉檔程式/RCRP0S402_106.xlsx",
        ]
    main(arry)
    print("done...")
