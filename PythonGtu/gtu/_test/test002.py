from enum import Enum
import re

from gtu.enum import enumUtil
from gtu.io import fileUtil
from gtu.reflect import checkSelf
from gtu.regex import regexUtil
from gtu.string import stringUtil


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

    def __init__(self):
        self.lst = list()
        
    def __repr__(self):
        strContent = ""
        for (i, line) in enumerate(self.lst):
            strContent += "{0} : {1}\n".format(i, line)
        return strContent
             

def getPdfTable(textContent):
    lst = list()
    tmpPage = PdfOnePage()
    startLineNumber = -1
    for (i, line) in enumerate(textContent.splitlines()):
        ''' for debug '''
#         if (i + 1) == 234 :
#             print("--------------", isEndOfTable(line))
        
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
    return lst


def main(file):
    textContent = fileUtil.loadFile(file)
    
    lst = getPdfTable(textContent)
                
    for (i, row) in enumerate(lst):
        print(i, row)
        
        


if __name__ == '__main__' :
    file = "c:/Users/gtu00/OneDrive/Desktop/秀娟0501/RCRP0S102.pdf.txt"
    main(file)

#     checkSelf.checkMembers("")

    print("done..")
