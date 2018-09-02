
import tempfile
import os
from gtu.io import fileUtil
from gtu.os import processUtil
from gtu.os import runtimeUtil

### from gtu.io import tempFileUtil

def __debugContent__(file):
    print(file)
    for (i, v) in enumerate(file) :
        print(i, v, type(v))


def createTempFile(suffex=""):
    file = tempfile.mkstemp(suffix=suffex, prefix='tmp', dir=None, text=False)
#     __debugContent__(file)
    return file[1]


def createTempDir():
    dir = tempfile.mkdtemp(suffix='', prefix='tmp', dir=None)
#     __debugContent__(dir)
    return dir[1]



if __name__ == '__main__' :
    print("done...")
    