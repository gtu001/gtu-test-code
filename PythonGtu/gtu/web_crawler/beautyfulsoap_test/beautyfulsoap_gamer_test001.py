import requests
from bs4 import BeautifulSoup as bs

resp = requests.get("https://buy.gamer.com.tw/indexList.php?hpmore=1")
soup = bs(resp.text, "html.parser")

pLst = soup.select("p.ES-lbox2A")

for i,v in enumerate(pLst) :
    print(i+1)
    print(v.select("a.ES-lbox2C")[0].text)
    print(v.select("a.ES-lbox2C")[0].find_next_siblings('span')[0].text)
    print()
    
