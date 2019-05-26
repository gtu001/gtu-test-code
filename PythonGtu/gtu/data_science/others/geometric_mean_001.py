from enum import Enum
from gtu.reflect import checkSelf
import re
from gtu.io import fileUtil


def geometricMean(lst):
    '''
        幾何平均數計算方式
        Geometric mean
    '''
    total = 1.0
    for (i,v) in enumerate(lst):
        total *= v
    return total ** (1 / len(lst))


if __name__ == '__main__' :
    lst = list()
    lst.append(1)
    lst.append(3)
    lst.append(9)
    lst.append(27)
    lst.append(81)
    lst.append(243)
    lst.append(729)
    print(geometricMean(lst))
    
    lst = list()
    lst.append(1.01)
    lst.append(1.09)
    lst.append(1.06)
    lst.append(1.02)
    lst.append(1.15)
    print(geometricMean(lst))
    print("done...")

