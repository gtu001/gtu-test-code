from gtu.io import fileUtil
import sys

try:
    import xml.etree.cElementTree as ET
except ImportError:
    import xml.etree.ElementTree as ET
    
    
    
def testReadExample():
    tree = ET.ElementTree(file='example001.xml')
    
    root = tree.getroot()
    tag = root.tag
    attr = root.attrib
    
    print(tree)
    print(root)
    print(tag)
    print(attr)
    
    for child_of_root in root:
        print(child_of_root.tag, child_of_root.attrib)
        
    print("root[0].tag=[{}], root[0].text=[{}]".format(root[0].tag, root[0].text))
    
    for elem in tree.iter():
        print(elem.tag, elem.attrib)
        
    for elem in tree.iter(tag='branch'):
        print(elem.tag, elem.attrib)
        
    for elem in tree.iterfind('branch/sub-branch'):
        print(elem.tag, elem.attrib)
        
    for elem in tree.iterfind('branch[@name="release01"]'):
        print(elem.tag, elem.attrib)
        

def testWriteExample():
    fileUtil.copyFile("example001.xml", "example001s.xml")
    
    tree = ET.ElementTree(file='example001s.xml')
    root = tree.getroot()
    del root[2]
    root[0].set('foo', 'bar')
    for subelem in root:
        print(subelem.tag, subelem.attrib)
    
    tree.write(sys.stdout)
        

if __name__ == '__main__' :
#     testReadExample()
    testWriteExample()
    print("done...")
    
    
    
    
    