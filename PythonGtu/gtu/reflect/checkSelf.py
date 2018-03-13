
from inspect import getmembers
import inspect
import re

#####from gtu.reflect import checkSelf

def checkMembers(testObj, doc=False):
    '''檢查物件成員'''
    print("self==>", type(testObj))
    dlist = inspect.getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        print("<<", i, ">>", mem[0], mem[1])
        if doc:
            print(inspect.getdoc(mem[1]))
        

def document(testObj):
    help(testObj)
    

if __name__ == '__main__':
    import inspect
    checkMembers(inspect)