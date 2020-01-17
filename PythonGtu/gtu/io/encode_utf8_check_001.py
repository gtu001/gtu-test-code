
import sys
from gtu.io import fileUtil
from gtu.error import errorHandler
from gtu.regex import regexUtil

'''
from gtu.io import encode_utf8_check_001
'''

def saveValidUTF8File(filepath):
    sb = ''
    with open (filepath,"r",encoding="utf-8") as f:
        for i in f:
            for c in i:
                if ord(c) < 128 :
                    # print(c,end="")
                    sb += c
    fileUtil.saveToFile(filepath, sb, "utf8")


def findErrorLineInFile(filepath, errLst) :
    findLineNumber = 127
    linePattern = r'\-{2}'
    # ------
    contentLst = fileUtil.loadFile_asList(filepath, False, False)
    if len(contentLst) < findLineNumber :
        return
    if regexUtil.find(linePattern, False, contentLst[findLineNumber - 1]) :
        # raise Exception("ERR File : " + filepath)
        print("\t [ERR File] : ",filepath)
        errLst.append(filepath)


if __name__ == '__main__':

    xmlChkDirs = [
        'D:/work_tool/20200114_sister_workspace/cashportal',
        'D:/work_tool/20200114_sister_workspace/cashweb',
        'D:/work_tool/20200114_sister_workspace/cashWebServiceClient',
        'D:/work_tool/20200114_sister_workspace/framework',
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
            findErrorLineInFile(f, errLst)
        except Exception as ex :
            print("ERR File : ", f)
            errorHandler.printStackTrace2(ex)
            break

    print("Start =============================")
    for i,f in enumerate(errLst) :
        print("ERR FIle ", f)
    print("End   =============================")

    print("done..")
	