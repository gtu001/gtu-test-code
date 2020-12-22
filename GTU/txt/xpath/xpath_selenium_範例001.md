xpath_selenium_範例001.md
---

    trs = self.driver.find_elements_by_xpath("//div[contains(@class,'cms-label')][text()='信用卡消費明細']/following-sibling::table//tbody//tr")
    for i,tr in enumerate(trs) :
        print(i, tr.text)
        tds = tr.find_elements_by_xpath(".//td")
        print("--------------")
        for j,td in enumerate(tds) :
            print(j, td.text)



    //input[@maxlength=2048]
    //input[@maxlength]
    //*[@maxlength]