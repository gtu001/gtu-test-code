# -*- coding: utf-8 -*-
import scrapy
from scrapy.crawler import CrawlerProcess

# scrapy settings for ourfirstscraper project
# For simplicity, this file contains only settings considered important or commonly used. You can find more settings consulting the documentation :
#   http://doc.scrapy.org/en/latest/topics/settings.html
#   http://scrapy.readthedocs.org/en/latest/topics/downloader-middleware.html
#   http://scrapy.readthedocs.org/en/latest/topics/spider-middleware.html


'''
from gtu.scrapy.ex1.scrapy_test_001 import RedditbotSpider
'''

BOT_NAME = "ourfirstscrper"

SPIDER_MODULES = ['ourfirstscraper.spiders']
NEWSPIDER_MODULE = "ourfirstscraper.spiders"

# Export as CSV Feed
FEED_FORMAT = "csv"
FEED_URI = "rediit.csv"




class RedditbotSpider(scrapy.Spider):
    name = 'redditbot'
    allowed_domains = ['www.reddit.com/r/gameofthrones/']
    start_urls = ['http://www.reddit.com/r/gameofthrones//']
  
    def parse(self, response):
        # Estracting the content using css selectors
        titles = response.css(".title.may-blank::text").extract()
        votes = response.css(".score.unvoted::text").extract()
        times = response.css("time::attr(title)").extract()
        comments = response.css(".comments::text").extract()
      
        # Give the extracted content row wise
        for item in zip(titles, votes, times, comments):
            # create a dictionary to store the scraped info
            scraped_info = {
                "title" : item[0],
                "vote" : item[1],
                "created_at" : item[2],
                "comments" : item[3],
            }
    
        # yield or give the scraped info to scrapy 
        yield scraped_info




class ShopcluesSpider(scrapy.Spider):
    # name of spider
    name = 'shopclues'
    # list of allowed domains
    allowed_domains = ['www.shopclues.com/mobiles-featured-store-4g-smartphone.html']
    # starting url
    start_urls = ['http://www.shopclues.com/mobiles-featured-store-4g-smartphone.html/']
    # location of csv file
    custom_settings = {
       'FEED_URI' : 'tmp/shopclues.csv'
    }

    def parse(self, response):
        # Extract product information
        titles = response.css('img::attr(title)').extract()
        images = response.css('img::attr(data-img)').extract()
        prices = response.css('.p_price::text').extract()
        discounts = response.css('.prd_discount::text').extract()

        for item in zip(titles, prices, images, discounts):
            scraped_info = {
               'title' : item[0],
               'price' : item[1],
               'image_urls' : [item[2]],  # Set's the url for scrapy to download images
               'discount' : item[3]
               }

            yield scraped_info
            
            
            

class TechcrunchSpider(scrapy.Spider):
    #name of the spider
    name = 'techcrunch'

    #list of allowed domains
    allowed_domains = ['techcrunch.com/feed/']

    #starting url for scraping
    start_urls = ['http://techcrunch.com/feed/']

    #setting the location of the output csv file
    custom_settings = {
        'FEED_URI' : 'tmp/techcrunch.csv'
    }

    def parse(self, response):
        #Remove XML namespaces
        response.selector.remove_namespaces()

        #Extract article information
        titles = response.xpath('//item/title/text()').extract()
        authors = response.xpath('//item/creator/text()').extract()
        dates = response.xpath('//item/pubDate/text()').extract()
        links = response.xpath('//item/link/text()').extract()

        for item in zip(titles,authors,dates,links):
            scraped_info = {
                'title' : item[0],
                'author' : item[1],
                'publish_date' : item[2],
                'link' : item[3]
            }

            yield scraped_info
           
           
           
            
            
if __name__ == '__main__' :
    process = CrawlerProcess({
        'USER_AGENT': 'Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)'
    })
    
    process.crawl(RedditbotSpider)
    process.start() # the script will block here until the crawling is finished










