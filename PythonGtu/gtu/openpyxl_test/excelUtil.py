import math

import openpyxl

'''
from gtu.openpyxl_test import excelUtil
'''


def cellEnglishToPos_toInt(column):
    '''用英文欄位取得相對row的index'''
    column = column.upper().strip()
    total = 0;
    _length = len(column)
    for i, c in enumerate(column, 0):
        total += (ord(c) - 64) * pow(26, _length - i - 1)
    total -= 1
    return total


'''
columnIndex 從1起算
'''
def cellEnglishToPos_toStr(columnIndex):
    '''取回excel的column名'''
    map = {}
    tmpColumn = columnIndex
    while True:
        exponent = 0
        for i in range(0, 1000):
            if math.pow(26, i) > tmpColumn:
                exponent = i - 1
                break
        tmpColumn -= math.pow(26, exponent)
        value = 0
        if map.__contains__(exponent):
            value = map[exponent]
        value += 1
        map[exponent] = value
        if exponent == 0:
            map[0] = tmpColumn + 1
            break
    rtnVal = ""
    keyLst = list(sorted(map.keys()))
    for i, k in enumerate(keyLst, 0):
        rtnVal = chr(int(map[k]) + 64) + rtnVal
    return rtnVal
    

def debugShowData(sheetName, filePath):
    wb = openpyxl.load_workbook(filePath, 'r', data_only=True)  # data_only=True會讀到真的值而不是func
    sheet = wb.get_sheet_by_name(sheetName)
    for rowNum, row in enumerate(sheet, 0):
        str2 = "row" + str(rowNum)
        for cellNum, cell in enumerate(row, 0):
            tmpVal = str(cell.value)
            tmpVal = tmpVal.replace('\n', '')
            tmpVal = tmpVal.replace('\r', '')
            str2 += "[" + str(cellNum) + "] " + tmpVal
        print(str2)
        

def getCellValue(column, row):
    cell = None
    if not column.isdigit() :
        cell = row[cellEnglishToPos_toInt(column)]
    else:
        cell = row[column]
    if str(type(cell.value)) == "<class 'NoneType'>" :
        return ""
    return str(cell.value)
    
    
if __name__ == '__main__' :
    print(cellEnglishToPos_toStr(3058))
    print(cellEnglishToPos_toStr(1031))
    print(cellEnglishToPos_toStr(1032))
    print(cellEnglishToPos_toStr(1033))
