
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
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import StaleElementReferenceException
from selenium.common.exceptions import ElementClickInterceptedException


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



def _getChromeConfig(config) :
    chromeOptions = webdriver.ChromeOptions()
    prefs = {}
    if "downloadPath" in config :
        prefs["download.default_directory"] = config["downloadPath"]
        prefs["profile.default_content_settings.popups"] = 0
        prefs["download.prompt_for_download"] = False
        prefs["download.directory_upgrade"] = True
        prefs["safebrowsing.enabled"] = True
    chromeOptions.add_experimental_option("prefs",prefs)
    return chromeOptions


def getDriver(width=1280, height=720, config={}) :
    # https://stackoverflow.max-everyday.com/2019/12/selenium-chrome-options/
    if envUtil.isWindows() :
        driverPath = r"D:/apps/selenium/chromedriver.exe"
    else :
        driverPath = r"/media/gtu001/OLD_D/apps/webdriver/chromedriver"
    
    # webdriver.Firefox()
    driver = webdriver.Chrome(executable_path=driverPath, chrome_options=_getChromeConfig(config))

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
    def setValue(element, text) :
        return element.send_keys(text)

    @staticmethod
    def getValue(element) :
        return element.get_attribute("value")

    @staticmethod
    def click(element) :
        return element.click()

    @staticmethod
    def clickUntil(driver, xpath=None, css=None) :
        for i in range(20):
            try:
                if xpath is not None :
                    WebDriverWait(driver, 20).until(EC.element_to_be_clickable((By.XPATH, xpath))).click()
                else :
                    WebDriverWait(driver, 20).until(EC.element_to_be_clickable((By.CSS_SELECTOR, css))).click()
                break
            except ElementClickInterceptedException as ex :
                time.sleep(1)

    @staticmethod
    def enter(webElement) :
        from selenium.webdriver.common import keys
        webElement.send_keys(keys.Keys.ENTER)

    @staticmethod
    def getHtml(element) :
        return element.get_attribute('innerHTML')

    @staticmethod
    def getCurrentUrl(driver):
        return driver.current_url

    @staticmethod
    def setSelect(webElement, text=None, value=None, index=None):
        if text is not None :
            webElement.find_element_by_xpath(".//option[text()='"+ text +"']").click()
        elif value is not None:
            webElement.find_element_by_xpath(".//option[value()='"+ value +"']").click()
        else :
            findOk = False
            options = webElement.find_elements_by_xpath(".//option")
            for i,v in enumerate(options) :
                if i == index:
                    findOk = True
                    print("setSelect(index) = ", i, ", value=", v.get_attribute("value"), ", text=", v.text)
                    v.click()
            if not findOk :
                print("index 超出範圍 : ", str(index))

    @staticmethod
    def getOptionsLst(webElement) :
        lst = list()
        options = webElement.find_elements_by_xpath(".//option")
        for i,v in enumerate(options) :
            print(i, ", value=", v.get_attribute("value"), ", text=", v.text)
            lst.append((v.get_attribute("value"), v.text))
        return lst


    @staticmethod
    def until(driver, timeout=300, frequence=0.5, message='', waitUntilFunc=None) :
        #回傳False會繼續等
        if waitUntilFunc is None :
            def waitUntilFuncDefault(driver) :
                try :
                    print("waitUntilFunc try...")
                    return False
                except Exception as ex :
                    return True
            waitUntilFunc = waitUntilFuncDefault
        WebDriverWait(driver, timeout, frequence).until(waitUntilFunc, message)

    @staticmethod
    def waitPageElementByCss(css, text, driver, retryTime=10, retryWait=0.5) : 
        for t in range(0, retryTime) :
            try :
                def waitUntilFunc(driver) :
                    chk1 = driver.find_elements_by_css_selector(css)
                    lenOk = len(chk1)
                    print("check_css_exists = " , lenOk)
                    if lenOk > 0 :
                        return True
                    return False        
                WebElementControl.until(driver, timeout=300, frequence=0.5, message='', waitUntilFunc=waitUntilFunc) 
                elements = driver.find_elements_by_css_selector(css)
                if stringUtil.isBlank(text) and len(elements) > 0:
                    return elements[0]
                text = text.lower()
                for i,v in enumerate(elements) :
                    print(i,v.text)
                    if text in v.text.lower() :
                        return v
            except StaleElementReferenceException as ex :
                time.sleep(retryWait)
                print("[StaleElementReferenceException] try again ! ")
        return None

    @staticmethod
    def waitPageElementByCsss(css, text, driver, retryTime=10, retryWait=0.5) : 
        for t in range(0, retryTime) :
            try :
                def waitUntilFunc(driver) :
                    chk1 = driver.find_elements_by_css_selector(css)
                    lenOk = len(chk1)
                    print("check_css_exists = " , lenOk)
                    if lenOk > 0 :
                        return True
                    return False        
                WebElementControl.until(driver, timeout=300, frequence=0.5, message='', waitUntilFunc=waitUntilFunc) 
                elements = driver.find_elements_by_css_selector(css)
                if stringUtil.isBlank(text) and len(elements) > 0:
                    return elements[0]
                text = text.lower()
                lst = list()
                for i,v in enumerate(elements) :
                    print(i,v.text)
                    if text in v.text.lower() :
                        lst.append(v)
                return lst
            except StaleElementReferenceException as ex :
                time.sleep(retryWait)
                print("[StaleElementReferenceException] try again ! ")
        return None
        
    @staticmethod
    def waitPageElementByXpath(xpath, driver, retryTime=10, retryWait=0.5) :
        for t in range(0, retryTime) :
            try :
                def waitUntilFunc(driver) :
                    chk1 = driver.find_elements_by_xpath(xpath)
                    print("check_xpath_exists = " , len(chk1))
                    if len(chk1) > 0 :
                        return True
                    return False        
                WebElementControl.until(driver, timeout=300, frequence=0.5, message='', waitUntilFunc=waitUntilFunc) 
                return driver.find_elements_by_xpath(xpath)[0]
            except StaleElementReferenceException as ex :
                time.sleep(retryWait)
                print("[StaleElementReferenceException] try again ! ")
		return None

    @staticmethod
    def waitPageElementByXpaths(xpath, driver, retryTime=10, retryWait=0.5) :
        for t in range(0, retryTime) :
            try :
                def waitUntilFunc(driver) :
                    chk1 = driver.find_elements_by_xpath(xpath)
                    print("check_xpath_exists = " , len(chk1))
                    if len(chk1) > 0 :
                        return True
                    return False        
                WebElementControl.until(driver, timeout=300, frequence=0.5, message='', waitUntilFunc=waitUntilFunc) 
                return driver.find_elements_by_xpath(xpath)
            except StaleElementReferenceException as ex :
                time.sleep(retryWait)
                print("[StaleElementReferenceException] try again ! ")
		return None
        


class DownloadWatcher :
    @staticmethod
    def getDownLoadedFileName_chrome(driver, waitTime):
        driver.execute_script("window.open()")
        driver.switch_to.window(driver.window_handles[-1])
        driver.get('chrome://downloads')
        endTime = time.time()+waitTime
        while True:
            try:
                downloadPercentage = driver.execute_script(
                    "return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('#progress').value")
                if downloadPercentage == 100:
                    return driver.execute_script("return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('div#content  #file-link').text")
            except:
                pass
            time.sleep(1)
            if time.time() > endTime:
                break

    @staticmethod
    def getDownLoadedFileName_firefox(driver, waitTime):
        driver.execute_script("window.open()")
        WebDriverWait(driver,10).until(EC.new_window_is_opened)
        driver.switch_to.window(driver.window_handles[-1])
        driver.get("about:downloads")

        endTime = time.time()+waitTime
        while True:
            try:
                fileName = driver.execute_script("return document.querySelector('#contentAreaDownloadsView .downloadMainArea .downloadContainer description:nth-of-type(1)').value")
                if fileName:
                    return fileName
            except:
                pass
            time.sleep(1)
            if time.time() > endTime:
                break



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

