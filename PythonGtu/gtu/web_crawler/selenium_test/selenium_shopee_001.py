import requests
from bs4 import BeautifulSoup as bs
from selenium import webdriver
from gtu.reflect import checkSelf

from selenium.webdriver.remote.webdriver import WebDriver as WD
from gtu.web_crawler.selenium_test import seleniumUtil
from threading import Thread
from gtu.net import ssl_util

import sys




class ShopeeItem :
    baseUrl = "https://shopee.tw/"
    def __init__(self, v) :
        self.href = ShopeeItem.baseUrl + v.select("[data-sqe='link']")[0].get("href")
        self.name = v.select("[data-sqe='name']")[0].getText().strip()
        self.priceRange = v.select("[data-sqe='name']")[0].find_next_siblings('div')[0].getText().strip()
        t1 = Thread(target=self.getProdInfo, args=(self, ))
        t1.start()

    # @classmethod
    def getProdInfo(self, v):
        driver = seleniumUtil.getDriver()
        driver.get(v.href)
        v.prodInfo = driver.select("div.product-detail.page-product__detail")[0].getText().strip()
        print("<<<", v.name, v.prodInfo)
        


def main() :

    driver = seleniumUtil.getDriver(1280, 720)

    driver.get("https://shopee.tw/search?keyword=switch&page=0&sortBy=relevancy&usedItem=true")

    pageContent = dict()

    prodLst = list()

    for page in range(0, 1) :
        seleniumUtil.ScrollHandler.scroll2Buttom_Repeat(driver, smooth=True, scrollCount=2)
        
        soup = bs(driver.page_source, "html.parser")
        
        products = soup.select("div.col-xs-2-4.shopee-search-item-result__item[data-sqe='item']")
 
        for i,v in enumerate(products) :
            item = ShopeeItem(v)
            prodLst.append(item)
            

        # nextBtn = driver.find_elements_by_css_selector(".jcarousel-next.jcarousel-next-horizontal")[0]
        # nextBtn.click()
        # time.sleep(2)




if __name__ == '__main__' :
    main()
    # network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a