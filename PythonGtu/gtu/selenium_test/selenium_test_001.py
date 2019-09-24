import time
from selenium import webdriver
from gtu.reflect import checkSelf
import urllib.parse

driverPath = r"D:/apps/selenium/chromedriver.exe"
# webdriver.Firefox()
driver = webdriver.Chrome(executable_path=driverPath)

driver.set_window_position(0,0) #瀏覽器位置
driver.set_window_size(700,700) #瀏覽器大小

pchome_query_url = "https://ecshweb.pchome.com.tw/search/v3.3/?q=" + urllib.parse.quote_plus("htc cosmos")
print("搜尋網頁", pchome_query_url)
driver.get(pchome_query_url)

for i in range(0, 6) :
    driver.execute_script("window.scrollTo(0,document.body.scrollHeight);")
    time.sleep(2)
    
# print(driver.page_source)

products = driver.find_elements_by_css_selector("dl.col3f")

showData = False

for i,v in enumerate(products) :
    #print(i, v.get_attribute('innerHTML'))
    print(i)
    print(v.find_element_by_css_selector('.c2f .prod_name a').text)
    print(v.find_element_by_css_selector('.c3f .price_box .price .value').text)
    print()

    # if not showData :
    #     checkSelf.checkMembersToHtml(v, "WebElement.htm")
    #     showData = True