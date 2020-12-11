import numpy as np
import pandas as pd
from gtu.io import fileUtil
from gtu.reflect import checkSelf
import os

from gtu.data_science.pandas import pandasUtil


def main(filePath):

    sheetSet = pandasUtil.loadExcel(filePath, "Sheet", headerRowIndices=[0])
    df = sheetSet["Sheet"]

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

    df2 = df.groupby(['消費明細'])['台幣入帳金額'].agg("sum")

    print(df2)

    pandasUtil.writeExcel(df2, fileUtil.getDesktopDir("firstBankTest001.xlsx"))

    print("done...")


if __name__ == '__main__' :
    filePath = fileUtil.getDesktopDir("firstBankCreditCardCheck_20201211153309.xlsx")
    main(filePath)




