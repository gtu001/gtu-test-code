from enum import Enum
import re

from gtu.enum import enumUtil
from gtu.io import fileUtil
from gtu.reflect import checkSelf, toStringUtil
from gtu.regex import regexUtil
from gtu.string import stringUtil
from gtu.enum.enumUtil import EnumHelper
import openpyxl


class __TableRegion(Enum):
    Region1 = ("總計", "GrandTotal")
    Region2 = ("新北市", "NewTaipeiCity")
    Region3 = ("臺北市", "TaipeiCity")
    Region4 = ("桃園市", "TaoyuanCity")
    Region5 = ("臺中市", "TaichungCity")
    Region6 = ("臺南市", "TainanCity")
    Region7 = ("高雄市", "KaohsiungCity")
    Region8 = ("臺灣省", "TaiwanProvince")
    Region9 = ("宜蘭縣", "YilanCounty")
    Region10 = ("新竹縣", "HsinchuCounty")
    Region11 = ("苗栗縣", "MiaoliCounty")
    Region12 = ("彰化縣", "ChanghuaCounty")
    Region13 = ("南投縣", "NantouCounty")
    Region14 = ("雲林縣", "YunlinCounty")
    Region15 = ("嘉義縣", "ChiayiCounty")
    Region16 = ("屏東縣", "PingtungCounty")
    Region17 = ("臺東縣", "TaitungCounty")
    Region18 = ("花蓮縣", "HualienCounty")
    Region19 = ("澎湖縣", "PenghuCounty")
    Region20 = ("基隆市", "KeelungCity")
    Region21 = ("新竹市", "HsinchuCity")
    Region22 = ("嘉義市", "ChiayiCity")
    Region23 = ("福建省", "FuchienProvince")
    Region24 = ("金門縣", "KinmenCounty")
    Region25 = ("連江縣", "LienchiangCounty")
    
    def __init__(self, chs, eng):
        self.chs = chs
        self.eng = eng


def has_english_chinese_blankLine(str):
    if stringUtil.hasChinese(str):
        return True
    if re.match(r"[a-zA-Z]+", str) :
        return True
    if len(str.strip()) == 0 :
        return True
    return False


def isStartLine(str):
    return regexUtil.find("加率‰", False, str)
    
    
def isEndOfTable(line):
    ptn = re.compile(r"[a-zA-Z]+", 0)
    mth = ptn.search(line)
    if mth is not None:
        return True
    return stringUtil.hasChinese(line)

    
class  PdfOnePage :
    STATICID = 65
    
    def __init__(self):
        self.lst = list()
        self.identity = chr(PdfOnePage.STATICID)
        PdfOnePage.STATICID += 1
        
    def __repr__(self):
        return toStringUtil.toString(self)
             

def getPdfTable(textContent):
    lst = list()
    tmpPage = PdfOnePage()
    startLineNumber = -1
    
    '''建立PdgOnePage'''
    for (i, line) in enumerate(textContent.splitlines()):
        if startLineNumber == -1 and isStartLine(line) :
            startLineNumber = i + 1
            print("find start : " , startLineNumber , "\t" , line)
        elif startLineNumber != -1 :
            if isEndOfTable(line):
                lst.append(tmpPage)
                tmpPage = PdfOnePage()
                print("find end : ", (i + 1)  , " \t" , line)
                startLineNumber = -1
            else :
                tmpPage.lst.append(line)
                print("\t\t append : " + line)
                
    '''移除掉空物件'''                

    def chk(row):
        return len(row.lst) != 0

    lst = list(filter(chk, lst))
    return lst


def toRowDataLst(rowStr):
    arry = rowStr.strip().split(" ")
    return arry


def createExcel(lst, targetXls):
    tabRegion = EnumHelper("gtu._test.janna.RCRP0S102.__TableRegion")
    wb = openpyxl.Workbook()
    
    for (i, table) in enumerate(lst):
        sheet = wb.create_sheet("工作表" + str(i + 1))
        for (j, row) in enumerate(table.lst):
            reg = tabRegion.get(j)
            rowArry = toRowDataLst(row)
#             print(">>> ", i, reg.chs, reg.eng, rowArry)
            rowData = list()
            rowData.append(reg.chs) 
            rowData.append(reg.eng) 
            rowData.extend(rowArry)
            sheet.append(rowData)
         
    wb.save(targetXls)


def getTargetXls(file):
    name = "janna_" + fileUtil.getNoSubName(file) + ".xlsx"
    newFile = fileUtil.getDesktopDir() + name
    print("create file = ", newFile)
    return newFile


def main(file):
    textContent = fileUtil.loadFile(file)
    print("textContent size = ", len(textContent))

    lst = getPdfTable(textContent)
    print("pdfTable size = ", len(lst))
    
    targetXls = getTargetXls(file)
    createExcel(lst, targetXls)


if __name__ == '__main__' :
#     file = "c:/Users/gtu00/OneDrive/Desktop/秀娟0501/RCRP0S102.pdf.txt"
#     file = "c:/Users/gtu00/OneDrive/Desktop/秀娟0501/RCRP0S102_107.pdf.txt"
    file = 'c:/Users/gtu00/OneDrive/Desktop/秀娟0501/';
    fileLst = list()
    fileUtil.searchFilefind(file, r".*\.txt", fileLst)

    for (i, f) in enumerate(fileLst) :
        main(f)
    print("done..")

