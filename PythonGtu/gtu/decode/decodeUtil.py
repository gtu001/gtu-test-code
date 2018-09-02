'''
from gtu.decode import decodeUtil
'''


def decodeChs(byteArray, index):
    return byteArray.decode(getChsCodeType(index))
    
def getChsCodeType(index):
    lst = ["big5hkscs", "cp950"]
    return lst[index]