import requests
from bs4 import BeautifulSoup as bs
from selenium import webdriver
from gtu.reflect import checkSelf

from selenium.webdriver.remote.webdriver import WebDriver as WD
from gtu.web_crawler.selenium_test import seleniumUtil
from threading import Thread
from gtu.net import ssl_util

from gtu.util import queue_test_001
from gtu.string import stringUtil

import sys
import time
import re
from enum import Enum
from gtu.io import fileUtil
from gtu.error import errorHandler

from abc import ABCMeta, abstractmethod

from gtu.io import LogWriter

log = LogWriter.LogWriter()

class MyGoogleTranslateFetcher(Thread, metaclass=ABCMeta) :
    baseURL = "https://translate.google.com.tw/?sxsrf=ACYBGNSXtWb9L-KnXJftDoKBonTteGPxRA:1570063273809&biw=1372&bih=758&um=1&ie=UTF-8&hl=zh-CN&client=tw-ob#en/zh-TW/"

    def __init__(self) :
        super().__init__()
        self.queue = queue_test_001.DequeObj()
        self.driver = seleniumUtil.getDriver()
        self.driver.get(MyGoogleTranslateFetcher.baseURL)
        self.start()

    def reflashName(self) :
        newName = "".format(\
            ""
            )
        # self.setName(newName)

    def translate(self, sourceStr) :
        textarea = self.driver.find_element_by_css_selector("textarea[id=source]")
        textarea.send_keys(sourceStr)
        targetLst = None
        while targetLst is None or len(targetLst) == 0:
            targetLst = self.driver.find_elements_by_css_selector(".tlid-translation.translation")
            time.sleep(0.2)
        sb = ""
        for i,v in enumerate(targetLst) :
            sb += v.text.strip()
        seleniumUtil.WebElementControl.clearInput(textarea)
        return sb
         
    def run(self) :
        # bs(self.driver.page_source, "html.parser")
        while True :
            if self.queue.length() > 0 :
                sourceStr = self.queue.popleft()
                _sourceStr = self.beforeProcess(sourceStr)
                resultStr = self.translate(_sourceStr)
                self.myProcess(sourceStr, resultStr)
            self.reflashName()
            time.sleep(0.2)

    @abstractmethod
    def beforeProcess(self, sourceStr) :
        pass

    @abstractmethod
    def myProcess(self, sourceStr, resultStr) :
        pass





class MyImplMyGoogleTranslateFetcher(MyGoogleTranslateFetcher) :
    def javaParameterToDbColumn(self, strVal) :
        strVal = strVal.strip()
        length = len(strVal)
        sb = ""
        for i in range(0, length) :
            if strVal[i].islower() and i + 1 < length and strVal[i + 1].isupper() :
                sb += strVal[i].lower() + " "
            elif strVal[i] =="_" :
                sb += " "
            else:
                sb += strVal[i].lower()
        return sb

    def beforeProcess(self, sourceStr) :
        return self.javaParameterToDbColumn(sourceStr)

    def myProcess(self, sourceStr, resultStr) :
        print("\t", sourceStr, "\t->\t", resultStr)
        strVal = "{javaName}\t{type}\t{require}\t{defaultVal}\t{description}".format(javaName=sourceStr,type="字串",require="Y",defaultVal="無",description=resultStr)
        log.write(strVal + "\r\n")


THREAD = MyImplMyGoogleTranslateFetcher()



def main() :
    lst = fileUtil.loadFile_asList("C:/Users/E123474/Desktop/FundQryRoiWebRequestDto.txt")
    for i,v in enumerate(lst) :
        THREAD.queue.appendleft(v)
    pass


if __name__ == '__main__' :
    main()
    # network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a