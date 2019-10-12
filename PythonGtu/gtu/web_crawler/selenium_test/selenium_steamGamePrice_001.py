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



log = LogWriter.LogWriter()

class SteamWishListFetcher() :
    def __init__(self, user_account, password) :
        self.driver = seleniumUtil.getDriver()
        self.baseURL = "https://store.steampowered.com/wishlist/id/{user_account}/#sort=discount&type=all&platform_windows=1".format(user_account=urllib.parse.quote_plus(user_account))
        self.wishLst = list()

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

        games = list()

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

                if gameName not in games :
                    d = dict()
                    d['name'] = gameName
                    d['price'] = currentPrice
                    print("game", d)
                    self.wishLst.append(d)
                else :
                    games.append(gameName) 

            except Exception as ex2 : 
                pass


        seleniumUtil.ScrollHandler.scroll2Buttom_Repeat(self.driver, smooth=True, scrollSize=10)




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
        while True :
            if self.queue.length() > 0 :
                d = self.queue.popright()
                g = CheckSteamGame(d['name'])
                log.write(g)
            time.sleep(0.1)


class CheckSteamGame :
    def __init__(self, gameName) :
        baseURL = "https://isthereanydeal.com/search/?q={game}".format(game=urllib.parse.quote_plus(gameName))
        resp = requests.get(baseURL, verify=False)
        soup = bs(resp.text, "html.parser")

        self.name = gameName
        self.history = dict()
        self.bestprice = dict()
        try :
            card = soup.select("div.card__content")[0]
            cardName = card.select(".card__head.card__row")[0].getText()
            historyLow = card.select("div.card__row")[1]
            bestprice = card.select("div.card__row")[2]
            self.name = cardName
            self.history = self.findInfo(historyLow)
            self.bestprice = self.findInfo(bestprice)
        except Exception as ex :
            pass
        
    def findInfo(self, card) :
        rtnObj = dict()
        try:
            rtnObj['price'] = card.select("div.numtag__primary")[0].getText().strip()
        except Exception as ex :
            pass
        try:
            rtnObj['percent'] = card.select("div.numtag__second")[0].getText().strip()
        except Exception as ex :
            pass
        try:
            rtnObj['store'] = card.select("span.shopTitle")[0].getText().strip()
        except Exception as ex :
            pass
        return rtnObj
    
    def __str__(self) :
        return toStringUtil.toString(self)


def main() :
    THREAD = MySteamWishListFetcher()
    wish = SteamWishListFetcher("gtu001", "luv90cxc048c")
    for i,v in enumerate(wish.wishLst):
        gameName = v['name']
        gameSteamPrice = v['price']
        THREAD.queue.appendleft(v)
    pass

if __name__ == '__main__' :
    main()
    # network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a




    