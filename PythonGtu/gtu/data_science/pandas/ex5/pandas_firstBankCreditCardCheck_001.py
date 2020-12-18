import numpy as np
import pandas as pd
from gtu.io import fileUtil
from gtu.reflect import checkSelf
import os

from gtu.data_science.pandas import pandasUtil
from gtu.string import stringUtil


def dataFrameProcess(df) :
    print(df.head())

    # pandasUtil.getColumns(df)
    
    # for i in df.index:
    #     print(df['消費明細'][i])

    # checkSelf.checkMembersToHtml( df.groupby(['消費明細']))

    print("===============================================================")
    print("===============================================================")
    print("===============================================================")

    pandasUtil.currencyColumnTransform('台幣入帳金額', df)

    def columnCombine(col) : 
        if col.startswith('國外交易手續費') :
            return '國外交易手續費'
        if col.startswith('街口電支') :
            return '街口電支'
        return col

    df['消費明細'] = df['消費明細'].transform(columnCombine)

    
    df = pandasUtil.filterOutRowsByValue('消費明細', ['小計', '記名式悠遊卡卡號', '綠活卡累積減免下次年費金額起算日', '現已累積', '累積消費次數', '您的本期金額總計', '感謝您ｉｂｏｎ繳款已收到'], df)

    df = df.groupby(['消費明細'])['台幣入帳金額'].agg("sum")
    
    dd1 = pandasUtil.seriesToDataFrame(df)

    for i,v in enumerate(dd1) :
        print(i,v)

    print(dd1)

    return dd1



def main(filePath):

    sheetSet = pandasUtil.loadExcel(filePath, headerRowIndices=[0])

    writer = pandasUtil.WriteExcelHandler(name="firstBankCreditCardCheck001.xlsx")

    dfs = pandasUtil.DataFrameHandler()
    dfs.createEmptyDataFrame(['消費明細', '台幣入帳金額'], [])

    widthsArry = [80, 30]


    for i in range(0, pandasUtil.sheetsCount(sheetSet)) :
        df = pandasUtil.loadSheet(sheetSet, index=i)

        sheetName = pandasUtil.getSheetName(i, sheetSet)

        dd1 = dataFrameProcess(df)

        writer.appendSheet(sheetName, dd1, widthsArry=widthsArry)

        dfs.appendDataFrame(dd1)


    dfttt = dfs.getDataFrame()
    
    # ------------------------------ 最後加總 Groupby
    dfttt = dfttt.groupby(['消費明細'])['台幣入帳金額'].agg("sum")
    # ------------------------------ 最後加總 Groupby

    writer.appendSheet("Total", dfttt, widthsArry=widthsArry)

    writer.save()

    print("done...")


if __name__ == '__main__' :
    filePath = fileUtil.getCurrentDir(__file__) + "firstBankCreditCardCheck_20201217133047.xlsx"
    main(filePath)

    # dfs = pd.DataFrame()
    # dfs['消費明細'] = np.nan
    # dfs['台幣入帳金額'] = np.nan
    # dfs.set_index(['消費明細'])

    # df1 = pd.DataFrame({"消費明細":['a','b'], "台幣入帳金額":[333,444]}, index=None)
    # df2 = pd.DataFrame({"消費明細":['a','c'], "台幣入帳金額":[777,888]}, index=None)

    # df3 = df1[:]
    # print("-----", df3)

    # dfs = dfs.append(df1)
    # dfs = dfs.append(df2)

    # print(" ---------------- 0")
    # print(dfs)

    # print(dfs.groupby(['消費明細'])['台幣入帳金額'].agg("sum"))

    print("done...")




