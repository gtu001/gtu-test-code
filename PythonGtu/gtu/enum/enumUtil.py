from enum import Enum

from gtu.reflect import checkSelf
from gtu.reflect import classUtil

'''
from gtu.enum import enumUtil
'''

class _ExampleTableRegion(Enum):
    Test1 = ("總計", "GrandTotal")
    Test2 = ("新北市", "NewTaipeiCity")
    Test3 = ("臺北市", "TaipeiCity")
    
    def __init__(self, chs, eng):
        self.chs = chs
        self.eng = eng
        
    @classmethod
#     @staticmethod
    def loopAll(clz):  
        for i, name in enumerate(_ExampleTableRegion.__members__, 0):
            e = _ExampleTableRegion[name]
            print("loopAll", i, e)
            ''' do something'''
        



class EnumHelper():
    def __init__(self, packageClassPath):
        (importScript, className) = classUtil.getImportScript(packageClassPath)
        exec(importScript)
        self.importScript = importScript
        self.enumClass = eval(className)


    ''' 取得所屬 index  TODO 9'''
    def ordinal(self, enumObj):
        enumMembers = self.enumClass.__getattribute__(self.enumClass, "__members__")
        for i, name in enumerate(enumMembers, 0):
            if enumObj.name == name:
                return i
        raise ValueError(self.enumClass + " : 查無此物件index : " + enumObj)
    
    
    def __callFuncDefault(self, i, mem):
            print(i, mem)
            return (False, None)
    
    
    ''' 迴圈比對 '''
    def loopAll(self, callFunc=None):
        exec(self.importScript) #因為有 --> eval("{0}['{1}']") 會需要再重新import
        
        if callFunc is None :
            callFunc = self.__callFuncDefault
        
        enumMembers = self.enumClass.__getattribute__(self.enumClass, "__members__")
        clzName = self.enumClass.__name__
        for i, name in enumerate(enumMembers, 0):
            member = eval("{0}['{1}']".format(clzName, name))
            (result, rtnObj) = callFunc(i, member)
            if result :
                break;
        return rtnObj

            
if __name__ == '__main__' :
#     globals().update(__ExampleTableRegion.__members__)

    enumHelper = EnumHelper("gtu.enum.enumUtil._ExampleTableRegion")
    print("ordinal = ", enumHelper.ordinal(_ExampleTableRegion.Test2))
    enumHelper.loopAll(None)
    
    print("done..")
    
