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
from gtu.net import ssl_util
from gtu.io import LogWriter

from gtu.net import simple_request_handler_001


DRIVER = seleniumUtil.getDriver()


# log = LogWriter.LogWriter()





def plus28Login() :
    DRIVER.get("https://www.plus28.com/logging.php?action=login")
    username = DRIVER.find_element_by_css_selector("input[id=username]")
    seleniumUtil.WebElementControl.setValue(username, "gtu001")
    password = DRIVER.find_element_by_css_selector("input[id=password]")
    seleniumUtil.WebElementControl.setValue(password, "123474736")
    okBtn = DRIVER.find_element_by_css_selector("button[name=loginsubmit]")
    okBtn.click()


def plus28Login_forPorn(driver, retry=0) :
    try:
        time.sleep(1)
        driver.get("https://p.plus28.com/forum-717-1.html")
        username = driver.find_element_by_css_selector("input[name=username]")
        seleniumUtil.WebElementControl.setValue(username, "gtu001")
        password = driver.find_element_by_css_selector("input[name=password]")
        seleniumUtil.WebElementControl.setValue(password, "123474736")
        okBtn = driver.find_element_by_css_selector("button[name=loginsubmit]")
        okBtn.click()
    except Exception as ex :
        errorHandler.printStackTrace()
    
    clickTime = 0
    while True:
        try:
            time.sleep(0.5)
            confirmBtn = driver.find_element_by_css_selector("input[name=agreedsubmit]")
            confirmBtn.click()
            clickTime += 1
            if clickTime > 10 :
                break
        except Exception as ex :
            errorHandler.printStackTrace()
            if checkIsReadyListPage(driver) :
                return
    if not checkIsReadyListPage(driver) and retry < 5 :
        retry += 1
        plus28Login_forPorn(driver, retry)


def checkIsReadyListPage(driver) :
    url = seleniumUtil.WebElementControl.getCurrentUrl(driver)
    if "fid=717" in url :
        return True
    return False


class AvClass :
    def __init__(self, title, threadUrl):
        self.title = title
        self.url = "https://p.plus28.com/" + threadUrl
        self.torrentUrl = ""


def plus28ListPage() :
    avLst = list()
    DRIVER.get("https://p.plus28.com/forum-717-1.html")
    soup = bs(DRIVER.page_source, "html.parser")
    tbodies = soup.select("tbody[id^='stickthread_']")
    for i,v in enumerate(tbodies) :
        aa = v.select("span[id^='thread_'] a[href^='thread-']")[0]
        href = aa.get("href")
        title = aa.text
        print(i, href, title)
        avLst.append(AvClass(title, href))
    tbodies = soup.select("tbody[id^='normalthread_']")
    for i,v in enumerate(tbodies) :
        aa = v.select("span[id^='thread_'] a[href^='thread-']")[0]
        href = aa.get("href")
        title = aa.text
        print(i, href, title)
        avLst.append(AvClass(title, href))
    return avLst


class MyPlus28TorrentFetcher(Thread, metaclass=ABCMeta) :
    baseURL = "https://p.plus28.com/"

    def __init__(self) :
        super().__init__()
        self.queue = queue_test_001.DequeObj()
        self.driver = seleniumUtil.getDriver()
        plus28Login_forPorn(self.driver, retry=0)
        self.start()

    def reflashName(self) :
        newName = "".format(\
            ""
            )
        # self.setName(newName)

    def getAttachFileUrl(self, url) :
        print("url", url)
        self.driver.get(url)
        soup = bs(self.driver.page_source, "html.parser")
        aaa = soup.select("div[class='box postattachlist'] a[href^='attachment.php?']")
        if aaa :
            aa = aaa[0]
            attachUrl = aa.get("href")
            if attachUrl :
                return "https://p.plus28.com/" + attachUrl
        raise Exception("Attach File Not Found!")

    def myProcess(self, av) :
        try :
            av.torrentUrl = self.getAttachFileUrl(av.url)
            print("downloadAV", av.torrentUrl)
            simple_request_handler_001.doDownload(av.torrentUrl, fileUtil.getDesktopDir(av.title + ".torrent"))
        except Exception as ex :
            errorHandler.printStackTrace()

    def run(self) :
        # bs(self.driver.page_source, "html.parser")
        while True :
            if self.queue.length() > 0 :
                av = self.queue.popright()
                self.myProcess(av)
            self.reflashName()
            time.sleep(0.2)

    # @abstractmethod
    # def beforeProcess(self, sourceStr) :
    #     pass




THREAD = MyPlus28TorrentFetcher()



# https://p.plus28.com/thread-6833518-1-1.html
# href="attachment.php?aid=2567381&k=dc4c8913b01a3c2040b8543b395a64ab&t=1574066437"




def main() :
    plus28Login_forPorn(DRIVER, retry=0)

    avLst = plus28ListPage()

    for i, av in enumerate(avLst) :
        THREAD.queue.appendleft(av)
    

    
if __name__ == '__main__' :
    main()
    # network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a