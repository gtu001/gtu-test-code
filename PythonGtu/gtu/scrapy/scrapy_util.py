
'''
from gtu.scrapy import scrapy_util
'''
from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.firefox.firefox_binary import FirefoxBinary

from gtu.io import fileUtil
from gtu.os import envUtil


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
    path = path + ":" + "/media/gtu001/OLD_D/apps/scrapy/linux/"
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
        binary = FirefoxBinary(r'/usr/lib/firefox/firefox.sh')
        fireFoxOptions = webdriver.FirefoxOptions()
        fireFoxOptions.set_headless()
        firefox_capabilities = DesiredCapabilities.FIREFOX
        firefox_capabilities['marionette'] = True
        driver = webdriver.Firefox(capabilities=firefox_capabilities,firefox_binary=binary, firefox_options = fireFoxOptions)
        return driver
    
    
    
    
    
    
    
    