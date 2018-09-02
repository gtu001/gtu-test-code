

import os
import re

from path import Path
from gtu.io import fileUtil
from gtu.string import stringUtil
from gtu.regex import regexReplace

'''
from gtu.buildexe import buildexe_import_scan_001
'''


class _ImportFileFilter():

    def __init__(self, basePath):
        self.basePath = basePath
        self.st = set()
        
    def readImport(self, file) :
        f = open(file, "r", encoding='utf8')
        for i, line in enumerate(f, 1):
            if line.startswith("from ") :
                line = line.replace("\n", "")
                truePath = self.importToRealPath(line)
                if truePath != "" and truePath not in self.st :
                    self.st.add(truePath)
                    self.readImport(truePath)
                
    def importToRealPath(self, importPath):
        ptn = re.compile(r"from\s+(?P<prefix>.*?)\s+import\s+(?P<clzName>.*)", re.I)
        mth = ptn.search(importPath)
        if mth is not None :
            relativePath = (mth.group("prefix") + "." + mth.group("clzName")).replace(".", os.sep) + ".py"
            realPath = self.basePath + os.sep + relativePath
            if os.path.exists(realPath) :
                return os.path.abspath(realPath)
        return "";
        
        
class _ImportFileFilter_Exclude():

    def __init__(self, fileList):
        self.fileList = fileList
        
        st = set()
        for (i, v) in enumerate(self.fileList) :
            self.grepImport(v, st)
            
        self.orignSt = st
        
        self.excludeSt = set()
        for (i, v) in enumerate(self.orignSt) :
#             print(i, v, "\t\t", self.parseToExclude(v))
            arry = self.parseToExclude(v)
            if len(arry) != 0:
                self.excludeSt.update(arry)
    
    def grepImport(self, file, st):
        f = open(file, "r", encoding='utf8')
        for i, line in enumerate(f, 1):
            line = line.strip()
            if line.startswith("from ") or line.startswith("import ") or stringUtil.isBlank(line):
                if not stringUtil.isBlank(line):
                    st.add(line)
                else:
                    return
                
    def _special_parseToExclude(self, line):
        if line.startswith("import") :
            ptn01 = re.compile("import\s+(?P<libs>[\w\,\s]+)")
            mth = ptn01.search(line)
            if mth is not None :
                libs = mth.group("libs")
                if "," in libs :
                    arry = mth.group("libs").split(",")
                    rtnLst = list()
                    for (i, v) in enumerate(arry):
                        v = v.strip()
                        if stringUtil.isNotBlank(v) :
                            rtnLst.append(v)
                    return rtnLst
                
    def parseToExclude(self, line):
        line = line.strip()
        
        # 特別處理  --> import os,platform,logging
        spResult = self._special_parseToExclude(line)
        if spResult is not None :
            return spResult
        
        ptnArry = [
            re.compile("from\s+(?P<lib>\w+)\s+") ,
            re.compile("from\s+(?P<lib>\w+)") ,
            re.compile("import\s+(?P<lib>\w+)\s+") ,
            re.compile("import\s+(?P<lib>\w+)") ,
            ]
        
        for (i, ptn) in enumerate(ptnArry) :
            mth = ptn.search(line)
            if mth is not None :
                lib = mth.group("lib")
                return [lib]
            
    def getExcludes(self):
        return self.excludeSt


def getFilterExcludes(projectBase, targetPy):
    '''取得來源所使用import'''
    t = _ImportFileFilter(projectBase)
    t.readImport(targetPy)
    t.st.add(targetPy)
    t1 = _ImportFileFilter_Exclude(t.st)
    keepNotExcludeSet = t1.getExcludes()
    
    '''取得所有import'''
    fileList = list()
    fileUtil.searchFileMatchs(projectBase, r"^.*\.py$", fileList)
    t2 = _ImportFileFilter_Exclude(fileList)
    allExcludeSet = t2.getExcludes()
    
    '''取得差集合'''
    finalExcludeSet = list(allExcludeSet - keepNotExcludeSet)
    return finalExcludeSet


def getDependencyPyLst(projectBase, targetPy):
    t = _ImportFileFilter(projectBase)
    t.readImport(targetPy)
    t.st.add(targetPy)
    return list(t.st)



def appendPyFile(projectBase, classpath):
    filename = classpath.replace(".", fileUtil.sep()) + ".py" 
    return projectBase + fileUtil.sep() + filename
    


if __name__ == '__main__' :
    projectBase = "E:/workstuff/workspace/gtu-test-code/PythonGtu/"
    targetPy = "E:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/_tkinter/ex/ex5/kill_port_ui_001.py";
    newProjectBase = "E:/workstuff/workspace/gtu-test-code/PythonGtuNew/"
    
    print("mkdir", fileUtil.mkdirs(newProjectBase))
    
    #取得所有excludes
    print(getFilterExcludes(projectBase, targetPy))
     
    #取得相依檔案
    lst = getDependencyPyLst(projectBase, targetPy)
    
    #特別加入
    lst.append(appendPyFile(projectBase, "gtu._tkinter.util.tkinterUIUtil"))
    
    #複製到目的資料夾
    for (i, v) in enumerate(lst) :
        v2 = fileUtil.replaceBasePath(v, projectBase, newProjectBase)
        print("mkdir", fileUtil.mkdirs(os.path.dirname(v2)), v2)
        fileUtil.copyFile(v, v2)

    print("done...")
