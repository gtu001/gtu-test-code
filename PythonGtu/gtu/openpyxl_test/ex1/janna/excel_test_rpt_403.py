
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


class CellDef(OrderedClass) :
    def __init__(self, row, sheetname, stype):
        OrderedClass.__init__(self)
        self.statistic_yyy = "104"  # 統計年月
        
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
            
        self.stype = stype  # 統計類別 1.出生 2.死亡 3.結婚 4.離婚
        self.cnt = "0"  # 個數
        self.unadjusted_rate = rnd(excelUtil.getCellValue("k", row))  # 未調整千分率
        self.unadjusted_increase = "0"  # 未調整變動數
        self.season_rate = "0"  # 季節調整千分率
        self.season_increase = "0"  # 季節調整變動數
        self.cumulate_rate = rnd(excelUtil.getCellValue("m", row))  # 累積千分率
        self.cumulate_increase = "0"  # 累積變動數

    def __repr__(self):
        return "{}".format(str(self.__dict__))
        

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
    file = fileUtil.getDesktopDir() + "_rcdfz302_104.csv"
    file2 = fileUtil.getDesktopDir() + "_rcdfz302_104_comma.csv"
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



def main(map):
    lst = list()
    
    for k,v in map.items() :
        mainDetail(k, v, lst)
        
    writeToCSV(lst)
    
    
class RptDefName(Enum):
    RCRP0S103 = (["T3-01", "T3-02", "T3-03"], "1")
    RCRP0S104 = (["T3-01", "T3-02", "T3-03"], "2")
    RCRP0S105 = (["T3-01", "T3-02", "T3-03"], "3")
    RCRP0S106 = (["T3-01", "T3-02", "T3-03"], "4")
    RCRP0S203 = (["T3-04", "T3-05", "T3-06"], "1")
    RCRP0S204 = (["T3-04", "T3-05", "T3-06"], "2")
    RCRP0S205 = (["T3-04", "T3-05", "T3-06"], "3")
    RCRP0S206 = (["T3-04", "T3-05", "T3-06"], "4")
    RCRP0S303 = (["T3-07", "T3-08", "T3-09"], "1")
    RCRP0S304 = (["T4-07", "T4-08", "T4-09"], "2")
    RCRP0S305 = (["T5-07", "T5-08", "T5-09"], "3")
    RCRP0S306 = (["T6-07", "T6-08", "T6-09"], "4")
    RCRP0S403 = (["T3-10", "T3-11", "T3-12"], "1")
    RCRP0S404 = (["T4-10", "T4-11", "T4-12"], "2")
    RCRP0S405 = (["T5-10", "T5-11", "T5-12"], "3")
    RCRP0S406 = (["T6-10", "T6-11", "T6-12"], "4")
    def __init__(self, arry, stype):
        self.arry = arry
        self.stype = stype
        
        
def mainDetail(tx, filePath, lst):
    rpt = RptDefName[Path(filePath).name.replace(".xlsx", "")]
    print(rpt)
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)
    for name in rpt.arry :
        sheet = wb.get_sheet_by_name(name)
        for i, row in enumerate(sheet, 1):
#             print("process row ", i)
            region = excelUtil.getCellValue("a", row)
            if i >= 11 and stringUtil.isNotBlank(str(region)) :
                try:
                    lst.append(CellDef(row, name, rpt.stype))
                except Exception as ex :
                    errorHandler.printStackTrace()
                    
                    
def rnd(strVal):
    return numberUtil.roundHalfUp4(strVal, 5)


if __name__ == '__main__':
    map = dict()
    map['1'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S103.xlsx"
    map['2'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S104.xlsx"
    map['3'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S105.xlsx"
    map['4'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S106.xlsx"
    map['5'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S203.xlsx"
    map['6'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S204.xlsx"
    map['7'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S205.xlsx"
    map['8'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S206.xlsx"
    map['9'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S303.xlsx"
    map['10'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S304.xlsx"
    map['11'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S305.xlsx"
    map['12'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S306.xlsx"
    map['13'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S403.xlsx"
    map['14'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S404.xlsx"
    map['15'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S405.xlsx"
    map['16'] = "C:/Users/janna/Desktop/季刊範本檔-20171108/變動率改為增減數/RCRP0S406.xlsx"
    main(map)
    print("done...")
