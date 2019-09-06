

import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import matplotlib.font_manager as mfm
import seaborn; 
import numpy as np
from gtu.reflect import checkSelf
from gtu.os import envUtil

'''
from gtu.data_science.matplotlib import matplotlibUtil
'''


def _showPlotAllFont():
    for (i, val) in enumerate(mfm.findSystemFonts()):
        print(i, val)


def applyChineseFont():
    if envUtil.getOS() == 'Windows' :
        applyChineseFont_win()
    else :
        applyChineseFont_win()
        

def applyChineseFont_win():
    import matplotlib as mpl
    font_name = "simhei"  # STKaiti
    mpl.rcParams['font.family'] = font_name
    mpl.rcParams['axes.unicode_minus'] = False  # in case minus sign is shown as box
    
    
def _test_applyChineseFont_usePath(ttfPath):
    font_path = "/usr/share/fonts/custom/simhei.ttf"
    prop = mfm.FontProperties(fname=font_path)
    plt.text(0.5, 0.5, s=u'测试', fontproperties=prop)
    

def stock(dictVal, title=None, xlabel=None, ylabel=None, seabornStyle=False):
    applyChineseFont()
    if seabornStyle:
        #seaborn.set()  # set plot style
        pass
        
    if 'x' in dictVal :
        plt.plot(dictVal['x'], dictVal['y'])
    elif 'y' in dictVal :
        plt.plot(dictVal['y'])
    
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.show()


def hist(npArry, title=None, xlabel=None, ylabel="occur times", seabornStyle=False):
    applyChineseFont()
    if seabornStyle:
        #seaborn.set()  # set plot style
        pass
        
    plt.hist(npArry)
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.show()
    

def image(npArry, seabornStyle=False):
    applyChineseFont()
    if seabornStyle:
        #seaborn.set()  # set plot style
        pass
        
    plt.imshow(npArry, origin='lower', cmap='viridis')
    plt.colorbar()
    plt.show()
    

def scatter(x, y, seabornStyle=False):
    applyChineseFont()
    if seabornStyle:
        #seaborn.set()  # set plot style
        pass
    plt.scatter(x, y)
    plt.show()


if __name__ == '__main__' :
    
#     plt.xticks(np.arange(min(x), max(x)+1, 5.0))

    print("done...")

