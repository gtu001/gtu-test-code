# -*- coding: utf-8 -*-
import os

import scrapy
from scrapy.crawler import Crawler
from scrapy.crawler import CrawlerProcess
from scrapy.linkextractors import LinkExtractor
# from scrapy.selector.lxmlsel import HtmlXPathSelector
from scrapy.settings import Settings
from scrapy.spiders import CrawlSpider, Rule
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.firefox.firefox_binary import FirefoxBinary
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.support import expected_conditions as ec
from selenium.webdriver.support.wait import WebDriverWait
from twisted.internet import reactor

from gtu._tkinter.util import tkinterUtil
from gtu.collection import listUtil
from gtu.datetime import dateUtil
from gtu.io import fileUtil
from gtu.net import simple_request_handler_001
from gtu.os import envUtil
from gtu.reflect import checkSelf
from gtu.scrapy_test import scrapy_runner
from gtu.scrapy_test import scrapy_util

# https://medium.com/python-pandemonium/develop-your-first-web-crawler-in-python-scrapy-6b2ee4baf954
class Porn91CrawlerSpider(scrapy.Spider):  #   scrapy.Spider    /    CrawlSpider
    name = '91porn_go'
    
    allowed_domains = ['91porn.com'] 
    start_urls = ['http://91porn.com/index.php']
    
    BASE_URL = 'https://91porn.com'
    rules = (
        Rule(LinkExtractor(
                            allow=(),
                            restrict_css=()
                           ),
             callback="process_item",
             follow=False),
        )
    
    def __init__(self):
        self.test = False
        self.driver = scrapy_util.getWebDriver()
  
    def parse(self, response):  # process_item
        print("### MAIN page : ", response.url)
        
        self.driver.get(response.url)
#         selenium.webdriver.firefox.webelement.FirefoxWebElement

        print("-------------------login")
        # self.doLoginForm()

        print("-------------------A")
        self.getClickBtn()
        
        print("-------------------B")
        
        self.driver.close()


    def getMovList(self):
        wait = WebDriverWait(self.driver, 5)
        ulObj = wait.until(ec.presence_of_element_located((By.XPATH, "//ul[contains(@class, 'jcarousel-list')]")) , "failed !!!!")
        liLst = wait.until(ec.presence_of_all_elements_located((By.XPATH, ".//li[position()>=0 and position()<=1000]")) , "failed2 !!!!")
        for (i,v) in enumerate(liLst) :
            print("-----", i, v)
            
    def getClickBtn(self) :
        wait = WebDriverWait(self.driver, 5)
        btns = wait.until(ec.presence_of_all_elements_located((By.XPATH, "//div[contains(@class, 'jcarousel-next') || contains(@class, 'jcarousel-prev')]")) , "failed2 !!!!")
        for (i,v) in enumerate(btns) :
            print("-----", i, v)


    def doLoginForm(self):
        self.driver.get("http://91porn.com/login.php")
        
#       assert "Title" in driver.title
        userName = self.driver.find_element_by_xpath('//input[@name="username"]')
        password = self.driver.find_element_by_xpath('//input[@name="password"]')
        captcha = self.driver.find_element_by_xpath('//input[@name="captcha_input"]')
        
        form = self.driver.find_element_by_xpath('//form[@name="loginForm"]')
        print("userName ", userName)
        print("password ", password)
        print("form ", form)
        
        tkinterUtil.createDefaultWin()
        capthaText = tkinterUtil.prompt("驗證馬", "請輸入驗證馬")
        
        print("capthaText ", capthaText)
        
        if len(capthaText.strip()) != 4 :
            raise Exception("未輸入正確驗證馬")
        
        userName.send_keys("gtu001")
        password.send_keys("123474736eE!")
        captcha.send_keys(capthaText)
        
        form.submit()
        print("doLoginForm done...")
        
            



if __name__ == '__main__' :
    scrapy_runner.runSpider(Porn91CrawlerSpider)
    print("done...")
