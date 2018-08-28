import requests
import urllib.parse
from gtu.reflect import checkSelf

def formEncode(form):
    params = urllib.parse.urlencode(form)
    

def main():
    lineToken = "qVxcCOM9qUmxVUwUKxmd320JA3a6fe6PPhafqxUou2R"
    line = LineAppNotifiyHelper_Simple(lineToken)
    line.send("哈哈哈")


class LineAppNotifiyHelper_Simple :
    LINE_URL = "https://notify-api.line.me/api/notify"
    DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0"

    def __init__(self, token):
        self.token = token
        self.headers = {"Content-type": "application/x-www-form-urlencoded", \
                 "Authorization": "Bearer " + token, \
                 "User-Agent" : LineAppNotifiyHelper_Simple.DEFAULT_USER_AGENT, \
                 "Accept-Encoding" : "UTF8", \
                 }
        
    def send(self, message):
        params = {'message': message}
        resp = requests.post(LineAppNotifiyHelper_Simple.LINE_URL, data=params, json=None, headers = self.headers)
        if resp.status_code == 200 :
            return True
        return False
    

if __name__ == '__main__' :
    main()
    print("done...")
    