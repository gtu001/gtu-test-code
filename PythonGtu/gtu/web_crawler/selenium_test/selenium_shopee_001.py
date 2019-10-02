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



class LogWriter :
    def __init__(self) :
        self.fs = open(fileUtil.getDesktopDir("nintendo_switch_files.txt"), 'a', encoding='UTF8', buffering=30)
    def write(self, line):
        self.fs.write(str(line))
        self.fs.flush()
    def close(self) :
        self.fs.flush()
        self.fs.close()


log = LogWriter()

class NintendoSwitchPatternEnum(Enum) :
    T1=("XAJ", "100001", "100309", 100)
    T2=("XAJ", "700001", "700435", 100)
    T3=("XAW", "400001", "400123", 100)
    T4=("XAJ", "400001", "400530", 100)
    T5=("XAW", "100501", "100793", 100)
    T6=("XAW", "700001", "700173", 100)
    T7=("XAK", "100000", "100053", 100)

    def __init__(self, prefix, startNum, endNum, percent):
        self.prefix = prefix
        self.startNum = startNum
        self.endNum = endNum
        self.percent = percent
        self._startNum = int(startNum)
        self._endNum = int(endNum)

    @staticmethod
    def isCrackable(strVal):
        strVal = stringUtil.trimSpace(strVal)
        for i, name in enumerate(NintendoSwitchPatternEnum.__members__, 0):
            e = NintendoSwitchPatternEnum[name]
            if strVal.startswith(e.prefix) :
                mth = re.search(r"[a-zA-Z]{3}[\s\t]*(\d{1,6})", strVal, re.I)
                if mth :
                    val = int(mth[1])
                    if e._startNum <= val and val <= e._endNum :
                        return e.percent
        return 0


class MyPageInfoFetcher(Thread) :
    matchCount = 0

    def __init__(self) :
        super().__init__()
        self.queue = queue_test_001.DequeObj()
        self.driver = seleniumUtil.getDriver()
        self.start()

    def reflashName(self) :
        newName = "all[{all}],queue[{queue}],match[{match}]".format(\
            all=self.queue.allCount(), \
            queue=self.queue.length(), \
            match=MyPageInfoFetcher.matchCount \
            )
        self.setName(newName)
        # print(newName)

    def append(self, v) :
        self.queue.appendright(v)
        

    def getMatchSerialNumber(self, prodInfo) :
        mth = re.findall(r"[a-zA-Z]{3}[\s\t]*\d{6}", prodInfo, flags=re.I)
        if mth :
            for i,v in enumerate(mth) :
                if NintendoSwitchPatternEnum.isCrackable(v) != 0 :
                    return v
        return ""

    def getPageInfo(self, v) :
        self.driver.get(v.href)
        soup = bs(self.driver.page_source, "html.parser")
        seleniumUtil.ScrollHandler.scroll2Button(self.driver)
        prodinfo = soup.select("div.product-detail.page-product__detail")
        if len(prodinfo) != 0 :
            v.prodInfo = prodinfo[0].getText().strip()
            self.checkProdInfo(v)
            

    def checkProdInfo(self, v) :
        matchSerialNo = self.getMatchSerialNumber(v.prodInfo)
        if stringUtil.isNotBlank(matchSerialNo) :
            v.matchSerialNo = matchSerialNo
            MyPageInfoFetcher.matchCount += 1
            log.write(v)
        

    def run(self) :
        while True :
            if self.queue.length() > 0 :
                v = self.queue.popleft()
                self.getPageInfo(v)
            self.reflashName()
            # time.sleep(0.2)



THREAD = MyPageInfoFetcher()


class ShopeeItem :
    baseUrl = "https://shopee.tw/"

    def __init__(self, v) :
        try :
            self.href = ShopeeItem.baseUrl + v.select("[data-sqe='link']")[0].get("href")
            self.name = v.select("[data-sqe='name']")[0].getText().strip()
            self.priceRange = v.select("[data-sqe='name']")[0].find_next_siblings('div')[0].getText().strip()
            self.prodInfo = None
            self.matchSerialNo = None
            self.isInitOk = True
            t1 = Thread(target=self.getProdInfo, args=(self, ))
            t1.start()
        except Exception as ex :
            errorHandler.printStackTrace2(ex)
            print("nextPageClick ERROR ")
            self.isInitOk = False

    # @classmethod
    def getProdInfo(self, v):
        THREAD.append(v)

    def __str__(self) :
        return "============================================== \n Name : {name} \n Price : {price} \n Link : {href} \n Detail : {prodInfo} \n".format(name=self.name, price=self.priceRange, href=self.href, prodInfo=self.prodInfo)
        

def nextPageClick(driver) :
    try:
        btn = driver.find_element_by_css_selector("button.shopee-button-solid.shopee-button-solid--primary ~ button")
        btn.click()
    except Exception as ex :
        errorHandler.printStackTrace2(ex)
        print("nextPageClick ERROR ")
        time.sleep(0.5)


def main() :
    driver = seleniumUtil.getDriver(1280, 720)

    driver.get("https://shopee.tw/search?facet=14602&keyword=switch&page=0&sortBy=relevancy")

    for pageIdx in range(0, 20) :
        seleniumUtil.ScrollHandler.scroll2Buttom_Repeat(driver, smooth=True, scrollCount=2)
        
        soup = bs(driver.page_source, "html.parser")
        
        products = soup.select("div.col-xs-2-4.shopee-search-item-result__item[data-sqe='item']")
 
        for i,v in enumerate(products) :
            ShopeeItem(v)

        nextPageClick(driver)
         
    # log.close()


if __name__ == '__main__' :
    main()
    # network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a