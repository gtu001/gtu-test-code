
from gtu.reflect import checkSelf

from threading import Thread

from gtu.util import queue_test_001
from gtu.string import stringUtil

import sys
import time
import re
from enum import Enum
from gtu.io import fileUtil
from gtu.error import errorHandler
from abc import ABCMeta, abstractmethod

from gtu.io import stringIoUtil
from gtu.regex import regexReplace
from gtu.datetime import dateUtil

from gtu.io import LogWriter


def main() :
    strContent = stringIoUtil.multiInput()

    pattern = r'\<.*?\>'
    def repFunc(originVal) :
        return ""

    resultContent = regexReplace.replaceContent(pattern, strContent, repFunc)
    filename = "TagReplace_" + dateUtil.currentDatetime_str() + ".txt"
    log = LogWriter.LogWriter(filename=filename)

    log.write(resultContent)
    print("######################################")
    print(resultContent)
    print("######################################")
    print("filename ", filename)
    log.close()



if __name__ == '__main__' :
    main() 
    print("done..")
