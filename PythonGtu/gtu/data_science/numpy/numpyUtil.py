
'''
from gtu.data_science.numpy import numpyUtil
'''

from enum import Enum
import numpy as np
from gtu.string import stringUtil
from gtu.reflect import checkSelf
from gtu.collection import listUtil
from gtu.data_science.matplotlib import matplotlibUtil

  
class Dtype(Enum):
    bool = (['bool'])
    byte = ()
    ubyte = ()
    short = ()
    ushort = ()
    int = (['int'])
    uint = ()
    long = ()
    ulong = ()
    longlong = ()
    ulonglong = ()
    half = ()
    float = (['float'])
    double = ()
    longdouble = ()
    cfloat = ()
    cdouble = ()
    clongdouble = ()
    datetime = ()
    timedelta = ()
    object = (['str'])
    string = ()  # 無法使用 -->改用object
    void = ()
    intp = ()
    uintp = ()
    genbool = ()
    signed = ()
    unsigned = ()
    floating = ()
    complex = ()
    
    def __init__(self, arry=[]):
        self.arry = arry
    
    @classmethod
    def getType(cls, value):
        typeName = type(value).__name__
        for (i, name) in enumerate(Dtype.__members__):
            e = Dtype[name]
            if len(e.arry) != 0:
                if typeName in e.arry :
                    return e
        raise ValueError(stringUtil.concat("找不到符合dtype : ", value))
    
    ''' 檢查 np 裡面定義的 dtype , 但未驗證'''

    @classmethod
    def getDtypeSet(cls):
        import inspect
        lst = list()
        dlist = inspect.getmembers(np)
        for i, mem in enumerate(dlist, 0):
            if type(mem[1]).__name__ == 'type' :
                if mem[1].__module__ == 'numpy':
                    lst.append(mem[0])
        listUtil.toLinkedHashSet(lst)
        return lst


def createEmptyMartix(row, col):
    return np.empty(shape=(row, col), dtype='object')


def __test_reshape_put_into_martix():
    arry1 = createEmptyMartix(5, 5)
    arry2 = np.array([3, 3, 3])
    arry3 = np.reshape(arry2, (3, 1), order='C')
    print(arry3)
    arry1[0:3, 0:1] = arry3
    print(arry1)
    
    
def getSimpleDtype(value):
    return Dtype.getType(value).name


def __test_get_condition_arry() :
    arry = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
    arry[np.where(arry == 5)] = -999
    print(arry.tolist())
    

def transferNoneType(npArry, newValue):
    npArry[np.where(npArry == None)] = newValue   

    
def removeNan(x):
    x[np.logical_not(np.isnan(x))]
    
    
def replaceNan(a, repVal) :
    where_are_NaNs = np.isnan(a)
    a[where_are_NaNs] = repVal
    
    
'''取得邊界'''


def getMaxRowCol(dataArry):
    maxRowIndex = -1
    maxColIndex = -1
    for (i, row) in enumerate(dataArry):
        for (j, col) in enumerate(row):
            if col is not None:
                maxColIndex = max(maxColIndex, j)
        for (j, col) in enumerate(row):
            if col is not None:
                maxRowIndex = max(maxRowIndex, i)
                break
    return (maxRowIndex, maxColIndex)
    

'''
取得開始與結束兩值間切N等分的array
'''


def getEvenArry(start, end, num):
    obj = np.linspace(start, end, num=num, endpoint=True, retstep=True, dtype=None)
    arry = obj[0].tolist()
    stepVal = float(obj[1])
    return (arry, stepVal)


'''
取得隨機陣列
'''


def randomArry(min, max, size):
    x3 = np.random.randint(min, high=max, size=size)
    return x3


'''
檢視npArry
'''


def inspectNpArry(npArry):
    print("維度: ", npArry.ndim)
    print("shape:", npArry.shape)
    print("size: ", npArry.size)
    print("dtype:", npArry.dtype)
    print("itemsize:", npArry.itemsize, "bytes")
    print("nbytes:", npArry.nbytes, "bytes")


'''
建立arry -> for i in range(start, stop, step)
'''


def arangeArry(start, stop, step):
    return np.arange(start, stop, step)


'''
串聯多的arry
Ex :
    ary1 = np.full((2,3), 1)
    ary2 = np.full((1,3), 2)
    print(concatenate([ary1, ary2]))
'''


def concatenate(arryArry):
    return np.concatenate(arryArry)


'''
水平或垂直疊加
'''


def stack(arryArry, orient):
    if orient == 'v' :
        return np.vstack(arryArry)
    elif orient == 'h' :
        return np.hstack(arryArry)
    raise ValueError("orign 必須維 v or h : " + str(orient))
    

'''
垂直水平切割 
unit為己等分
'''


def split(arry, orient, unit):
    if orient == 'v' :
        upper, lower = np.vsplit(arry, [unit])
        return (upper, lower)
    elif orient == 'h' :
        left, right = np.hsplit(arry, [unit])
        return (left, right)
    
    
def inspectAggregation(npArry):
    print("sum = " , np.sum (npArry))  # 
    print("nansum = " , np.nansum (npArry))  # Compute sum of elements
    print("prod = " , np.prod (npArry))  # 
    print("nanprod = " , np.nanprod (npArry))  # Compute product of elements
    print("mean = " , np.mean (npArry))  # 
    print("nanmean = " , np.nanmean (npArry))  # Compute mean of elements
    print("std = " , np.std (npArry))  # 
    print("nanstd = " , np.nanstd (npArry))  # Compute standard deviation
    print("var = " , np.var (npArry))  # 
    print("nanvar = " , np.nanvar (npArry))  # Compute variance
    print("min = " , np.min (npArry))  # 
    print("nanmin = " , np.nanmin (npArry))  # Find minimum value
    print("max = " , np.max (npArry))  # 
    print("nanmax = " , np.nanmax (npArry))  # Find maximum value
    print("argmin = " , np.argmin (npArry))  # 
    print("nanargmin = " , np.nanargmin (npArry))  # Find index of minimum value
    print("argmax = " , np.argmax (npArry))  # 
    print("nanargmax = " , np.nanargmax (npArry))  # Find index of maximum value
    print("median = " , np.median (npArry))  # 
    print("nanmedian = " , np.nanmedian (npArry))  # Compute median of elements
    print("percentile = " , np.percentile (npArry, 50))  # 
    print("nanpercentile = " , np.nanpercentile (npArry, 50))  # Compute rank-based statistics of elements
    print("any = " , np.any (npArry))  # Evaluate whether any elements are true
    print("all = " , np.all (npArry))  # Evaluate whether all elements are true


if __name__ == '__main__':

    checkSelf.document(np.random.multivariate_normal)


    print("done...")




    



