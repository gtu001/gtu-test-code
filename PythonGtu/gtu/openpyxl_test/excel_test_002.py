
from _sqlite3 import Row
from test.test_logging import LEVEL_RANGE
from unittest.test.testmock.support import is_instance

import openpyxl

from gtu.io import fileUtil
from gtu.number import numberUtil


class LifeCac:
    def __init__(self, age, sex, level, nmx, nqx):
        self.age = age
        self.sex = sex
        self.level = level
        self.nmx = nmx 
        self.nqx = nqx
    def __repr__(self):
        return "age={}, sex={}, level={}, nmx={}, nqx={}".format(self.age, self\
                                                                 .sex, self.level, self.nmx, self.nqx)

def deathRate(d):
    val = (d * 0.004989 + 0) * 1000
    return numberUtil.roundHalfUp(val, 2)
    
def searchMatch(age, sex, nqx1, list):
    listBigger = []
    listSmaller = []
    for i, row in enumerate(list, 0):
        if row.age == age and row.sex == sex:
            if row.nqx >= nqx1:
                listBigger.append(row)
            elif row.nqx <= nqx1:
                listSmaller.append(row)
    sorted(listBigger, key=lambda s1: s1.nqx)
    sorted(listSmaller, key=lambda s1: s1.nqx)
    
    for v in listBigger:
        #print("big",v)
        pass
    for v in listSmaller:
        #print("sma",v)
        pass
    
    rtnVal = {}
    if len(listBigger) > 0:
        rtnVal['small'] = listBigger[-1].level
        #print("small", listBigger[-1])
    if len(listSmaller) > 0:
        rtnVal['big'] = listSmaller[0].level
        #print("big", listSmaller[0])
    #print("searchMatch - ", rtnVal)
    return rtnVal

def ageRangeSet(list2):
    s1 = set()
    for i, row in enumerate(list2, 0):
        try:
            s1.add(int(row.age))
        except Exception as ex:
            #ex.print_stack()
            pass
    sorted(s1)
    l1 = list(s1)
    return l1

def findMatch(age, sex, level, list2):
    for i, row in enumerate(list2, 0):
        if row.age == age and row.sex == sex and row.level == level:
            return row
    return None

def findMatch2(age, sex, levelSmall, levelBigger, list2):
    small = findMatch(age, sex, levelSmall, list2)
    big = findMatch(age, sex, levelBigger, list2)
    if small == None or big == None :
        raise NotFoundError("not found!")
    return small, big
    
class NotFoundError(Exception):
    pass

def calcNmx(small, big, baseSmall, baseBig, dRate):
    val = small.nmx - (small.nmx - big.nmx) * (baseSmall.nmx - dRate) / (baseSmall.nmx - baseBig.nmx)
    return numberUtil.roundHalfUp(val, 2)


def __main__(sex, dRate, nAge):
    wb = openpyxl.load_workbook(fileUtil.getDesktopDir() + "lifeTable.xlsx", 'r')

    sheet = wb.get_sheet_by_name("工作表1")
    
    lst = []
    
    for i, row in enumerate(sheet.__iter__(), 1):
        lst.append(LifeCac(row[0].value, row[1].value, row[2].value, row[3].value, row[4].value))
      
    #年齡階級
    ageSet = ageRangeSet(lst)
    print(ageSet)
      
    nqx1 = deathRate(dRate)
    print("nqx ", nqx1)
      
    matchLevel = {}
    age = int(nAge)
    
    '''
    for i, _age in enumerate(ageSet, 0):
        #print(_age, sex)
        if _age >= 5:
            matchLevel = searchMatch(_age, sex, nqx1, lst)
            if len(matchLevel)==2:
                age = int(_age)
                break
    '''
    
    matchLevel = searchMatch(age, sex, nqx1, lst)
    if len(matchLevel)!=2:
        print("超出範圍!")
        return None, None
    
    print(age, matchLevel)
    
    baseSmall = None
    baseBig = None
    
    for i, _age in enumerate(ageSet, 0):
        if _age >= 5:
            try:
                baseSmall, baseBig = findMatch2(age, sex, matchLevel['small'], matchLevel['big'], lst)
                break
            except NotFoundError as ex :
                continue
    
    small1, big1 = findMatch2(0, sex, matchLevel['small'], matchLevel['big'], lst)
    small2, big2 = findMatch2(1, sex, matchLevel['small'], matchLevel['big'], lst)
    
    finalVal1 = calcNmx(small1, big1, baseSmall, baseBig, dRate)
    finalVal2 = calcNmx(small2, big2, baseSmall, baseBig, dRate)
    
    return finalVal1, finalVal2


print()
finalVal1, finalVal2 = __main__('m', 0.1, 5)
print("性別m, 死亡率0.1")
print("0歲 : ", finalVal1)
print("1-4歲 : ", finalVal2)

print()
finalVal1, finalVal2 = __main__('m', 0.39, 5)
print("性別m, 死亡率0.39")
print("0歲 : ", finalVal1)
print("1-4歲 : ", finalVal2)

print()
finalVal1, finalVal2 = __main__('m', 1.18, 15)
print("性別m, 死亡率1.18")
print("0歲 : ", finalVal1)
print("1-4歲 : ", finalVal2)

print()
finalVal1, finalVal2 = __main__('f', 0.25, 5)
print("0歲 : ", finalVal1)
print("1-4歲 : ", finalVal2)
