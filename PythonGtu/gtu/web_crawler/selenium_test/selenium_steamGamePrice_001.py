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
import urllib.parse
import requests

from gtu.io import LogWriter
from selenium.webdriver.support.wait import WebDriverWait
from gtu.reflect import toStringUtil



log = LogWriter.LogWriter(filename="wishList.csv", mode="w")


class SteamGameEasyModeFetcher() :
    def __init__(self, nameLst, mMySteamWishListFetcher) :
        self.wishLst = list()
        self.games = list()
        self.mMySteamWishListFetcher = mMySteamWishListFetcher

        for i, name in enumerate(nameLst) :
            if name not in self.games :
                d = dict()
                d['name'] = name
                d['price'] = -1
                print("game", d)
                self.wishLst.append(d)

                # 寫入檔案
                self.mMySteamWishListFetcher.queue.appendleft(d)

                self.games.append(name) 


class SteamWishListFetcher() :
    def __init__(self, user_account, password, mMySteamWishListFetcher) :
        self.driver = seleniumUtil.getDriver()
        self.baseURL = "https://store.steampowered.com/wishlist/id/{user_account}/#sort=discount&type=all&platform_windows=1".format(user_account=urllib.parse.quote_plus(user_account))
        self.wishLst = list()
        self.mMySteamWishListFetcher = mMySteamWishListFetcher

        # bs(self.driver.page_source, "html.parser")
        self.driver.get(self.baseURL)

        # login
        self.driver.find_element_by_css_selector("a.global_action_link").click()
        username = self.driver.find_element_by_css_selector("input[name=username]")
        password1 = self.driver.find_element_by_css_selector("input[name=password]")
        username.send_keys(user_account)
        password1.send_keys(password)
        submitButton = self.driver.find_element_by_css_selector("button[type=submit]")
        submitButton.click()

        print("檢查碼 Start ################################################")

        time.sleep(1)
        def waitUntil(driver) :
            try :
                btnExists = driver.find_element_by_css_selector("div[data-modalstate='submit']")
                print("btnExists", btnExists)
                return False
            except Exception as ex :
                return True
        WebDriverWait(self.driver, 200, 1).until(waitUntil)

        print("檢查碼 End   ################################################")

        self.driver.get(self.baseURL)
        time.sleep(1)

        self.games = list()

        seleniumUtil.ScrollHandler.scroll2Buttom_Repeat(self.driver, smooth=True, scrollSize=10, pageChangeListener=self.pageChangeListenerFunc)


    def pageChangeListenerFunc(self, driver) :
        webElements = self.driver.find_elements_by_css_selector("div.wishlist_row")
        print("len - webElements= ", len(webElements))

        for i,v in enumerate(webElements) :
            try :
                html = seleniumUtil.WebElementControl.getHtml(v)
                soup = bs(html, "html.parser")

                gameName = soup.select("a.title")[0].getText().strip()
                currentPrice = ""
                try :
                    currentPrice = soup.select("div.discount_final_price")[0].getText().strip()
                except Exception as ex :
                    pass

                if gameName not in self.games :
                    d = dict()
                    d['name'] = gameName
                    d['price'] = currentPrice
                    print("game", d)
                    self.wishLst.append(d)

                    # 寫入檔案
                    self.mMySteamWishListFetcher.queue.appendleft(d)

                    self.games.append(gameName) 
            except Exception as ex2 : 
                pass        



class MySteamWishListFetcher(Thread, metaclass=ABCMeta) :
    def __init__(self) :
        super().__init__()
        self.queue = queue_test_001.DequeObj()
        self.start()

    def reflashName(self) :
        newName = "".format(\
            ""
            )
        # self.setName(newName)
         
    def run(self) :

        log.writeline("名稱\t現在價格\t現在最低店家\t現在最低價\t現在折扣\t歷史最低店家\t歷史最低價\t歷史最低折扣")
        while True :
            if self.queue.length() > 0 :
                d = self.queue.popright()
                g = CheckSteamGame(d)
                log.writeline(g)
            time.sleep(0.2)


class CheckSteamGame :
    def __init__(self, dictObj) :
        gameName = dictObj['name']

        baseURL = "https://isthereanydeal.com/search/?q={game}".format(game=urllib.parse.quote_plus(gameName))
        resp = requests.get(baseURL, verify=False)
        soup = bs(resp.text, "html.parser")
        
        self.currentPrice = dictObj['price']

        self.name = gameName
        self.history = dict()
        self.bestprice = dict()
        try :
            card = soup.select("div.card__content")[0]
            self.name = self.getGameName(card, gameName)
            self.history = self.findInfo(card, 1)
            self.bestprice = self.findInfo(card, 2)
        except Exception as ex :
            pass

    def getGameName(self, card, defaultGameName) :
        try:
            return card.select("div.card__head.card__row a.card__title")[0].getText()
        except Exception as ex :
            return defaultGameName


    def fixCsvString(self, strVal):
        strVal = strVal.strip()
        strVal = re.sub(r"[\t]+", " ", strVal)
        strVal = re.sub(r"[,]+", "", strVal)
        return strVal

        
    def findInfo(self, card, index) :
        rtnObj = dict()
        try :
            card = card.select("div.card__row")[index]
        except Exception as ex :
            pass
        try:
            rtnObj['price'] = self.fixCsvString(card.select("div.numtag__primary")[0].getText())
        except Exception as ex :
            rtnObj['price'] = "ERR"
        try:
            rtnObj['percent'] = self.fixCsvString(card.select("div.numtag__second")[0].getText())
        except Exception as ex :
            rtnObj['percent'] = "ERR"
        try:
            rtnObj['store'] = self.fixCsvString(card.select("span.shopTitle")[0].getText())
        except Exception as ex :
            rtnObj['store'] = "ERR"
        return rtnObj
    

    def __str__(self) :
        def g(map, key) :
            if key in map :
                return map[key]
            return ""

        return "{name}\t{currentPrice}\t{currentLowStore}\t{currentLow}\t{currentLowPercent}\t{historyLowStore}\t{historyLow}\t{historyLowPercent}"\
                .format(name=self.name, currentPrice=self.currentPrice, \
                    currentLowStore=g(self.bestprice, 'store'),currentLow=g(self.bestprice, 'price'),currentLowPercent=g(self.bestprice, 'percent'), \
                    historyLowStore=g(self.history, 'store'),historyLow=g(self.history, 'price'),historyLowPercent=g(self.history, 'percent'), \
                )


def main() :
    THREAD = MySteamWishListFetcher()
    # SteamWishListFetcher("gtu001", "XXXXXXXXXXXXXXXXXXX", THREAD)

    lst = list()
    lst.append("Elite: Dangerous")
    lst.append("Grand Theft Auto V")
    lst.append("Don't Knock Twice")
    lst.append("侠客风云传(Tale of Wuxia)")
    lst.append("Kingdom Come: Deliverance")
    lst.append("ARK Park")
    lst.append("DOOM VFR")
    lst.append("ProjectM : Daydream")
    lst.append("FlyInside Flight Simulator")
    lst.append("VR Paradise")
    lst.append("Catch & release")
    lst.append("Ultimate Fishing Simulator VR")
    SteamGameEasyModeFetcher(lst, THREAD)
    pass

if __name__ == '__main__' :
    main()
    print("done...")
    # network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a




    