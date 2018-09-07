# -*- coding: utf-8 -*-
import os

from scrapy import log
import scrapy
from scrapy.crawler import Crawler
from scrapy.crawler import CrawlerProcess
from scrapy.settings import Settings
from twisted.internet import reactor

from gtu.scrapy import scrapy_runner

from gtu.reflect import checkSelf


# https://stackoverflow.com/questions/45200710/scrapy-yield-a-request-parse-in-the-callback-but-use-the-info-in-the-original

# <div class="title">
# <a href="/bbs/Beauty/M.1536306364.A.2E6.html">[神人] 壹電視記者</a>
class PttBeautyBoardSpider(scrapy.Spider):
    name = 'redditbot'
    allowed_domains = ['www.ptt.cc/bbs/Beauty/']
    start_urls = ['https://www.ptt.cc/bbs/Beauty']
  
    def parse(self, response):
        
        divs = response.css("div.title")
        titles = divs.css("a::text").extract()
        hrefs = divs.css("a::attr(href)").extract()
        
        for item in zip(titles, hrefs):
            
            scraped_info = {
               'title' : item[0],
               'href' : item[1],
               }
            
            print("scraped_info", scraped_info)
            
            next_url = "https://www.ptt.cc" + scraped_info['href']
            
            print(next_url)
            
            scrapy.Request(next_url, callback=self.parse_tag, meta=response.meta)
            
            
            
    def parse_tag(self, response):
        
        print("parse_tag-------------------")
#         <a href="https://i.imgur.com/af03C67.jpg" target="_blank" rel="nofollow">https://i.imgur.com/af03C67.jpg</a>
        picLst = response.xpath("..//a[contains(@href, 'imgur')]").extract()
        
        for item in zip(picLst):
            
            scraped_info = {
               'pic' : item[0]
               }
            
            print("scraped_info", scraped_info)
            
            yield scraped_info
        

if __name__ == '__main__' :
    scrapy_runner.runSpider(PttBeautyBoardSpider)
    print("done...")

    







