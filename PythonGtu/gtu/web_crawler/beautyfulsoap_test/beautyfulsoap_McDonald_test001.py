import requests
from bs4 import BeautifulSoup as bs
from gtu.reflect import checkSelf


def subMenuProducts(subHref) :
    baseUrl = "https://www.mcdelivery.com.tw/tw/browse/menu.html"
    resp2 = requests.get(baseUrl + subHref, verify=False)
    soup2 = bs(resp2.text, "html.parser")
    prodLst = soup2.select("div.panel.panel-default.panel-product")

    for i,v in enumerate(prodLst) :
        print(v.select(".product-title")[0].getText().strip())
        print(v.select(".product-details")[0].getText().strip())


def main() :
    resp = requests.get("https://www.mcdelivery.com.tw/tw/browse/menu.html?daypartId=45&catId=97", verify=False)
    soup = bs(resp.text, "html.parser")

    menuLst = soup.select("a.secondary-menu-item-target")
    showDoc = False

    for i,v in enumerate(menuLst) :
        print("==============================================")
        print(i+1)
        print(v.getText().strip())
        subMenuProducts(v.get("href").strip())
        print()

        if not showDoc :
            showDoc = True
            checkSelf.checkMembersToHtml(v, "bs4.element.Tag.htm")


if __name__ == '__main__' :
    main()
    
    
