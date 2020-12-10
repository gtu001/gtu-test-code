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
from selenium.webdriver.support.ui import WebDriverWait

log = LogWriter.LogWriter()


class FirstBankHandler(Thread, metaclass=ABCMeta) :
    loginURL = "https://ccard.firstbank.com.tw/cmsweb/home/index"

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


    def findButton(self, text) :
        findBtns = self.driver.find_elements_by_css_selector("button")
        for i,v in enumerate(findBtns) :
            if text in v.text :
                return v
        return None


    def login(self) :
        self.driver.get(FirstBankHandler.loginURL)

        txtIDCard = self.driver.find_element_by_css_selector("input[id=txtIDCard]")
        txtUserAcct = self.driver.find_element_by_css_selector("input[id=txtUserAcct]")
        txtPswd = self.driver.find_element_by_css_selector("input[id=txtPswd]")
        _txtPicCode_myPicVerify = self.driver.find_element_by_css_selector("input[id=_txtPicCode_myPicVerify]")

        loginBtn = self.findButton('登入')
        
        seleniumUtil.WebElementControl.setValue(txtIDCard, "E123474736")
        seleniumUtil.WebElementControl.setValue(txtUserAcct, "gtu001")

        def waitUntilFunc(driver) :
            print("waitUntilFunc try...")
            chk1 = stringUtil.isNotBlank(seleniumUtil.WebElementControl.getValue(txtPswd))
            chk2 = len(seleniumUtil.WebElementControl.getValue(_txtPicCode_myPicVerify)) == 4
            print("check11 = " , chk1 , " , ", chk2)
            print("check22 = " , seleniumUtil.WebElementControl.getValue(txtPswd) , " , ", seleniumUtil.WebElementControl.getValue(_txtPicCode_myPicVerify))
            if chk1 and chk2 :
                return True
            return False
        
        seleniumUtil.WebElementControl.until(self.driver, timeout=300, frequence=0.5, message='', waitUntilFunc=waitUntilFunc) 
        print("loginBtn = ", loginBtn)
        seleniumUtil.WebElementControl.click(loginBtn)



    def showBillPage(self) :
        Alink = self.findPageLink("li[class=cms-bg-menu]", '信用卡帳務服務')
        seleniumUtil.WebElementControl.click(Alink)
        Blink = self.findPageLink("li > a > div", '帳單明細查詢╱列印繳款聯')
        seleniumUtil.WebElementControl.click(Blink)
        Clink = self.findPageLink("li[class=active] > a", '台幣')
        pageContent = self.driver.page_source
        print("################# start ")

        trs = self.driver.find_elements_by_xpath("//div[contains(@class,'cms-label')][text()='信用卡消費明細']/following-sibling::table//tbody//tr")
        for i,tr in enumerate(trs) :
            print(i, tr.text)
            tds = tr.find_elements_by_xpath(".//td")
            print("--------------")
            for j,td in enumerate(tds) :
                print(j, td.text)
        
        
         
    def run(self) :
        # bs(self.driver.page_source, "html.parser")
        # while True :
        #     if self.queue.length() > 0 :
        #         sourceStr = self.queue.popright()
        #         _sourceStr = self.beforeProcess(sourceStr)
        #         resultStr = self.searchInput(_sourceStr)
        #         self.myProcess(sourceStr, resultStr)
        #     self.reflashName()
        #     time.sleep(0.2)
        pass




THREAD = FirstBankHandler()




def main() :
    # lst = fileUtil.loadFile_asList(fileUtil.getDesktopDir(fileName="GoogleWord.txt"))
    # for i,v in enumerate(lst) :
    #     THREAD.queue.appendleft(v)
    # pass
    
    THREAD.login()
    THREAD.showBillPage()


if __name__ == '__main__' :
    main()
    # network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a