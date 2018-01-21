
from inspect import getmembers



def checkMembers(testObj):
    '''檢查物件成員'''
    dlist = getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        print(i, mem)
        


if __name__ == '__main__':
    import inspect
    checkMembers(inspect)