
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
import io 
from gtu.regex import regexUtil



def main() :
    strContent = stringIoUtil.multiInput(exitArry=("#exit#"))

    buf = io.StringIO(strContent)

    httpLst = list()

    pattern = re.compile(r"(?P<group_link>https?\:.*)", re.I)

    for (i,line) in enumerate(buf) :
        mth = pattern.search(line)
        if mth :
            link = "{:s}".format(mth.group("group_link"))
            print(i, link)
            httpLst.append(link)


    filename = "LineLinkSearch_" + dateUtil.currentDatetime_str() + ".html"
    log = LogWriter.LogWriter(filename=filename)

    for (i, line) in enumerate(httpLst) :
        log.write("<a href=\"{link}\">{link}</a><br/>".format(link=line))
    
    print("filename ", filename)
    log.close()



if __name__ == '__main__' :
    main() 
    print("done..")
