import numpy as np
import pandas as pd
from gtu.io import fileUtil
from gtu.reflect import checkSelf
import os

'''
from gtu.data_science.pandas import pandasUtil
'''

class Converters :
    def __init__(self) :
        self.map = dict()
    def column_is_str(self, column):
        self.map[column] = 'str'
    def column_is_float64(self, column):
        self.map[column] = np.float64
    def column_is_int32(self, column):
        self.map[column] = np.int32
    def getConverters(self):
        return self.map


def loadExcel(filePath, sheetName, headerRowIndices=[0], indexColIndices=[0], converters=None):
    sheet_name = [sheetName]
    names = None  # 若無column title 在此自訂
    dataFrame = pd.read_excel(filePath, sheet_name=sheet_name, header=headerRowIndices, \
                  skiprows=None, \
                  skip_footer=0, index_col=indexColIndices, names=names, \
                  usecols=None, parse_dates=False, date_parser=None, \
                  na_values=None, thousands=None, convert_float=True, \
                  converters=converters, dtype=None, true_values=None, \
                  false_values=None, engine=None, squeeze=False)
    return dataFrame


def writeExcel(dataFrame, targetExcelPath):
    with pd.ExcelWriter(targetExcelPath,
                      date_format='YYYY-MM-DD',
                      datetime_format='YYYY-MM-DD HH:MM:SS') as writer:
        dataFrame.to_excel(writer, sheet_name='Sheet1', \
                na_rep='', float_format=None, columns=None, \
                header=True, index=True, index_label=None, \
                startrow=0, startcol=0, engine=None, merge_cells=True, \
                encoding=None, inf_rep='inf', verbose=True, freeze_panes=None)
    print("write excel = ", targetExcelPath, fileUtil.exists(targetExcelPath))


def getColumns(df) :
    lst = list()
    # print(df.head())
    for i,v in enumerate(df.columns): 
        print(i, v) 
        lst.append(v)
    return lst


def currencyColumnTransform(column, df) :
    '''
     貨幣欄位去掉逗號
    '''
    def converStrToInteger(x) :
        x = str(x).replace(',', '')
        return float(x)
    df[column] = df[column].transform(converStrToInteger)


def filterOutRowsByValue(column, notInLst, df, ignoreCase=False) :
    '''
    過濾掉Row
    '''
    removeMark = "~!@#_REMOVE_LABEL_#@!~"
    def tryToRemoveMark(val) :
        if ignoreCase :
            val = val.lower()
        for v in notInLst :
            if ignoreCase :
                v = v.lower()
            if v in val :
                return removeMark
        return val
    df[column] = df[column].apply(tryToRemoveMark)
    df = df[df[column] != removeMark]
    return df