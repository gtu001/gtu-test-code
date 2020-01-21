
import sys
from gtu.io import fileUtil
from gtu.error import errorHandler
from gtu.regex import regexUtil
import re

'''
from gtu.io import encode_utf8_check_001
'''


def saveValidUTF8File(filepath):
    ptn = re.compile(r"[\u4e00-\u9fa5]", 0)
    sb = ''
    with open (filepath,"r",encoding="utf-8") as f:
        for i in f:
            for c in i:
                if ord(c) < 128 :
                    sb += c
                elif ptn.search(c) :
                    sb += c
                else :
                    sb += c
    fileUtil.saveToFile(filepath, sb, "utf8")


def findErrorLineInFile(filepath, errLst) :
    findLineNumber = 125
    linePattern = r'\-{2}'
    # ------
    contentLst = fileUtil.loadFile_asList(filepath, False, False)
    if len(contentLst) < findLineNumber :
        return

    findOk1 = False
    findOk2 = False

    # if re.search('ibatis', filepath, re.IGNORECASE):
    findOk1 = True

    if regexUtil.find(linePattern, False, contentLst[findLineNumber - 1]) :
        # raise Exception("ERR File : " + filepath)
        print("\t [ERR File] : ",filepath)
        findOk2 = True

    if findOk1 and findOk2 :
        errLst.append(filepath)

if __name__ == '__main__':

    xmlChkDirs = [
        'D:/work_tool/Z-Code/20200114/cashportal',
        'D:/work_tool/Z-Code/20200114/cashweb',
        'D:/work_tool/Z-Code/20200114/cashWebServiceClient',
        'D:/work_tool/Z-Code/20200114/framework',
    ]
    pattern = ".*\.xml$"
    fileList = list() 

    for f in xmlChkDirs :
        fileUtil.searchFileMatchs(f, pattern, fileList)

    errLst = list()

    for i,f in enumerate(fileList) :
        try:
            # print(i, f)
            saveValidUTF8File(f)
            # findErrorLineInFile(f, errLst)
        except Exception as ex :
            print("ERR File : ", f)
            errorHandler.printStackTrace2(ex)
            errLst.append(f)

    print("Start =============================")
    for i,f in enumerate(errLst) :
        print("ERR FIle ", f)
    print("End   =============================")

    print("done..")
	