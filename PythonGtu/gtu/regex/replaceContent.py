import re


def replaceContent(pattern, text, rep):
	'''替換match的內容, 可自訂替換的邏輯'''
	matchList = re.finditer(pattern, text)
	curPos = 0
	rtnStr = ""
	for mth in matchList:
		matchStr = mth.group()
		rtnStr += text[curPos : mth.start()]
		rtnStr += rep.replaceTo(rep, matchStr)
		curPos = mth.end()
	rtnStr += text[curPos :]
	return rtnStr
	
	
class DefaultReplace:
	'''for replaceContent 參數rep, 可實作邏輯'''
	def replaceTo(self, matchStr):
		return matchStr


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

		
if __name__ == '__main__' :
# 	rtn = replaceContent("a", "111 fsadfsdf asdfasfdsff asfdfasdfsa asdfadsfsdf ", DefaultReplace)
# 	print(rtn)
	print(testFind001("([d])", "1234567890"))

