
from inspect import getmembers
import inspect
import re
from gtu.io import fileUtil

''' import 這行
from gtu.reflect import checkSelf
'''


def checkMembersToHtml(testObj, fileName=None) :
    '''檢查物件成員'''
    def getDiv(html, tag='div', id=None, header='') :
        if id is not None :
            id = "id=\"" + id + "\""
        else :
            id = ""
        return "<" + tag + " " + id + " style='font-family: Consolas'>" + header + html + "</" + tag + ">"
    def replaceNbsp(strVal) :
        def getNbspStr(strVal2) : 
            return strVal2.replace(' ', '&nbsp;')
        matchList = re.finditer('\<\w+\s.*?\>', strVal)
        curPos = 0
        rtnStr = ""
        for mth in matchList:
            rtnStr += getNbspStr(strVal[curPos : mth.start()])
            rtnStr += mth.group()
            curPos = mth.end()
        rtnStr += getNbspStr(strVal[curPos :])
        return rtnStr
    def getDoc(obj) :
        strVal = inspect.getdoc(obj)
        if strVal :
            return replaceNbsp(strVal).replace('\n', '<br/>\n')
        else:
            return "NA"
    htmlHeader = '''
    <head>
        <script text="text/javascript">
            function search() {
                var value = document.querySelector(".search").value.toLowerCase();
                var aArry = document.querySelectorAll("li");
                for(var ii = 0 ; ii < aArry.length ; ii ++){
                    aArry[ii].style.display = 'block';
                }
                for(var ii = 0 ; ii < aArry.length ; ii ++){
                    var id = aArry[ii].getAttribute("id").replace(/^li\_/, '').toLowerCase();
                    if(id.indexOf(value) == -1) {
                        aArry[ii].style.display = 'none';
                    }
                }
                var aArry = document.querySelectorAll("div");
                for(var ii = 0 ; ii < aArry.length ; ii ++){
                    aArry[ii].style.display = 'block';
                }
                for(var ii = 0 ; ii < aArry.length ; ii ++){
                    if(aArry[ii].getAttribute('id') && aArry[ii].getAttribute('id').toLowerCase().indexOf(value) == -1) {
                        aArry[ii].style.display = 'none';
                    }
                }
            }
            document.addEventListener("DOMContentLoaded", function(){
                var aa = document.querySelectorAll("a");
                for(var ii = 0 ; ii < aa.length ; ii ++){
                    if(aa[ii].getAttribute("href")) {
                        aa[ii].addEventListener("click", function() {
                            var href = this.getAttribute("href").replace(/^\#/, "");
                            document.querySelector("#" + href).scrollIntoView(true);
                        });
                    }
                }
            });
        </script>
    </head>
    '''
    if not fileName :
        fileName = getClassFullName(testObj)
    print("testObj ==> ", getClassFullName(testObj))
    dlist = inspect.getmembers(testObj)
    htmlDoc = ''
    html = "<html>"
    html += htmlHeader
    html += "<body>"
    html += "<input type='text' class='search' onblur='javascript:search();' /><br/><br/>"
    html += "<ul>"
    header = "<a id='{0}'><h3><font color='red'>{1}</font></h3></a>"
    for i, mem in enumerate(dlist, 0):        
        print("<<", i, ">>", mem[0], "\t", mem[1])
        html += "<li id='li_{0}'><a href='#{0}'>{1}</a></li>\n".format(mem[0], getDiv(mem[0], tag='span'))
        headerContent = header.format(mem[0], getDiv(mem[0]))
        divContent = "<br/>{0}<br/><br/><br/>\n\n".format(getDoc(mem[1]))
        htmlDoc += getDiv(divContent, id=mem[0], header=headerContent)
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