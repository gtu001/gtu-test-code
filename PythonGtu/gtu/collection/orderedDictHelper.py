

'''
from gtu.collection import orderedDictHelper
'''

def getKeyByIndex(index, orderedDict) :
    return list(orderedDict.keys())[index]


def get(orderedDict, key=None, index=None) :
    if index != None :
        name1 = list(orderedDict.keys())[index]
    if key != None :
        name1 = key
    return orderedDict.get(name1)



def size(orderedDict) :
    return len(orderedDict.keys())