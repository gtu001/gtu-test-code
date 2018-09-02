from enum import Enum
import random
from gtu.data_science.numpy import numpyUtil
from gtu.reflect import checkSelf
import numpy as np
from gtu.datetime import dateUtil


def __test_upper(lst):
    return [k.upper() for k in lst]


def __test_filter():
    lst = ['a', 'b', 'a1', 'd', 'a3']
    lst2 = filter(lambda x : x.startswith("a"), lst)
    for i in lst2:
        print("result", i)
        
    lst3 = [x for x in lst if x.startswith("a")]
    print(lst3)
    
    
def __test_filter_useFunc():
    lst = list()
    for i in range(0, 10):
        lst.append(random.randint(0, 100))
        
    def check(row):
        return row > 50
        
    lst = list(filter(check, lst))
    print(lst)
    
    
def arraycopy(src, srcPos, dest, destPos, length):
    fromArry = src[srcPos:srcPos + length]
    dest[destPos:destPos + length] = fromArry


def zeroLst(size):
    return np.zeros(size).tolist()


def fullLst(size, fillValue):
    shape = (size)
    fill_value = fillValue
    dtype = numpyUtil.getSimpleDtype(fillValue)
    order = 'C'
    lst = np.full(shape, fill_value, dtype, order).tolist()
    return lst


def toLinkedHashSet(arry):
    nums = [3, 3, 4, 2, 6, 6, 1]
    from collections import OrderedDict
    nums = list(OrderedDict.fromkeys(nums).keys())
    return nums

    
def _test_strArry_to_dateArry(npArry):
    xdate = [dateUtil.getDatetimeByJavaFormat(i, "yyyy-MM-dd") for i in npArry]
    return xdate


    
if __name__ == '__main__' :
#     __test_filter()
#     __test_filter_useFunc()
    print("done...")
    
