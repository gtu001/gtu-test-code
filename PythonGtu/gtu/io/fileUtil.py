
import os
from os.path import expanduser
from pathlib import Path
from gtu.thread_test import threadUtil
from gtu.reflect import checkSelf
import re
from gtu.string import stringUtil
import sys
import collections


'''
from gtu.io import fileUtil

| 操作模式 | 具体含义                         |
| -------- | -------------------------------- |
| `'r'`    | 读取 （默认）                    |
| `'w'`    | 写入（会先截断之前的内容）       |
| `'x'`    | 写入，如果文件已经存在会产生异常 |
| `'a'`    | 追加，将内容写入到已有文件的末尾 |
| `'b'`    | 二进制模式                       |
| `'t'`    | 文本模式（默认）                 |
| `'+'`    | 更新（既可以读又可以写）         |

'''


def mkdirs(path):
	'''建立多個目錄'''
	os.makedirs(path, mode=0o777, exist_ok=True)
	return os.path.exists(path) and os.path.isdir(path)


def exeCommand(cmdStr):
	'''執行command'''
	os.system(cmdStr)
	
	
def get_line_number(phrase, file_name):
	'''取得資料所在行數'''
	with open(file_name) as f:
		for i, line in enumerate(f, 1):
			if phrase in line:
				return i
	return -1


def get_line_number2(phrase, file_name):
	'''取得資料所在行數'''
	f = open(file_name, 'r')
	line_num = 0
	for line in f.readlines():
		line_num += 1
		if line.find(phrase) >= 0:
			return line_num
	return -1
	
	
def getDesktopDir(fileName=''):
	'''取得桌面路徑'''
# 	home = str(Path.home())
	home = expanduser("~")
	if sys.platform != 'linux' :
		if os.path.isdir(home + os.sep + "OneDrive" + os.sep + "Desktop" + os.sep):
			return home + os.sep + "OneDrive" + os.sep + "Desktop" + os.sep + fileName
		else :
			return home + os.sep + "Desktop" + os.sep + fileName
	else :
		return "/home/gtu001/桌面" + os.sep + fileName
	
	
def getAbsPath(path):
	'''取得絕對路徑'''
	'''return os.path.abspath(path)'''
	return Path(path).resolve()


def getName(path):
	'''取得檔名'''
	if False :
		if type(path).__name__ == 'str' :
			path = Path(path)
		fullName = str(path.resolve())
		name = fullName[fullName.rfind(os.sep) + 1:]
		return name
	elif True :
		import ntpath
		return ntpath.basename(path)


def getNoSubName(path):
	'''取得無附檔名黨名'''
	fullName = getName(path)
	name = fullName[0 : fullName.rfind(".")]
	return name


def getSubName(path):
	'''取得副檔名'''
	fullName = getName(path)
	name = fullName[fullName.rfind(".") + 1:]
	return name


def copyFile(fromFile, toFile):
	'''寫檔'''
	tof = open(toFile, 'wb', buffering=30)
	with open(fromFile, 'rb', buffering=30) as f:
		while 1:
			b = f.read()
			if not b : break
			tof.write(b)
		f.flush()
		tof.flush()
		tof.close()


def copyDir(fromDir, toDir):
	'''複製目錄'''
	fromPath = Path(fromDir)
	if fromPath.is_dir():
		for i, f1 in enumerate(fromPath.iterdir(), 0):
			newToDir = Path(toDir, getName(f1))
			if f1.is_dir():
				copyDir(f1, newToDir) 
			elif f1.is_file():
				print("copy from: {}, to: {}".format(f1, newToDir))
				mkdirs(str(newToDir.parent))
				copyFile(str(f1), str(newToDir))


def saveToFile(filePath, content, encode):
	f = open(filePath, "w", encoding=encode)
	f.write(content)
	f.flush()
	f.close()


def saveToFileBytes(filePath, byteArry):
	f = open(filePath, "wb")
	f.write(byteArry)
	f.flush()
	f.close()
	

def loadFile(filePath):
	file = open(filePath, "r", encoding='utf8')
	return file.read() 


def loadFileBytes(filePath):
	file = open(filePath, "rb") 
	return file.read() 


def loadFile_asList(filePath, trim=False, distinct=False) :
	lst = list()
	if distinct :
		lst = collections.OrderedDict()
	fs = open(filePath, "r", encoding='utf8')
	for line in fs.readlines() :
		if trim :
			line = line.strip()
		line = re.sub(r"\n+$", "", line)
		if not distinct :
			lst.append(line)
		else:
			lst[line] = ""
	if not distinct :
		return lst
	else:
		return list(lst.keys())

	


def getCurrentDir(filename=None):
	''' filename 呼叫端要傳 __file__ '''
	''' print(os.getcwd()) '''
	if filename is None :
		(threadId, filename, lineno, name, line) = threadUtil.getCurrentRunning(ignorePy=[__file__])
	return (os.path.dirname(os.path.abspath(filename))) + os.sep




def getDir(file):
	if Path(file).is_dir():
		return file
	return os.path.dirname(file)
   

'''
讀取字串變數, by line
'''


def readStringVariableByLine(textData):
	for line in textData.splitlines():
	    print(line)
	    

def __searchFileIgnoreSubFileNameLst__(name, ignoreSubFileNameLst) :
	if ignoreSubFileNameLst :
		name = name[name.rfind(".") + 1:]
		for subName in ignoreSubFileNameLst :
			if name.lower() == subName.lower() :
				return True
	return False


def searchFilefind(file, pattern, fileList, debug=False, ignoreSubFileNameLst=None):
	if type(pattern).__name__ == 'str':
		pattern = re.compile(pattern, re.IGNORECASE)
	
	file = Path(file)
	currentDir = getDir(file)
	
	if file.is_dir() :
		listFile = os.listdir(file)
		for i, f in enumerate(listFile, 0):
			f = Path(currentDir , f)
			searchFilefind(f, pattern, fileList, debug, ignoreSubFileNameLst)
	elif file.is_file() :
		name = getName(file)

		if __searchFileIgnoreSubFileNameLst__(name, ignoreSubFileNameLst) :
			if debug :
				print("忽略附檔名 :", file)
		else :
			mth = pattern.search(name)
		
			if mth is not None :
				if debug :
					print("符合 :", file)
				fileList.append(file.resolve())
			elif debug :
				print("不符合 :", file)
		


def searchFileMatchs(file, pattern, fileList, debug=False, ignoreSubFileNameLst=None):
	if type(pattern).__name__ == 'str':
		pattern = re.compile(pattern, re.IGNORECASE)
	
	file = Path(file)
	currentDir = getDir(file)
	
	if file.is_dir() :
		listFile = os.listdir(file)
		for i, f in enumerate(listFile, 0):
			f = Path(currentDir , f)
			searchFileMatchs(f, pattern, fileList, debug, ignoreSubFileNameLst)
	elif file.is_file() :
		name = getName(file)

		if __searchFileIgnoreSubFileNameLst__(name, ignoreSubFileNameLst) :
			if debug :
				print("忽略附檔名 :", file)
		else :
			mth = pattern.match(name)
			
			if mth is not None :
				if debug :
					print("符合 :", file)
				fileList.append(file.resolve())
			elif debug :
				print("不符合 :", file)


def sep():
	'''取得檔案路徑分隔符號'''
	req_sep = "/"
	if os.sep == '\\' :
		req_sep = '\\\\'
	else:
		req_sep = '\/'
	return req_sep


def replaceBasePath(orign, from_path, to_path):
	'''換掉前置目錄'''
	req_sep = sep()
	
	orign = re.sub(r"[\\/]", req_sep, orign)
	from_path = re.sub(r"[\\/]", req_sep, from_path)
	to_path = re.sub(r"[\\/]", req_sep, to_path)
	
	pos = orign.find(from_path)
	
	if pos == -1:
		raise Exception(stringUtil.concat("無法取得前置目錄  : ", orign, " 找不到  ", from_path))
	
	return os.path.abspath(to_path + os.sep + orign[orign.find(from_path) + len(from_path) : ])


def canRead(filepath):
	from os import access, R_OK
	from os.path import isfile
	return isfile(filepath) and access(filepath, R_OK)


def listFileOnly(dirpath):
	from os import listdir
	from os.path import isfile, join
	onlyfiles = [f for f in listdir(dirpath) if isfile(join(dirpath, f))]
	onlyfiles2 = list()
	for (i, fname) in enumerate(onlyfiles) :
		f2 = join(dirpath, fname)
# 		print(i, type(f2), f2)
		onlyfiles2.append(f2)
	return onlyfiles2


def listFile_example(dirpath):
	from os import walk
	f = list()
	for (dirpath, dirnames, filenames) in walk(dirpath):
	    f.extend(filenames)


def listFile_example2(dirpath):
	'''
	推薦這個
	'''
	from os import walk
	import os
	for (dirpath, dirnames, filenames) in walk(dirpath):
		for (i, name) in enumerate(filenames) :
			old_file = os.path.join(dirpath, name)
			print(i, old_file)
                
                
def listFile_example3():
	import glob
	print(glob.glob("/home/adam/*.txt"))
	
	
	
	
def deleteDir(dirPath):
	import shutil
	shutil.rmtree(dirPath)
	
	
	
def rename(fromDir, fromName, toDir, toName):
	old_file = os.path.join(fromDir, fromName)
	new_file = os.path.join(toDir, toName)
	os.rename(old_file, new_file)



if __name__ == '__main__':
	dirPath = "C:/Users/E123474/Desktop/FundQryRoiWebRequestDto.txt"
	lst = loadFile_asList(dirPath, distinct=True)
	for i,v in enumerate(lst) :
		print(i, v)
	print("done..")
	
