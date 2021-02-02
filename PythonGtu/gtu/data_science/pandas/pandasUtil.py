import numpy as np
import pandas as pd
from gtu.io import fileUtil
from gtu.reflect import checkSelf
import os
from gtu.collection import orderedDictHelper
from gtu.string import stringUtil
from gtu.io import fileUtil


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


def loadExcel(filePath, sheetName=None, headerRowIndices=[0], indexColIndices=[0], converters=None):
    names = None  # 若無column title 在此自訂
    sheets = pd.read_excel(filePath, sheet_name=sheetName, header=headerRowIndices, \
                  skiprows=None, \
                  skipfooter=0, index_col=indexColIndices, names=names, \
                  usecols=None, parse_dates=False, date_parser=None, \
                  na_values=None, thousands=None, convert_float=True, \
                  converters=converters, dtype=None, true_values=None, \
                  false_values=None, engine=None, squeeze=False)
    return sheets


def getSheetName(index, sheets) :
    return orderedDictHelper.getKeyByIndex(index, sheets)

def loadSheet(sheets, name=None, index=None) :
    return orderedDictHelper.get(sheets, key=name, index=index)
        
def sheetsCount(sheets) :
    return orderedDictHelper.size(sheets)

def seriesToDataFrame(series) :
    return series.to_frame()


class WriteExcelHandler :
    def __init__(self, targetExcelPath=None, name=None) :
        if targetExcelPath is None and stringUtil.isNotBlank(name) :
            targetExcelPath = fileUtil.getDesktopDir(name)
        self.targetExcelPath = targetExcelPath
        self.writer = pd.ExcelWriter(targetExcelPath,
                      date_format='YYYY-MM-DD',
                      datetime_format='YYYY-MM-DD HH:MM:SS')

    def appendSheet(self, sheetName, dataFrame, widthsArry=None) :
        dataFrame.to_excel(self.writer, sheet_name=sheetName, \
                na_rep='', float_format=None, columns=None, \
                header=True, index=True, index_label=None, \
                startrow=0, startcol=0, engine=None, merge_cells=True, \
                encoding=None, inf_rep='inf', verbose=True, freeze_panes=None)
        if widthsArry is not None :
            self.setSheetColumnsWidth(sheetName, widthsArry)

    def setSheetColumnsWidth(self, sheetName, widthsArry) :
        worksheet = self.writer.sheets[sheetName]
        for idx, length in enumerate(widthsArry):  
            worksheet.set_column(idx, idx, length) 

    def save(self) :
        self.writer.save()



def isEmptyDataFrame(df) :
    if df.empty:
        return True
    return False



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


class DataFrameHandler :
    def __init__(self, df=None) :
        self.df = df

    def createEmptyDataFrame(self, columns, indexes=None) :
        df = pd.DataFrame()
        for n in columns :
            df[n] = np.nan
        indexes2 = list()
        if indexes is not None and len(indexes) > 0 :
            indexes2.extend(indexes)
            df.set_index(indexes2)
        self.df = df
        
    def appendDataFrame(self, df2) :
        '''
            最好要有相同的column
        '''
        df3 = df2.copy(deep=True)
        df3 = df3.reset_index()
        self.df = self.df.append(df3)

    def getColumns(self) :
        lst = list()
        # print(df.head())
        for i,v in enumerate(self.df.columns): 
            print(i, v) 
            lst.append(v)
        return lst

    def getDataFrame(self):
        return self.df




if __name__ == '__main__' :
    # checkSelf.checkMembersToHtml(pd)
    print("done...")