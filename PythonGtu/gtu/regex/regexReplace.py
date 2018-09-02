import re

'''
from gtu.regex import regexReplace
'''


def replaceContent(pattern, text, repFunc):
	'''替換match的內容, 可自訂替換的邏輯'''
	matchList = re.finditer(pattern, text)
	curPos = 0
	rtnStr = ""
	for mth in matchList:
		matchStr = mth.group()
		rtnStr += text[curPos : mth.start()]
		rtnStr += repFunc(matchStr)
		curPos = mth.end()
	rtnStr += text[curPos :]
	return rtnStr
	


def testFind001(restr, dataText):
	regexp = re.compile(restr, re.DOTALL | re.MULTILINE)
	matches = regexp.findall(dataText)
	print("matches", matches)
	if matches == False:
		return False
	for m in matches:
		n = regexp.groupindex['test'] - 1
		key = m[n]
		print(n, key)
	return True


def __test_replace():
	'''
	(?i) starts case-insensitive mode
	(?-i) turns off case-insensitive mode
	'''
	line = "interfaceOpDataFile"
	fileIn = "WTF"
	line = re.sub(\
           r"(?i)^.*interfaceOpDataFile.*$", \
           "interfaceOpDataFile %s" % fileIn, \
           line \
           )
	print(line)
		
		
if __name__ == '__main__' :
	'''
		test replaceContent
	'''
# 	def replace(orign):
# 		return "A"
# 	rtn = replaceContent(r"a", "111 fsadfsdf asdfasfdsff asfdfasdfsa asdfadsfsdf ", replace)
# 	print(rtn)
	
	'''
		test testFind001
	'''
# 	print(testFind001("([d])", "1234567890"))

