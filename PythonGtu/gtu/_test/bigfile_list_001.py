import io
from gtu.io import stringIoUtil
from gtu.string import stringUtil
from gtu.io import LogWriter
from gtu.io import fileUtil
from gtu.collection import listUtil



def main() :
    fileLst = list()
    filePath = "/media/gtu001/GTU500G"

    print("1---------------------")

    fileUtil.searchFilefind(filePath, r".*\.mp4", fileLst, debug=False, ignoreSubFileNameLst=None)

    print("2---------------------")

    def sortFunc(a, b) :
        as1 = fileUtil.length(a)
        bs1 = fileUtil.length(b)
        val = None
        if as1 < bs1 :
            val = -1
        elif bs1 > as1 :
            val = 1
        else :
            val = 0
        val = val * -1
        return val

    print("3---------------------")

    listUtil.sort(fileLst, sortFunc)

    print("4---------------------")

    for i in range(0, 100) :
        if len(fileLst) - 1 <= i :
            print(fileLst[i])




if __name__ == '__main__' :
    main()
    print("done...")
