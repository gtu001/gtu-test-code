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

from gtu.scrapy import scrapy_runner

# https://medium.com/python-pandemonium/develop-your-first-web-crawler-in-python-scrapy-6b2ee4baf954


class PttBeautyCrawlerSpider(scrapy.Spider):  #   scrapy.Spider    /    CrawlSpider
    name = 'ptt_beauty'
    
    allowed_domains = ['www.ptt.cc'] 
    start_urls = ['https://www.ptt.cc/bbs/Beauty']
    
    BASE_URL = 'https://www.ptt.cc/bbs/Beauty'
    
    rules = (
        Rule(LinkExtractor(
                            allow=(),
                            restrict_css=()
                           ),
             callback="process_item",
             follow=False),
        )
  
    def parse(self, response):  # process_item
        
        hxs = HtmlXPathSelector(response)
        base_url = response.url
        
        '''
        <a href="/bbs/Beauty/M.1536306364.A.2E6.html">[神人] 壹電視記者</a>
        '''
        btns = response.css(".btn.wide::attr(href)").extract();
        
        for item in zip(btns):
            scraped_info = {
                "pageUrl" : PttBeautyCrawlerSpider.BASE_URL + item[0],
                }
            print("pageUrl - ", scraped_info['pageUrl'])
            yield scrapy.Request(scraped_info['pageUrl'], callback=self.parse, meta=scraped_info)
        
        divs = response.css("div.title")
        titles = divs.css("a::text").extract()
        hrefs = divs.css("a::attr(href)").extract()
        
        for item in zip(titles, hrefs):
            scraped_info = {
               'title' : item[0],
               'href' : "https://www.ptt.cc" + item[1],
               }
            print("scraped_info", scraped_info)
            print("url", scraped_info['href'])
            
            yield scrapy.Request(scraped_info['href'], callback=self.parse_detail_page, meta=scraped_info)
            
            
    def parse_detail_page(self, response):
        print("parse_detail_page-------------------")
#        
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




if __name__ == '__main__' :
    scrapy_runner.runSpider(PttBeautyCrawlerSpider)
    print("done...")

