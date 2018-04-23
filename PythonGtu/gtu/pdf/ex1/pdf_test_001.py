import PyPDF2
import urllib.request

from gtu.reflect import checkSelf

'''
Document Here
https://pythonhosted.org/PyPDF2/
'''

file = open('E:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/pdf/ex1/RCRP0S102.pdf', 'rb')

fileReader = PyPDF2.PdfFileReader(file)

pageCount = fileReader.getNumPages()


for i in range(0 , fileReader.getNumPages()) :
    page = fileReader.getPage(i)
#     print(page)
    
#     checkSelf.checkMatchPtnMembers(page, ".*", doc=False, ignoreInherit=True)
#     break

    str = page.extractText()
    str = str.encode(encoding='utf_8', errors='strict')
    print(str)