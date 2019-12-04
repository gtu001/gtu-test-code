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
from gtu.net import simple_request_handler_001


log = LogWriter.LogWriter()

class MyGoogleSearchFetcher(Thread, metaclass=ABCMeta) :
    baseURL = "https://www.google.com"

    def __init__(self) :
        super().__init__()
        self.queue = queue_test_001.DequeObj()
        self.driver = seleniumUtil.getDriver()
        self.start()

    def reflashName(self) :
        newName = "".format(\
            ""
            )
        # self.setName(newName)

    def searchInput(self, sourceStr) :
        self.driver.get(MyGoogleSearchFetcher.baseURL)
        textInput = self.driver.find_element_by_css_selector("input[name=q]")
        textInput.send_keys(sourceStr)
        seleniumUtil.WebElementControl.enter(textInput)
        try :
            fixWordNode = self.driver.find_element_by_xpath("//span[contains(text(),'目前顯示的是以下字詞的搜尋結果：')]/following-sibling::a")
            return fixWordNode.text
        except Exception as ex :
            errorHandler.printStackTrace()
        try :
            fixWordNode = self.driver.find_element_by_xpath("//span[contains(text(),'您是不是要查：')]/following-sibling::a")
            return fixWordNode.text
        except Exception as ex2 :
            errorHandler.printStackTrace()
        try :
            targetLst = self.driver.find_elements_by_css_selector("pre.tw-data-text.tw-text-large.tw-ta")
            sb = ""
            for i,v in enumerate(targetLst) :
                sb += v.text.strip()
            return sb
        except Exception as ex :
            errorHandler.printStackTrace()
        return ""
         
    def run(self) :
        # bs(self.driver.page_source, "html.parser")
        while True :
            if self.queue.length() > 0 :
                sourceStr = self.queue.popright()
                _sourceStr = self.beforeProcess(sourceStr)
                resultStr = self.searchInput(_sourceStr)
                self.myProcess(sourceStr, resultStr)
            self.reflashName()
            time.sleep(0.2)

    @abstractmethod
    def beforeProcess(self, sourceStr) :
        pass

    @abstractmethod
    def myProcess(self, sourceStr, resultStr) :
        pass



class MyGoogleSearchFetcherImpl(MyGoogleSearchFetcher) :
    def beforeProcess(self, sourceStr) :
        return sourceStr

    def myProcess(self, sourceStr, resultStr) :
        print("\t", sourceStr, "\t->\t", resultStr)
        strVal = "{sourceStr}\t{resultStr}".format(sourceStr=sourceStr,resultStr=resultStr)
        log.write(strVal + "\r\n")


# THREAD = MyGoogleSearchFetcherImpl()



def main() :
    #lst = fileUtil.loadFile_asList(fileUtil.getDesktopDir(fileName="GoogleWord.txt"))
    #for i,v in enumerate(lst) :
    #    THREAD.queue.appendleft(v)
    #pass

    driver = seleniumUtil.getDriver()
    driver.get("https://www.instagram.com/p/B4to46agwtx/")
    
    #fileUtil.saveToFile(fileUtil.getDesktopDir("xxxxxxx.htm"), driver.page_source, "UTF8")

    picLst = list()

    soup = bs(driver.page_source, "html.parser")
    imgs = soup.select("img[class][alt][src][srcset]")

    for i,v in enumerate(imgs) :
        clzVal = v.get("class")[0]
        mth = re.search(r"[A-Z0-9]+", clzVal)
        if mth :
            picLink = v.get("src")
            picLst.append(picLink)

            filepath = fileUtil.getDesktopDir(getPicName(picLink))
            simple_request_handler_001.doDownload(picLink, filepath)


def getPicName(picLink) :
    mth = re.search(r"\w+\.jpg", picLink)
    if mth :
        return mth[0]
    raise Exception("無法取得picName : " + picLink)



if __name__ == '__main__' :
    main()
    # network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a