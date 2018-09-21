# -*- coding: utf-8 -*-
import os

import scrapy
from scrapy.crawler import Crawler
from scrapy.crawler import CrawlerProcess
from scrapy.linkextractors import LinkExtractor
from scrapy.selector.lxmlsel import HtmlXPathSelector
from scrapy.settings import Settings
from scrapy.spiders import CrawlSpider, Rule
from twisted.internet import reactor

from gtu.io import fileUtil
from gtu.net import simple_request_handler_001
from gtu.reflect import checkSelf
from gtu.scrapy import scrapy_runner
from gtu.datetime import dateUtil

from selenium import webdriver



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
        self.driver = webdriver.Firefox()
  
    def parse(self, response):  # process_item
        print("### MAIN page : ", response.url)
            



if __name__ == '__main__' :
    scrapy_runner.runSpider(Porn91CrawlerSpider)
    print("done...")
