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
    
    

def __test_pattern_match_group_key():
    string = "The quick brown fox jumps over the lazy dog"
    string_list = string.split()
    print(string_list)
    pattern = re.compile(r"(?P<match_word>the)", re.I)
    print("Output #39:")
    for word in string_list :
        mth = pattern.search(word)
        if mth :
            print("{:s}".format(mth.group("match_word")))



if __name__ == '__main__' :
    __test_pattern_match_group_key()
    print("done...")














