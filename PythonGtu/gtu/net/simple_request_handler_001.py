import urllib.request
from urllib.request import urlretrieve


'''
from gtu.net import simple_request_handler_001
'''



def doRequestGet(url):
    # response = <class 'http.client.HTTPResponse'> 
    return urllib.request.urlopen(url)  


def doDownload(url, filepath):
    urlretrieve(url, filepath)






if __name__ == '__main__' :
    print("done...")