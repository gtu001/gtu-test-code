import os

from gtu.io import fileUtil
from os import walk

def test():
    
    dirPath = "D:/gtu001_dropbox/Dropbox/Apps/gtu001_test/english_txt"
    
    f = list()
    for (path, dirnames, filenames) in walk(dirPath):
        if path.endswith(".files") and os.path.isdir(path) :
            htmFile = path[:path.rfind(".")] + ".htm"
            if not os.path.exists(htmFile) :
                fileUtil.deleteDir(path)
                print(path, os.path.exists(path))

    
    
if __name__ == '__main__' :
    test()
    print("done...")
