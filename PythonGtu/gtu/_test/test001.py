import sys
# sys.path.append("D:\workstuff\gtu-test-code\PythonGtu")

from enum import Enum
from gtu.reflect import checkSelf
import re


class Test001Class :
    def __init__(self):
        print("Test001Class Done!!!")


def main():
    ptn = re.compile("www\.youtube\.com\/watch\?v\=(\w+?)\&")
#     mth = ptn.search("https://www.youtube.com/watch?v=S02GWKdZpbg&t=10486s")
    mth = ptn.search("https://www.youtube.com/watch?v=S02GWKdZpbg")
    if mth is not None :
        print("group1 = ", mth.group(1))
    else :
        print("____")



if __name__ == '__main__' :
    main()
    print("done...")

