# -*- coding: utf-8 -*-
import os

import scrapy
from scrapy.crawler import Crawler
from scrapy.crawler import CrawlerProcess
from scrapy.linkextractors import LinkExtractor
from scrapy.selector.lxmlsel import HtmlXPathSelector
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
from gtu.scrapy import scrapy_runner
from gtu.scrapy import scrapy_util
from gtu.io import fileUtil



FORUM_NUMBER = "717"

# https://medium.com/python-pandemonium/develop-your-first-web-crawler-in-python-scrapy-6b2ee4baf954
class Porn91CrawlerSpider(scrapy.Spider):  #   scrapy.Spider    /    CrawlSpider
    name = 'plus28_porn'
    
    allowed_domains = ['p.plus28.com'] 
    start_urls = ["https://p.plus28.com/forum-"+FORUM_NUMBER+"-1.html"]
    
    BASE_URL = 'https://p.plus28.com'
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

        
#         scrapy_util.saveHtml(response, fileUtil.getDesktopDir() + os.sep + "test.txt")

        self.checkLogin(response)
        
        

        ths = response.xpath("//table[@summary='"+FORUM_NUMBER+"']")#/tbody/th[@class='new']
        print("----", len(ths))
        
        aTitles = ths.xpath(".//span[contains(@id, 'thread_')]/a[text()]").extract()
        aHrefs = ths.xpath(".//span[contains(@id, 'thread_')]/a[@href]").extract()
        aAttachs = ths.xpath(".//img[@class='attach'][@alt='附件']").extract()
        
        for item in zip(aTitles, aHrefs, aAttachs):
            scraped_info = {}
            scraped_info.update(response.meta)
            scraped_info['title'] = item[0]
            scraped_info['link'] = item[1]
            scraped_info['attach'] = item[2]
            print("scraped_info", scraped_info)


# a href=thread-6655512-1-1.html> xxxxxxxxxxxxxxx
        

    def checkLogin(self, response):
        checkLoginOk = response.xpath("//div[@class='box message']/*[contains(text(),'您還沒有登錄')]")
        print("response url ", response.url)
        if len(checkLoginOk) == 1:
            print("driver ", self.driver)
            driver = self.driver.get(response.url)
            print(type(driver))


if __name__ == '__main__' :
    scrapy_runner.runSpider(Porn91CrawlerSpider)
    print("done...")
