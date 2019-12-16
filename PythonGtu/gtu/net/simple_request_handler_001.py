import urllib.request
from urllib.request import urlretrieve
from gtu.io import fileUtil
from gtu.os import runtimeUtil
from gtu.sys import os_info
# import ssl
# import urllib2


'''
from gtu.net import simple_request_handler_001
'''



def doRequestGet(url):
    # response = <class 'http.client.HTTPResponse'> 
    return urllib.request.urlopen(url)  


def doDownload(url, filepath):
    # ssl._create_default_https_context = ssl._create_unverified_context
    urlretrieve(url, filepath)


def _getWget() :
    wget = ""
    if os_info.isWindows():
        wget = " D:/apps/wget.exe "
    else:
        wget = " wget "
    return wget


def doDownload_ver2(url, filepath) :
    # command = " curl {url} --ssl-no-revoke -o \"{filepath}\" "
    command = " {wget} -O  \"{filepath}\"  --no-check-certificate  {url} " # https://eternallybored.org/misc/wget/
    finalCommand = command.format(wget=_getWget(), url=url, filepath=filepath)
    print("doDownload_ver2 : ", finalCommand)
    return runtimeUtil.runtimeExec3(finalCommand)



if __name__ == '__main__' :
    url = "https://p.plus28.com/attachment.php?aid=2567955&k=2a9ca21fb570719acce55824d6ab23f9&t=1574070577"
    filepath = fileUtil.getDesktopDir("xxx.torrent")
    out, err, errcode = doDownload_ver2(url, filepath)
    print("out", out)
    print("out", err)
    print("out", errcode)
    print("done...") 