
from inspect import getmembers
import inspect
import re

''' import 這行
from gtu.reflect import checkSelf
'''

def checkMembers(testObj, doc=False):
    '''檢查物件成員'''
    print("testObj ==> ", type(testObj))
    dlist = inspect.getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        print("<<", i, ">>", mem[0], "\t", mem[1])
        if doc:
            print(inspect.getdoc(mem[1]))
            

def checkLikeMembers(testObj, name, doc=False):
    '''找出接近的成員'''
    print("testObj ==> ", type(testObj))
    findOk = False
    name = name.lower()
    dlist = inspect.getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        if name in mem[0].lower() :
            print("like --> ", mem[0], "\t", mem[1])
            findOk = True
            if doc:
                print(inspect.getdoc(mem[1]))
    if findOk == False :
        print("找不到類似成員!")
        

def document(testObj):
    help(testObj)
    

if __name__ == '__main__':
    import inspect
    checkMembers(inspect)