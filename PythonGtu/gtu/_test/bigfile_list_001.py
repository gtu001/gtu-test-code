import io
from gtu.io import stringIoUtil
from gtu.string import stringUtil
from gtu.io import LogWriter
from gtu.io import fileUtil
from gtu.collection import listUtil

import os
from os.path import expanduser
from pathlib import Path
from gtu.thread_test import threadUtil
from gtu.reflect import checkSelf
import re
from gtu.string import stringUtil
import sys
import collections
import glob


def getDir(file):
	if Path(file).is_dir():
		return file
	return os.path.dirname(file)


def searchFilefind(file, fileList):
    try :
        file = Path(file)
        currentDir = getDir(file)
        if file.is_dir() :
            listFile = os.listdir(file)
            for i, f in enumerate(listFile, 0):
                f = Path(currentDir , f)
                searchFilefind(f, fileList)
        elif file.is_file() :
            length1 = file.stat().st_size
            if length1 >= 1000000 :
                print(file, str(length1))
    except PermissionError as ex:
        pass


def main() :
    fileLst = list()
    filePath = "c:/"

    print("1---------------------")

    searchFilefind(filePath, fileLst)

    # print("2---------------------")

    # def sortFunc(a, b) :
    #     as1 = fileUtil.length(a)
    #     bs1 = fileUtil.length(b)
    #     val = None
    #     if as1 < bs1 :
    #         val = -1
    #     elif bs1 > as1 :
    #         val = 1
    #     else :
    #         val = 0
    #     val = val * -1
    #     return val

    # print("3---------------------")

    # listUtil.sort(fileLst, sortFunc)

    # print("4---------------------")

    # for i in range(0, 100) :
    #     if len(fileLst) - 1 <= i :
    #         print(fileLst[i])




if __name__ == '__main__' :
    main()
    print("done...")
