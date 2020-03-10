from gtu.io import fileUtil
import sys
from gtu.reflect import checkSelf


try:
    import xml.etree.cElementTree as ET
except ImportError:
    import xml.etree.ElementTree as ET
    
    

def testReadExample(filePath):
    tree = ET.ElementTree(file=filePath)

    # checkSelf.checkMembersToHtml(tree, fileName="ET_Tree")
    
    for i, e in enumerate(tree.getroot().iterfind("string")):
        print(e.tag, e.attrib['name'], e.text)
        

        

if __name__ == '__main__' :
    filePath = "/media/gtu001/OLD_D/workstuff/andy_Projects/repository1/EupGobal/app/src/main/res/values/strings.xml"
    testReadExample(filePath)
    print("done...")
    
    
    
    
    


    