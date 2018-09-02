

import re
import os
from gtu.io import fileUtil


f = open("C:/Users/gtu001/Desktop/xxxxxx.txt", 'r', encoding='UTF-8')

nSet = []

def findLine(line):
    ptn = re.compile("(\w+)DaoFactory")
    findObj = re.search(ptn, line);
    if str(type(findObj)) == "<class '_sre.SRE_Match'>":
        val = findObj.group(1)
        val = parseToTable(val)
        if not nSet.__contains__(val):
            nSet.append(val)
        
def parseToTable(valStr):
    if valStr.lower().startswith("lf"):
        return valStr[0:2] + "_" + valStr[2:]
    else:
        return valStr

for i, line in enumerate(f, 1) :
    findLine(line)
f.close()


f2 = open(fileUtil.getDesktopDir() + "new_xx.txt", 'w', encoding='UTF-8')

for idx, item in enumerate(nSet):
    print(idx, item)
    f2.write(item + os.linesep)
    
f2.close()