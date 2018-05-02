import re
from gtu.reflect import checkSelf

'''
from gtu.regex import regexUtil
'''

def find(pattern, multiLine, scanString):
    flags = 0
    if multiLine:
        flags |= re.DOTALL | re.MULTILINE
    ptn = re.compile(pattern, flags)
    mth = ptn.search(scanString)
    if mth is None :
        return False
    else:
        return True


def match(pattern, multiLine, scanString):
    flags = 0
    if multiLine:
        flags |= re.DOTALL | re.MULTILINE
    ptn = re.compile(pattern, flags)
    mth = ptn.match(scanString)
    if mth is None :
        return False
    else:
        return True
    
    
    
if __name__ == '__main__' :
    print(find(r"加率‰", True, "str: (4)增加率‰"))
    print("done...")
