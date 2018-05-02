from enum import Enum
from gtu.reflect import checkSelf


class ExampleTableRegion(Enum):
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
        
    @staticmethod
    def loopAll():  
        for i, name in enumerate(ExampleTableRegion.__members__, 0):
            e = ExampleTableRegion[name]
            ''' do something'''
        
        
def ordinal(enumObj, enumType):
    enumMembers = enumType.__getattribute__(enumType, "__members__")
    for i, name in enumerate(enumMembers, 0):
        if enumObj.name == name:
            return i
    raise Exception(enumType + " : 查無此物件index : " + enumObj)

            
if __name__ == '__main__' :
#     checkSelf.checkMembers(ExampleTableRegion)
    print(ordinal(ExampleTableRegion.Region12, ExampleTableRegion))
    print("done..")