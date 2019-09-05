
from inspect import getmembers
import inspect
import re
from gtu.io import fileUtil

''' import 這行
from gtu.reflect import checkSelf
'''


def checkMembersToHtml(testObj, fileName) :
    '''檢查物件成員'''
    def getDiv(html) :
        return "<div style='font-family: Consolas'>" + html + "</div>"
    def getDoc(obj) :
        strVal = inspect.getdoc(obj)
        if strVal :
            return strVal.replace('\n', '<br/>\n').replace(' ', '&nbsp;')
        else:
            return "NA"
    print("testObj ==> ", getClassFullName(testObj))
    dlist = inspect.getmembers(testObj)
    htmlDoc = ''
    html = "<html><body>"
    html += "<ul>"
    for i, mem in enumerate(dlist, 0):        
        print("<<", i, ">>", mem[0], "\t", mem[1])
        html += "<li><a href='#{0}'>{0}</a></li>\n".format(mem[0])
        htmlDoc += "<a id='{0}'><h3><font color='red'>{0}</font></h3></a><br/>{1}<br/><br/><br/>\n\n".format(getDiv(mem[0]), getDiv(getDoc(mem[1])))
    html += "</ul>\n"
    html += "<br/><br/>\n"
    html += htmlDoc
    html += '</body></html>\n'
    fileUtil.saveToFile(fileUtil.getDesktopDir() + fileName + ".html", html, 'UTF8')



def checkMembers(testObj, doc=False, ignorePrivate=True, ignoreInherit=True):
    '''檢查物件成員'''
    print("testObj ==> ", getClassFullName(testObj))
    dlist = inspect.getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        if ignoreInherit and __isInheritMember(mem[0]) :
            continue
        if ignorePrivate and __isPrivateMember(mem[0]) :
            continue
        
        print("<<", i, ">>", mem[0], "\t", mem[1])
        if doc:
            print(inspect.getdoc(mem[1]))
            __printLine()
            


def checkLikeMembers(testObj, name, doc=False, ignorePrivate=True, ignoreInherit=True):
    '''找出接近的成員'''
    print("testObj ==> ", getClassFullName(testObj))
    findOk = False
    name = name.lower()
    dlist = inspect.getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        if ignoreInherit and __isInheritMember(mem[0]) :
            continue
        if ignorePrivate and __isPrivateMember(mem[0]) :
            continue
        
        if name in mem[0].lower() :
            print("<<", i, ">>", mem[0], "\t", mem[1])
            findOk = True
            if doc:
                print(inspect.getdoc(mem[1]))
                __printLine()
    if findOk == False :
        print("找不到類似成員!")
        
        

def checkMatchPtnMembers(testObj, pattern, doc=False, ignorePrivate=True, ignoreInherit=True):
    '''找出接近的成員'''
    print("testObj ==> ", getClassFullName(testObj))
    findOk = False
    dlist = inspect.getmembers(testObj)
    for i, mem in enumerate(dlist, 0):
        if ignoreInherit and __isInheritMember(mem[0]) :
            continue
        if ignorePrivate and __isPrivateMember(mem[0]) :
            continue
        
        mth = re.match(pattern, mem[0])
        if mth:
            print("<<", i, ">>", mem[0], "\t", mem[1])
            findOk = True
            if doc:
                print(inspect.getdoc(mem[1]))
                __printLine()
    if findOk == False :
        print("找不到類似成員!")
        
        

def document(testObj):
    help(testObj)
    
    

def __printLine():    
    str = ""
    for i in range(0, 200):
        str = str + "-"
    print(str)
        
        

def __isInheritMember(name):
    mth = re.match(r"^__\w+__$", name)
    if mth :
        return True
    return False


def __isPrivateMember(name):
    mth = re.match(r"^_\w+$", name)
    if mth :
        return True
    return False
        
        
def getClassFullName(o):
    module = "<module not found>"
    if hasattr(o, "__module__") : 
        module = o.__module__
    return module + "." + o.__class__.__qualname__
        
        

if __name__ == '__main__':
    import inspect
    checkMembers(inspect)