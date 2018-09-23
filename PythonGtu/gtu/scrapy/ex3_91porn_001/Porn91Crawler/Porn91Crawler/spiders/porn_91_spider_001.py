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
from selenium.webdriver.firefox.firefox_binary import FirefoxBinary
from twisted.internet import reactor

from gtu.datetime import dateUtil
from gtu.io import fileUtil
from gtu.net import simple_request_handler_001
from gtu.os import envUtil
from gtu.reflect import checkSelf
from gtu.scrapy import scrapy_runner


def getWebDriver():
    path = envUtil.getEnv("PATH");
    if path.find("geckodriver") == -1 :
        path = path + ":" + "/media/gtu001/OLD_D/apps/scrapy/linux/geckodriver"
        print("set Path = " + path)
        envUtil.export("PATH", path)
    
#     return webdriver.Firefox()
    firefox = FirefoxBinary(r"/usr/bin/firefox") 
    return webdriver.Firefox(firefox_binary=firefox) 
    

    
       
         

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
        self.driver = getWebDriver()
  
    def parse(self, response):  # process_item
        print("### MAIN page : ", response.url)
            



if __name__ == '__main__' :
    scrapy_runner.runSpider(Porn91CrawlerSpider)
    print("done...")
