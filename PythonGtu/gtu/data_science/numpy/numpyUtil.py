
'''
from gtu.data_science.numpy import numpyUtil
'''

from enum import Enum
import numpy as np
from gtu.string import stringUtil
from gtu.reflect import checkSelf
from gtu.collection import listUtil


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


if __name__ == '__main__':
    
    set = Dtype.getDtypeSet()
    for i in set :
        print(">>", i)
    
    shape = (10)
    fill_value = "XXX"
    dtype = "void"
    order = 'C'
    lst = np.full(shape, fill_value, dtype, order)
    print(lst)

    print("done...")
    
    
