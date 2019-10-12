
import requests
from bs4 import BeautifulSoup as bs
from selenium import webdriver
from gtu.reflect import checkSelf

from selenium.webdriver.remote.webdriver import WebDriver as WD
import time
from gtu.datetime import duringMointer
from gtu.os import envUtil
import inspect
from gtu.string import stringUtil

'''
from gtu.web_crawler.selenium_test import seleniumUtil
'''

def __test_network(url) :
    from selenium import webdriver
    from selenium.webdriver.common.by import By
    from selenium.webdriver.support.ui import Select
    from selenium.webdriver.support.ui import WebDriverWait
    from selenium.common.exceptions import NoSuchElementException, ElementNotVisibleException
    from browsermobproxy import Server
    import urllib.parse

    server = Server(r"D:/apps/browsermob-proxy-2.1.4/bin/browsermob-proxy.bat")
    server.start()
    proxy = server.create_proxy()
    proxy.new_har("file_name", options={'captureHeaders': True, 'captureContent': True})

    chrome_options = webdriver.ChromeOptions()
    proxy1 = urllib.parse.urlparse(proxy.proxy).netloc
    chrome_options.add_argument('--proxy-server=%s' % proxy1)
    driver = webdriver.Chrome(
        executable_path=r"D:/apps/selenium/chromedriver.exe",
        chrome_options=chrome_options)
    driver.get(url)
    proxy.wait_for_traffic_to_stop(1, 60)
    checkSelf.checkMembersToHtml(proxy)
    print(proxy.har)
    driver.quit()
    server.stop()



def getDriver(width=1280, height=720) :
    if envUtil.isWindows() :
        driverPath = r"D:/apps/selenium/chromedriver.exe"
    else :
        driverPath = r"/media/gtu001/OLD_D/apps/webdriver/chromedriver"
    
    # webdriver.Firefox()
    driver = webdriver.Chrome(executable_path=driverPath)

    driver.set_window_position(0,0) #瀏覽器位置
    driver.set_window_size(width, height) #瀏覽器大小
    return driver


class WebElementControl :
    @staticmethod
    def clearInput(webElement) :
        from selenium.webdriver.common import keys
        webElement.send_keys(keys.Keys.CONTROL + "a")
        webElement.send_keys(keys.Keys.DELETE)

    @staticmethod
    def enter(webElement) :
        from selenium.webdriver.common import keys
        webElement.send_keys(keys.Keys.ENTER)

    @staticmethod
    def getHtml(element) :
        return element.get_attribute('innerHTML')


class _PageChangeCallback :
    def __init__(self, driver, func) :
        self.func = func
        self.driver = driver
        self.page_source = ""
    def checkPageSource(self) :
        page_source = self.driver.page_source
        if self.func and self.page_source != page_source :
            self.page_source = page_source
            self.func(self.driver)


class ScrollHandler :
    @staticmethod
    def getScrollPosition(driver) :
        return int(driver.execute_script("return document.documentElement.scrollTop;"))


    @staticmethod
    def getScrollMaxHeight(driver) :
        script = '''
        //var body = document.body,
        //html = document.documentElement;
        //var height = Math.max( body.scrollHeight, body.offsetHeight, 
        //                html.clientHeight, html.scrollHeight, html.offsetHeight );
        //return height;
        return document.body.scrollHeight;
        '''
        scrollHeight = driver.execute_script(script)
        return int(scrollHeight)

    

    @staticmethod
    def scroll2Button(driver, smooth=False, scrollSize=10, pageChangeListener=None) :
        if not smooth :
            driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        else:
            start = ScrollHandler.getScrollPosition(driver)
            end = ScrollHandler.getScrollMaxHeight(driver)

            listener = _PageChangeCallback(driver, pageChangeListener)         
            for i in range(start, end, scrollSize) :
                driver.execute_script("window.scrollBy(0, arguments[0])", i)
                listener.checkPageSource()



    @staticmethod
    def scroll2Buttom_Repeat(driver, smooth=False, repeatButtomCount=10, sleepSecond=2, scrollSize=10, pageChangeListener=None) :
        height = ScrollHandler.getScrollMaxHeight(driver)
        print("init h : ", height)
        for i in range(0, repeatButtomCount) :
            ScrollHandler.scroll2Button(driver, smooth, scrollSize, pageChangeListener)

            if sleepSecond and smooth == False :
                time.sleep(sleepSecond)

            tmpHeight = ScrollHandler.getScrollPosition(driver)
            print("repeat h : ", height, tmpHeight)
            if int(height) < int(tmpHeight) :
                height = tmpHeight
            else:
                print("buttomed")
                break

