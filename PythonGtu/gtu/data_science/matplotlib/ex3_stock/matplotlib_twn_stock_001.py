import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from gtu.io import fileUtil
from gtu.reflect import checkSelf
from gtu.data_science.matplotlib import matplotlibUtil
from gtu.data_science.numpy import numpyUtil
# from matplotlib.finance import candlestick2_ohlc
from mpl_finance import candlestick2_ohlc
import datetime as datetime
from gtu.datetime import dateUtil
import matplotlib.ticker as ticker


'''
https://finance.yahoo.com/quote/%5ETWII/

點 Histroical Data
點 Time Period
點 Apply 
點 Download Data

'''


def main():
    data = pd.read_csv(fileUtil.getCurrentDir() + 'twn_stock_data.csv')
    
    stockingY = np.array(data['Adj Close'])
    stockingX = np.array(data['Date'])
    
    dateArry = getDateNpArry2(stockingX)
    
    opensArry = np.array(data['Open'])
    highsArry = np.array(data['High'])
    lowsArry = np.array(data['Low'])
    closesArry = np.array(data['Close'])
    
    fig, ax = plt.subplots()
    candlestick2_ohlc(ax, opensArry, highsArry, lowsArry, closesArry, width=0.6)
    
    ax.xaxis.set_major_locator(ticker.MaxNLocator(6))
    
    def mydate(x, pos):
        try:
            return dateArry[int(x)]
        except IndexError:
            return ''
    
    ax.xaxis.set_major_formatter(ticker.FuncFormatter(mydate))
    
    fig.autofmt_xdate()
    fig.tight_layout()
    
    plt.show()
    


def getDateNpArry(npArry):
    arry = list()
    for (i, v) in enumerate(npArry) :
        val = dateUtil.getDatetimeByJavaFormat(v, "yyyy-MM-dd", "第" + str(i) + "筆")
        arry.append(val)
    return np.array(arry)



def getDateNpArry2(npArry):
    xdate = [dateUtil.getDatetimeByJavaFormat(i, "yyyy-MM-dd") for i in npArry]
    return xdate



if __name__ == '__main__' :
    main()
    print("done...")

