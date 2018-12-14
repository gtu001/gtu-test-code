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


# https://medium.com/python-pandemonium/develop-your-first-web-crawler-in-python-scrapy-6b2ee4baf954
class PttBeautyCrawlerSpider(scrapy.Spider):  #   scrapy.Spider    /    CrawlSpider
    name = 'ptt_beauty'
    
    allowed_domains = ['www.ptt.cc'] 
    start_urls = ['https://www.ptt.cc/bbs/Beauty']
    
    BASE_URL = 'https://www.ptt.cc'
    FETCH_SINCE_DATE = dateUtil.getDatetimeByJavaFormat("2018/11/01", "yyyy/MM/dd", True)
    FETCH_PREFIX_YEAR = "2018/"
    
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
  
    def parse(self, response):  # process_item
        print("### MAIN page : ", response.url)
        hxs = HtmlXPathSelector(response)
        
        for item in self.otherPageDetect(response) :
            yield item
        
        for item in self.parseLinkAndTitle(response) :
            yield item
            
    def parseLinkAndTitle(self, response):
        print("### parseLinkAndTitle")
        
        divs = response.css("div.r-ent > div.title > a[href*='/bbs/Beauty']")
        dates = response.css("div.r-ent > div.meta > div.date::text").extract()
        titles = divs.css("::text").extract()
        hrefs = divs.css("::attr(href)").extract()
        
        for item in zip(titles, hrefs, dates):
            scraped_info = {
               'title' : item[0],
               'href' : "https://www.ptt.cc" + item[1],
               'date' : item[2].strip(),
               }
            print("scraped_info", scraped_info)
            
            if not self.isDatePremit(scraped_info) :
                print("日期不符跳過")
                continue
            
            if not self.isPicFormatOk(scraped_info) :
                print("內容不符跳過")
                continue
            
            yield scrapy.Request(scraped_info['href'], callback=self.parse_detail_page, meta=scraped_info)
            
    def otherPageDetect(self, response):
        print("### otherPageDetect")
        btns = response.css(".btn.wide::attr(href)").extract();
        for item in zip(btns):
            scraped_info = {
                "pageUrl" : PttBeautyCrawlerSpider.BASE_URL + item[0],
                }
            yield scrapy.Request(scraped_info['pageUrl'], callback=self.parse, meta=scraped_info)
            
    def parse_detail_page(self, response):
        print("parse_detail_page-------------------")
        '''
         <a href="https://i.imgur.com/af03C67.jpg" target="_blank" rel="nofollow">https://i.imgur.com/af03C67.jpg</a>
        '''
        orignTags = response.xpath("..//a[contains(@href, 'imgur')][string-length(text()) > 0]")
        links = orignTags.xpath("./@href").extract()
        texts = orignTags.xpath("./text()").extract()
        
        for item in zip(links, texts):
            scraped_info = {}
            scraped_info.update(response.meta)
            scraped_info['img'] = item[0]
#             print("scraped_info", scraped_info)
            yield scraped_info
            yield self.download(scraped_info);

    def download(self, scraped_info):
        dateObj = dateUtil.getDatetimeByJavaFormat("2018/" + scraped_info['date'], "yyyy/MM/dd", True)
        dateStr = dateUtil.formatDatetimeByJavaFormat(dateObj, "yyyyMMdd")
        
        fileDir = fileUtil.getDesktopDir() + os.sep + "ptt_beauty_pics" + os.sep + dateStr + os.sep + scraped_info['title']
        fileUtil.mkdirs(fileDir)
        
        imgName = scraped_info['img']
        imgName = imgName[imgName.rfind("/") + 1:]
        
        url = scraped_info['img']
        filepath = fileDir + os.sep + imgName;
        
        if os.path.exists(filepath) and fileUtil.canRead(filepath) :
            print("檔案已存在:", filepath)
            return
        
        simple_request_handler_001.doDownload(url, filepath)
        
    def isDatePremit(self, scraped_info):
        dateObj = dateUtil.getDatetimeByJavaFormat(PttBeautyCrawlerSpider.FETCH_PREFIX_YEAR + scraped_info['date'], "yyyy/MM/dd", True)
        if PttBeautyCrawlerSpider.FETCH_SINCE_DATE > dateObj :
            return False
        return True
    
    def isPicFormatOk(self, scraped_info):
        if "帥哥" in scraped_info['title'] or "[公告]" in scraped_info['title'] :
            return False
        return True


if __name__ == '__main__' :
    scrapy_runner.runSpider(PttBeautyCrawlerSpider)
    print("done...")





















