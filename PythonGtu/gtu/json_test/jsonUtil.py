import json
import io
from json import JSONEncoder
from json import JSONDecoder
from gtu.reflect import checkSelf
from gtu.io import fileUtil

'''
from gtu.json_test import jsonUtil
'''

class MyEncoder(JSONEncoder):
    def default(self, o):
        return o.__dict__    


def _getOuputConfig() :
    config = { 
        'skipkeys' : True,
        'ensure_ascii' : False,
        'check_circular' : True,
        'allow_nan' : False,
        'indent' : 4,
        'separators' : (', ', ': '),
        'sort_keys' : False,#排序
        'cls' : MyEncoder
    }
    return config


def dumpToFile(dictObj, filePath) :
    fs = open(filePath, "w", encoding='utf8')
    json.dump(dictObj, fs, **_getOuputConfig())
    print("寫json檔:", filePath)


def dumpToStr(dictObj):
    fs = io.StringIO()
    json.dump(dictObj, fs, **_getOuputConfig())
    return fs.getvalue()


def dumpToStr2(dictObj):
    result = json.dumps(dictObj, **_getOuputConfig())
    return result
    

def loadFromStr2(stringify) :
    return json.loads(stringify)


def loadFromStr(stringify) :
    fs = io.StringIO(stringify)
    return json.load(fs)


def dumpObjToStr(obj):
    return MyEncoder().encode(obj)


def loadDictToObj(dictObj, clz) :
    o = clz()
    for k, v in dictObj.items() :
        if o.__dict__.__contains__(k) :
            o.__setattr__(k, v)
    return o


def loadStrToObj(stringify, clz) :
    def myLoadStrToObj(dictObj) :
        return loadDictToObj(dictObj, clz)
    return JSONDecoder(object_hook=myLoadStrToObj).decode(stringify)




if __name__ == '__main__' :
    class TestObj() :
        def __init__(self) :
            self.name = ""
            self.age = ""
            self.qq = ""

    mydict = {
        'name': '骆昊',
        'age': 38,
        'qq': 957658,
        'friends': ['王大锤', '白元芳'],
        'cars': [
            {'brand': 'BYD', 'max_speed': 180},
            {'brand': 'Audi', 'max_speed': 280},
            {'brand': 'Benz', 'max_speed': 320}
        ]
    }
    # stringify = dumpToStr(mydict)
    # testObj = loadFromStr(stringify)
    # print(testObj)

    t = TestObj()
    t.name = "aaa"
    t.age = "222"
    t.qq = "ddfdffdsfdfd"

    t1 = loadDictToObj(t.__dict__, TestObj)
    t1Str = dumpObjToStr(t1)

    t2 = loadStrToObj(t1Str, TestObj)
    print(dumpObjToStr(t2))

    print("done...")