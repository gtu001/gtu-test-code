
from inspect import getmembers
import inspect
import re

''' import 這行
from gtu.reflect import checkSelf
'''

def checkMembers(testObj, doc=False, ignorePrivate=True, ignoreInherit=True):
    '''檢查物件成員'''
    print("testObj ==> ", type(testObj))
    dlist = inspect.getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        if ignoreInherit and __isInheritMember(mem[0]) :
            continue
        if ignorePrivate and __isPrivateMember(mem[0]) :
            continue
        
        print("<<", i, ">>", mem[0], "\t", mem[1])
        if doc:
            print(inspect.getdoc(mem[1]))
            __printLine()
            


def checkLikeMembers(testObj, name, doc=False, ignorePrivate=True, ignoreInherit=True):
    '''找出接近的成員'''
    print("testObj ==> ", type(testObj))
    findOk = False
    name = name.lower()
    dlist = inspect.getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        if ignoreInherit and __isInheritMember(mem[0]) :
            continue
        if ignorePrivate and __isPrivateMember(mem[0]) :
            continue
        
        if name in mem[0].lower() :
            print("<<", i, ">>", mem[0], "\t", mem[1])
            findOk = True
            if doc:
                print(inspect.getdoc(mem[1]))
                __printLine()
    if findOk == False :
        print("找不到類似成員!")
        
        

def checkMatchPtnMembers(testObj, pattern, doc=False, ignorePrivate=True, ignoreInherit=True):
    '''找出接近的成員'''
    print("testObj ==> ", type(testObj))
    findOk = False
    dlist = inspect.getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        if ignoreInherit and __isInheritMember(mem[0]) :
            continue
        if ignorePrivate and __isPrivateMember(mem[0]) :
            continue
        
        mth = re.match(pattern, mem[0])
        if mth:
            print("<<", i, ">>", mem[0], "\t", mem[1])
            findOk = True
            if doc:
                print(inspect.getdoc(mem[1]))
                __printLine()
    if findOk == False :
        print("找不到類似成員!")
        
        

def document(testObj):
    help(testObj)
    
    

def __printLine():    
    str = ""
    for i in range(0, 200):
        str = str + "-"
    print(str)
        
        

def __isInheritMember(name):
    mth = re.match(r"^__\w+__$", name)
    if mth :
        return True
    return False


def __isPrivateMember(name):
    mth = re.match(r"^_\w+$", name)
    if mth :
        return True
    return False
        
        

if __name__ == '__main__':
    import inspect
    checkMembers(inspect)