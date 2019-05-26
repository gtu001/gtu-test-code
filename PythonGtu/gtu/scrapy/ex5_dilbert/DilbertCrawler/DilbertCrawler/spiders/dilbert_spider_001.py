# -*- coding: utf-8 -*-
import os
import re

import scrapy
from scrapy.crawler import Crawler
from scrapy.crawler import CrawlerProcess
from scrapy.linkextractors import LinkExtractor
from scrapy.selector.lxmlsel import HtmlXPathSelector
from scrapy.settings import Settings
from scrapy.spiders import CrawlSpider, Rule
from twisted.internet import reactor

from gtu.datetime import dateUtil
from gtu.io import fileUtil
from gtu.net import simple_request_handler_001
from gtu.reflect import checkSelf
from gtu.scrapy import scrapy_runner


def getSysdate():
    return dateUtil.formatDatetimeByJavaFormat(dateUtil.currentDate(), "yyyy-MM-dd")
    


class InitFolder():
    def __init__(self):
        self.dateLst = list()
        self.fileDir = fileUtil.getDesktopDir() + os.sep + "dilbert_pics" + os.sep
        fileUtil.mkdirs(self.fileDir)
        self.checkStartDate()
    
    def checkStartDate(self):
        from os import walk
        for (dirpath, dirnames, filenames) in walk(self.fileDir):
            for (i,name) in enumerate(filenames) :
                chkDate = self.findDate(name)
                if chkDate :
                    self.dateLst.append(chkDate)
                
    def findDate(self, link):
        link = link.strip()
        pattern = re.compile(r"(?P<date_str>\d{4}\-\d{2}\-\d{2})", re.I)
        mth = pattern.search(link)
        if mth:
            return mth.group("date_str")
        return None



# https://medium.com/python-pandemonium/develop-your-first-web-crawler-in-python-scrapy-6b2ee4baf954
class DilbertCrawlerSpider(scrapy.Spider):  #   scrapy.Spider    /    CrawlSpider
    name = 'dilbert'
    
    folderConfig = InitFolder()

    allowed_domains = ['dilbert.com'] 
    start_urls = ['http://dilbert.com/strip/' + getSysdate() ]
    
    BASE_URL = 'http://dilbert.com/strip/'
    
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
        
        for item in self.getPicInfo(response) :
            yield item
        
            
    def getBeforeDate(self, date_str):
        dateObj = dateUtil.getDatetimeByJavaFormat(date_str, "yyyy-MM-dd")
        dateObj = dateUtil.dayAdd(dateObj, -1)
        rtnDateStr = dateUtil.formatDatetimeByJavaFormat(dateObj, "yyyy-MM-dd")
        print("rtnDateStr ", rtnDateStr)
        return rtnDateStr;
    
    def getNextUrl(self, date_str):
        while True :
           date_str = self.getBeforeDate(date_str)
           if not DilbertCrawlerSpider.folderConfig.dateLst.__contains__(date_str) :
                break
        return DilbertCrawlerSpider.BASE_URL + date_str
            
            
    def getPicInfo(self, response):
        aHref = response.xpath("//a[contains(@class, 'img-comic-link') and contains(@itemprop, 'image')]")
        link = aHref.xpath("./@href").extract()
        img = aHref.xpath("./img[contains(@class, 'img-responsive') and contains(@class, 'img-comic')]")
        src = img.xpath("./@src").extract()
        alt = img.xpath("./@alt").extract()

        for item in zip(link, src, alt):
            print("link ", item[0], type(item[0]))
            print("src ", item[1], type(item[1]))
            print("alt ", item[2], type(item[2]))
            
            scraped_info = {}
            scraped_info.update(response.meta)
            scraped_info['link'] = item[0]
            scraped_info['src'] = item[1]
            scraped_info['alt'] = item[2]
            scraped_info['date_str'] = self.findLinkDate(item[0])
        
            print("scraped_info", scraped_info)
            yield scraped_info
            yield self.download(scraped_info);
            
            yield scrapy.Request(self.getNextUrl(scraped_info['date_str']), callback=self.parse, meta=scraped_info)
            
            
    def findLinkDate(self, link):
        link = link.strip()
        pattern = re.compile(r".*(?P<date_str>\d{4}\-\d{2}\-\d{2})", re.I)
        mth = pattern.search(link)
        if mth:
            return mth.group("date_str")
        raise Exception("無法取得date str")
            
    def download(self, scraped_info):
        imgName = scraped_info['date_str'] + "_" + scraped_info['alt'] + ".jpg"
        
        url = scraped_info['src']
        filepath = DilbertCrawlerSpider.folderConfig.fileDir + os.sep + imgName;
        
        if os.path.exists(filepath) and fileUtil.canRead(filepath) :
            print("檔案已存在:", filepath)
            return
        
        simple_request_handler_001.doDownload(url, filepath)


if __name__ == '__main__' :
    scrapy_runner.runSpider(DilbertCrawlerSpider)
    print("done...")

