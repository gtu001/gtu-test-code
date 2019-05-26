from enum import Enum
from gtu.reflect import checkSelf
import re
from gtu.io import fileUtil


def main():
    file_name = "/home/gtu001/Dropbox/Apps/gtu001_test/etc_config/EnglishTester_new_GodImage.txt"
    file_to_name = "/home/gtu001/Dropbox/Apps/gtu001_test/etc_config/EnglishTester_new_GodImage.htm"
    tof = open(file_to_name, 'w', buffering=30)
    with open(file_name) as f:
        for i, line in enumerate(f, 1):
            tof.write("<img src=\"" + line.replace("\n", "") + "\"/>\n")
    



if __name__ == '__main__' :
    main()
    print("done...")





