from enum import Enum
import io
import os
from pathlib import Path
import re
from tkinter import Text, Button, Label
from tkinter.filedialog import askopenfilename

from gtu._tkinter.util import tkinterUtil
from gtu._tkinter.util import tkinterUtil 
from gtu._tkinter.util.tkinterUIUtil import _Label 
from gtu._tkinter.util.tkinterUIUtil import _Text 
from gtu.decode import decodeUtil
from gtu.enum import enumUtil
from gtu.enum.enumUtil import EnumHelper
from gtu.error import errorHandler
from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.os import runtimeUtil
from gtu.reflect import checkSelf
from gtu.string import stringUtil
import tkinter as tk


class MainUI():
    
    def __init__(self):
        win = tk.Tk()
        win.title("取得相對位置")
        
        #------------------------------row 1
        
        l1 = Label(win, text="根目錄")
        l1.grid(column=0, row=0)
        
        self.dirText = _Text.create(win)
        self.dirText.grid(column=1, row=0)
        self._dirText = _Text(self.dirText)
        self._dirText.dbclick(self.dirTextClick)
        
        b1 = Button(win, text="filter目錄", command=self.filterDirBtnAction)
        b1.grid(column=2, row=0)
        
        #------------------------------row 2
        
        l1 = Label(win, text="來源ts")
        l1.grid(column=0, row=1)
        
        self.srcTsText = _Text.create(win)
        self.srcTsText.grid(column=1, row=1)
        self._srcTsText = _Text(self.srcTsText)
        
        #------------------------------row 3
        
        l2 = Label(win, text="目的ts")
        l2.grid(column=0, row=2)
        
        self.targetTsText = _Text.create(win)
        self.targetTsText.grid(column=1, row=2)
        self._targetTsText = _Text(self.targetTsText)
        
        b2 = Button(win, text="取得對應路徑", command=self.fetchRelativePathAction)
        b2.grid(column=2, row=2)
        
        #------------------------------row 4
        
        tkinterUtil.centerWin(win)
        win.mainloop()


    def dirTextClick(self, event):
        try:
            dirPath = tkinterUtil.askdirectory()
            if dirPath:
                self._dirText.setText(str(dirPath))
        except Exception as ex:
            tkinterUtil.error_ex(ex, ex)
    
    
    def filterDirBtnAction(self):
        try:
            dirPath = self._dirText.getText()
            self.fileLst = list()
            self.__searchFilefind(dirPath, r"\.*\.ts", self.fileLst)
            tkinterUtil.message("取得ts檔", "檔案數:" + str(len(self.fileLst)))
        except Exception as ex:
            tkinterUtil.error_ex(ex, ex)
    
    
    def __searchFilefind(self, file, pattern, fileList):
        if type(pattern).__name__ == 'str':
            pattern = re.compile(pattern, re.IGNORECASE)
        
        file = Path(file)
        currentDir = fileUtil.getDir(file)
        
        if file.is_dir() and fileUtil.getName(file) != 'node_modules':
            listFile = os.listdir(file)
            for i, f in enumerate(listFile, 0):
                f = Path(currentDir , f)
                self.__searchFilefind(f, pattern, fileList)
        elif file.is_file() :
            name = fileUtil.getName(file)
            mth = pattern.search(name)
            
            if mth is not None :
                fileList.append(file.resolve())
                
                
    def getFile(self, fileName, label):
        for (i, row) in enumerate(self.fileLst) :
            if fileUtil.getName(row) == fileUtil.getName(fileName) :
                return row
        raise Exception(label + "找不到檔案 :" + fileName)
    
    
    def fetchRelativePathAction(self):
        try:
            srcFile = self.getFile(self._srcTsText.getText(), "來源")
            targetFile = self.getFile(self._targetTsText.getText(), "目的")
            
            v = RelativePathGetter(str(srcFile), str(targetFile))
            tkinterUtil.prompt("最終結果", "取得相對路徑:", v.finalResultPath)
        except Exception as ex:
            tkinterUtil.error_ex(ex, ex)


class RelativePathGetter :

    def __init__(self, srcPath, toPath):
        print("orign srcPath = ", srcPath)
        print("orign toPath  = ", toPath)
         
        self.srcLst = self.getPathLst(srcPath)
        self.toLst = self.getPathLst(toPath)
        self.initGetSameParent()
        
        self.srcSep = OsSepEnum.getPathsepType(srcPath)
        self.toSep = OsSepEnum.getPathsepType(toPath)
        
        samePathLen = len(self.sameParentPath) + 1
        self.srcPath_compare = srcPath[samePathLen:]
        self.toPath_compare = toPath[samePathLen:]
        
        prefix = self.src_path_process()
        print("prefix : " + prefix)
        
        self.finalResultPath = prefix + "/" + self.toPath_compare.replace("\\", "/")
    
    
        
    def src_path_process(self):
        count = self.srcPath_compare.count(self.srcSep.sep)
        rtnStr = ""
        for i in range(0, count):
            rtnStr += "../"
        return rtnStr
        
        
    def initGetSameParent(self):
        tempPos = -1
        for i in range(0, len(self.srcLst)):
            if self.srcLst[i] == self.toLst[i] :
                tempPos = i
            else :
                break
        self.sameParentPath = self.srcLst[tempPos];
    
    
    def getPathLst(self, path):
        lst = list()
        for i in range(0, len(path)):
            if path[i] in (os.sep , '\\', '/') : 
                print("->", path[0:i])
                lst.append(path[0:i])
        return lst



class OsSepEnum(Enum):
    seqDefault = (os.sep)
    seqWin = ('\\')
    seqLinux = ('/')
    
    def __init__(self, sep):
        self.sep = sep
    
    @staticmethod
    def getPathsepType(path):
        map = dict()
        def callFunc(i, member):
            map[path.count(member.sep)] =  member
        
        enumHelper = EnumHelper("gtu._tkinter.ex.ex6.angular_relative_path_detect_001.OsSepEnum")
        enumHelper.loopAll(callFunc)
        return map[max(map.keys())]


if __name__ == '__main__':
#     p1 = "D:/workstuff/gtu-test-code/GTU/angular/src/app/app.component.ts"
#     p2 = "D:/workstuff/gtu-test-code/GTU/angular/src/app/gtu/utils/sanitize_url.component.ts"
#     v = RelativePathGetter(p1, p2)
#     print(v.finalResultPath)

    MainUI()
    print("done..")

