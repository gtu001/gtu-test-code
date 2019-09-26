import requests
from bs4 import BeautifulSoup as bs
from selenium import webdriver
from gtu.reflect import checkSelf
import time
from selenium.webdriver.remote.webdriver import WebDriver as WD

def getDriver() :
    driverPath = r"D:/apps/selenium/chromedriver.exe"
    # webdriver.Firefox()
    driver = webdriver.Chrome(executable_path=driverPath)

    driver.set_window_position(0,0) #瀏覽器位置
    driver.set_window_size(700,700) #瀏覽器大小
    return driver


def main() :
    driver = getDriver()

    driver.get("http://www.91porn.com/index.php")

    pageContent = dict()

    for page in range(0, 10) :
        soup = bs(driver.page_source, "html.parser")
        
        pornMovies = soup.select(".jcarousel-clip.jcarousel-clip-horizontal [jcarouselindex]")

        for i,v in enumerate(pornMovies) :
            a = v.select("a")[0]
            link = a.get("href")
            text = a.getText().strip()

            if text in pageContent :
                break

            pageContent[text] = link
            print(i, text, link)

        nextBtn = driver.find_elements_by_css_selector(".jcarousel-next.jcarousel-next-horizontal")[0]
        nextBtn.click()
        time.sleep(2)


def network(url) :
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



if __name__ == '__main__' :
    # main()
    network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a