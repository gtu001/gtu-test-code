# -*- coding: utf-8 -*-
import os

from scrapy import log
import scrapy
from scrapy.crawler import Crawler
from scrapy.crawler import CrawlerProcess
from scrapy.settings import Settings
from twisted.internet import reactor

from multiprocessing import Pool

'''
from gtu.scrapy import scrapy_runner
'''

def runCrawl(spider_name=None):
    if spider_name:
        os.system('scrapy crawl %s' % spider_name)
    return None



def runMultiCrawler(spider_names=None):
    if spider_names :
        pool = Pool(processes=len(spider_names))
        pool.map(runCrawl, spider_names)
    
    
    
def runSpider(spider):
    process = CrawlerProcess({
        'USER_AGENT': 'Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)'
    })
    process.crawl(spider)
    process.start()  # the script will block here until the crawling is finished











