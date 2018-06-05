

import numpy as np
import pandas as pd
from gtu.io import fileUtil
from gtu.reflect import checkSelf
import os


def loadExcel(filePath):
    sheet_name = ["年中人口"]
    headerRowIndices = [1] 
    indexColIndices = [0, 1]
    names = None  # 若無column title 在此自訂
    dataFrame = pd.read_excel(filePath, sheet_name=sheet_name, header=headerRowIndices, \
                  skiprows=None, \
                  skip_footer=0, index_col=indexColIndices, names=names, \
                  usecols=None, parse_dates=False, date_parser=None, \
                  na_values=None, thousands=None, convert_float=True, \
                  converters=None, dtype=None, true_values=None, \
                  false_values=None, engine=None, squeeze=False)
    return dataFrame


def writeExcel(dataFrame, targetName):
    targetPath = fileUtil.getDesktopDir() + os.sep + targetName
    excelWriter = pd.ExcelWriter(targetPath)
    dataFrame.to_excel(excelWriter, sheet_name='Sheet1', \
             na_rep='', float_format=None, columns=None, \
             header=True, index=True, index_label=None, \
             startrow=0, startcol=0, engine=None, merge_cells=True, \
             encoding=None, inf_rep='inf', verbose=True, freeze_panes=None)


def main(filePath):
    sheetSet = loadExcel(filePath)
    df = sheetSet["年中人口"]
    
    for i in df.index:
        print(df['10~14'][i])

    pass


if __name__ == '__main__' :
    filePath = "E:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/openpyxl_test/ex2/RCRP0S108.xlsx"
    main(filePath)
