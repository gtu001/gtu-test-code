import time
import inspect
from gtu.reflect import checkSelf
from gtu.reflect import toStringUtil
from gtu.io import fileUtil


'''
from gtu.datetime import duringMointer
'''

def currentTimeMillis() :
    return int(round(time.time() * 1000))


class During :
    def __init__(self, file):
        self.file = file
        self.startTime = currentTimeMillis()
        self.endTime = -1
        self.startStack = getCurrentStack(self.file)
        self.endStack = None
        self.duringTime = -1

    def count(self, file=None) :
        if not file :
            file = self.file
        self.endTime = currentTimeMillis()
        self.endStack = getCurrentStack(file)
        self.duringTime = self.endTime - self.startTime

    def __str__(self):
        return "{filename}-{method}:({startLine} - {endLine}):耗時:{duringTime}".format(
            filename=self.startStack.filename,
            method=self.startStack.line, 
            startLine=self.startStack.lineNumber, endLine=self.endStack.lineNumber, duringTime=self.duringTime)


def getCurrentStack(file) :
    lst = list()
    for i,v in enumerate(inspect.stack()):
        s = MyStackInfo(v)
        if s.file == file and file != __file__ :
            lst.append(s)

    for i,v in enumerate(lst):
        pass
        # print("---->", i , v)

    if len(lst) != 1:
        return lst[0]    
    else:
        return MyStackInfo(("NA", "NA", -1, "NA", "NA"))
    


def printCurrentStack() :
    lst = list()
    for i,v in enumerate(inspect.stack()):
        s = MyStackInfo(v)
        lst.append(s)
    print("stack - start ---------------------------------------------------")
    for i,v in enumerate(lst):
        print(i , v.filename, v.lineNumber, "\t", v.line)
    print("stack - end   ---------------------------------------------------")


class MyStackInfo:
    def __init__(self, stack) :
        self.allLine = stack[0]
        self.file = stack[1]
        self.filename = fileUtil.getName(self.file)
        self.lineNumber = int(stack[2])
        self.object = stack[3]
        self.line = stack[4]
        self.unknowedNumber = int(stack[5])

    def __str__(self):
        return toStringUtil.toString(self, limitSize=50)

if __name__ == '__main__' :
    d1 = During(__file__)
    d1.count()
    print(d1)
    print("done...")