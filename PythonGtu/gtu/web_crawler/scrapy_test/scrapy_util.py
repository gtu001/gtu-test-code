
'''
from gtu.scrapy import scrapy_util
'''
from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.firefox.firefox_binary import FirefoxBinary

from gtu.io import fileUtil
from gtu.os import envUtil

from gtu.sys import os_info
import os


def getHtml(response, encode=None):
    if encode:
        html = response.body.decode(encode)
    else :
        html = response.body
    return html


def saveHtml(response, filePath, encode=None):
    if encode:
        html = response.body.decode(encode)
        fileUtil.saveToFile(filePath, encode)
    else :
        html = response.body
        fileUtil.saveToFileBytes(filePath, html)
    
    
    
def getWebDriver():
    path = str(envUtil.getEnv("PATH"))  
    #path = path + os_info.getPathEnd() + "/media/gtu001/OLD_D/apps/scrapy/linux/"
    path = path + os_info.getPathEnd() + "D:/apps/chromedriver_win32"

    print("set Path = " + path)
    envUtil.export("PATH", path)
#     envUtil.showAll()
    type = ""
    if type == 'firefox_linux' :
        firefox = FirefoxBinary(r"/usr/bin/firefox") 
        return webdriver.Firefox(firefox_binary=firefox)
    elif type == 'chrome' :
        return webdriver.Chrome() 
    else :
        # binary = FirefoxBinary(r'/usr/lib/firefox/firefox.sh')
        binary = FirefoxBinary(r'C:/Program Files/Mozilla Firefox/firefox.exe')
        fireFoxOptions = webdriver.FirefoxOptions()
        fireFoxOptions.set_headless(headless=False)#可取消開啟瀏覽器
        firefox_capabilities = DesiredCapabilities.FIREFOX
        firefox_capabilities['marionette'] = True
        driver = webdriver.Firefox(capabilities=firefox_capabilities,firefox_binary=binary, firefox_options = fireFoxOptions)
        return driver
    
    
    
    
    
    
    
    