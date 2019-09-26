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


if __name__ == '__main__' :
    main()