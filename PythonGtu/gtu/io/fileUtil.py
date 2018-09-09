
import os
from os.path import expanduser
from pathlib import Path
from gtu.thread import threadUtil
from gtu.reflect import checkSelf
import re
from gtu.string import stringUtil
import sys

'''
from gtu.io import fileUtil
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
	
	
def getDesktopDir():
	'''取得桌面路徑'''
# 	home = str(Path.home())
	home = expanduser("~")
	if sys.platform != 'linux' :
		if os.path.isdir(home + os.sep + "OneDrive" + os.sep + "Desktop" + os.sep):
			return home + os.sep + "OneDrive" + os.sep + "Desktop" + os.sep
		else :
			return home + os.sep + "Desktop" + os.sep
	else :
		return "/home/gtu001/桌面"
	
	
	
def getAbsPath(path):
	'''取得絕對路徑'''
	'''return os.path.abspath(path)'''
	return Path(path).resolve()


def getName(path):
	'''取得檔名'''
	if type(path).__name__ == 'str' :
		path = Path(path)
	fullName = str(path.resolve())
	name = fullName[fullName.rfind(os.sep) + 1:]
	return name


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


def saveToFile(filePath, content):
	f = open(filePath, "w", encoding='utf8')
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


def getCurrentDir():
	''' print(os.getcwd()) '''
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
	    
	    

def searchFilefind(file, pattern, fileList):
	if type(pattern).__name__ == 'str':
		pattern = re.compile(pattern, re.IGNORECASE)
	
	file = Path(file)
	currentDir = getDir(file)
	
	if file.is_dir() :
		listFile = os.listdir(file)
		for i, f in enumerate(listFile, 0):
			f = Path(currentDir , f)
			searchFilefind(f, pattern, fileList)
	elif file.is_file() :
		name = getName(file)
		mth = pattern.search(name)
		
		if mth is not None :
			fileList.append(file.resolve())
			
			


def searchFileMatchs(file, pattern, fileList):
	if type(pattern).__name__ == 'str':
		pattern = re.compile(pattern, re.IGNORECASE)
	
	file = Path(file)
	currentDir = getDir(file)
	
	if file.is_dir() :
		listFile = os.listdir(file)
		for i, f in enumerate(listFile, 0):
			f = Path(currentDir , f)
			searchFilefind(f, pattern, fileList)
	elif file.is_file() :
		name = getName(file)
		mth = pattern.match(name)
		
		if mth is not None :
			fileList.append(file.resolve())
			


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
	
	return os.path.abspath(to_path  + os.sep + orign[orign.find(from_path) + len(from_path) : ])



def canRead(filepath):
	from os import access, R_OK
	from os.path import isfile
	return isfile(filepath) and access(filepath, R_OK)


if __name__ == '__main__':
	
	file = "C:/Users/gtu00/OneDrive/Desktop/秀娟0501"
	print(replaceBasePath(file, "C:/Users/gtu00/OneDrive/DeskXXtop", "C:/Users/gtu00/OneDrive/Desktop/VVV"))
	
	print("done..")
	
