
import os
'''
from gtu.reflect import classUtil
'''


def fullname(o):
    module = o.__class__.__module__
    if module is None or module == str.__class__.__module__:
        return o.__class__.__name__
    return module + '.' + o.__class__.__name__


'''
取得動態import script
要做兩件事
    1: exec(arg[0])
    2. eval(arg[1])
'''
def getImportScript(packageClassPath):
    fromVal = packageClassPath[0: packageClassPath.rfind(".")]
    importVal = packageClassPath[packageClassPath.rfind(".") + 1:]
    importScript = "from " + fromVal + " import " + importVal
    return (importScript, importVal)


if __name__ == '__main__' :
    pass