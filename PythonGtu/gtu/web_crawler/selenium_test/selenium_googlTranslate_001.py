import requests
from bs4 import BeautifulSoup as bs
from selenium import webdriver
from gtu.reflect import checkSelf

from selenium.webdriver.remote.webdriver import WebDriver as WD
from gtu.web_crawler.selenium_test import seleniumUtil
from threading import Thread
from gtu.net import ssl_util

from gtu.util import queue_test_001
from gtu.string import stringUtil

import sys
import time
import re
from enum import Enum
from gtu.io import fileUtil
from gtu.error import errorHandler

from abc import ABCMeta, abstractmethod

from gtu.io import LogWriter
from gtu.collection import listUtil

log = LogWriter.LogWriter()

class MyGoogleTranslateFetcher(Thread, metaclass=ABCMeta) :
    baseURL = "https://translate.google.com.tw/?sxsrf=ACYBGNSXtWb9L-KnXJftDoKBonTteGPxRA:1570063273809&biw=1372&bih=758&um=1&ie=UTF-8&hl=zh-CN&client=tw-ob#en/zh-TW/"

    def __init__(self) :
        super().__init__()
        self.queue = queue_test_001.DequeObj()
        self.driver = seleniumUtil.getDriver()
        self.driver.get(MyGoogleTranslateFetcher.baseURL)
        self.start()

    def reflashName(self) :
        newName = "".format(\
            ""
            )
        # self.setName(newName)

    def translate(self, sourceStr) :
        if stringUtil.isBlank(sourceStr) :
            return ""
        textarea = self.driver.find_element_by_css_selector("textarea[id=source]")
        textarea.send_keys(sourceStr)
        targetLst = None
        count = 0
        while ( targetLst is None or len(targetLst) == 0 ) and count <= 30 :
            targetLst = self.driver.find_elements_by_css_selector(".tlid-translation.translation")
            time.sleep(0.2)
            count += 1
        sb = ""
        for i,v in enumerate(targetLst) :
            sb += v.text.strip()
        seleniumUtil.WebElementControl.clearInput(textarea)
        return sb
         
    def run(self) :
        # bs(self.driver.page_source, "html.parser")
        while True :
            if self.queue.length() > 0 :
                # sourceStr = self.queue.popleft()
                sourceStr = self.queue.popright()
                _sourceStr = self.beforeProcess(sourceStr)
                resultStr = self.translate(_sourceStr)
                self.myProcess(sourceStr, _sourceStr, resultStr)
            self.reflashName()
            time.sleep(0.2)

    @abstractmethod
    def beforeProcess(self, sourceStr) :
        pass

    @abstractmethod
    def myProcess(self, sourceStr, fixSourceStr, resultStr) :
        pass



class MyImplMyGoogleTranslateFetcher(MyGoogleTranslateFetcher) :
    def javaParameterToDbColumn(self, strVal) :
        strVal = strVal.strip()
        length = len(strVal)
        sb = ""
        for i in range(0, length) :
            if strVal[i].islower() and i + 1 < length and strVal[i + 1].isupper() :
                sb += strVal[i].lower() + " "
            elif strVal[i] =="_" :
                sb += " "
            else:
                sb += strVal[i].lower()
        return sb

    def beforeProcess(self, sourceStr) :
        return self.javaParameterToDbColumn(sourceStr)

    def myProcess(self, sourceStr, fixSourceStr, resultStr) :
        sourceStr = sourceStr.strip()
        fixSourceStr = fixSourceStr.strip()
        resultStr = CustomTranslateEnum.replace([sourceStr, fixSourceStr], resultStr)
        print("\t", sourceStr, "\t->\t", resultStr)
        strVal = "{javaName}\t{type}\t{require}\t{defaultVal}\t{description}".format(javaName=sourceStr,type="字串",require="Y",defaultVal="無",description=resultStr)
        log.writeline(strVal)




class CustomTranslateEnum(Enum) :
    t1 = (["robo"], ["機器", "機器人"], "智能投資")
    t2 = (["shore Type"], ["岸型"], "境內外")
    t3 = (["Req"], ["Req", "要求"], "請求")
    t4 = (["idOnlyRadio"], ["僅ID電台"], "只輸入radio button")
    t5 = (["TrustAcctPeriod"], ["*"], "Y=定期不定額,N=定期定額") #"*"=表示全部替換
    t6 = (["ShortNM"], ["短缺"], "名稱")
    t7 = (["Branch"], ["分支"], "分行")
    t8 = (["CTFType"], ["ctftype"], "境內外")
    t9 = (["trust"], ["信任"], "信託")
    t10 = (["Res"], ["Res", "資源"], "回應")
    t11 = (["from"], ["from"], "來源")
    tXXXXX = (["xxxxxxxxxxxxxxx"], ["xxxxxxxxxxxxxxx"], "xxxxxxxxxxxxxxx")

    def __init__(self, englishArry, chineseFromArry, chineseTo) :
        self.englishArry = self.sortChineseFromArry(englishArry)
        self.chineseFromArry = self.sortChineseFromArry(chineseFromArry)
        self.chineseTo = chineseTo

    def sortChineseFromArry(self, chineseFromArry) :
        def comparatorFunc(x, y) :
            if len(x) > len(y) : 
                return -1
            elif len(x) < len(y) :
                return 1
            else :
                return 0
        return listUtil.sort(chineseFromArry, comparatorFunc)

    @classmethod
    def replace(clz, sourceWordArry, targetWord) :
        for i, name in enumerate(CustomTranslateEnum.__members__, 0):
            e = CustomTranslateEnum[name]
            for english in e.englishArry :
                for compareEnglish in sourceWordArry :
                    if english.lower() in compareEnglish.lower() :
                        for chineseFrom in e.chineseFromArry :
                            if chineseFrom.lower() in targetWord.lower() :
                                # return targetWord.replace(chineseFrom, e.chineseTo)
                                targetWord = re.sub(chineseFrom, e.chineseTo, targetWord, flags=re.I)
                            elif chineseFrom == "*" :
                                targetWord = e.chineseTo
        return targetWord




def main() :
    THREAD = MyImplMyGoogleTranslateFetcher()
    lst = fileUtil.loadFile_asList(fileUtil.getDesktopDir(fileName="googleTranslate.txt"))
    for i,v in enumerate(lst) :
        THREAD.queue.appendleft(v)
    pass



if __name__ == '__main__' :
    main()
    # network("http://www.gamer.com.tw") # http://www.91porn.com/view_video.php?viewkey=a9a7ceb02bab655e0e6a