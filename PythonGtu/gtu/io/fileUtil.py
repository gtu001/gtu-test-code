
import os
from os.path import expanduser
from pathlib import Path

### from gtu.io import fileUtil


def mkdirs(path):
	'''建立多個目錄'''
	arry = path.split(os.sep)
	url = ''
	for i in range(0, len(arry)):
		url += arry[i] + os.sep
		if not os.path.exists(url):
			os.mkdir(url)
			print("mkdir ", url) 


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
	f = open(file_name,'r')
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
	if os.path.isdir(home + os.sep + "OneDrive" + os.sep + "Desktop" + os.sep):
		return home + os.sep + "OneDrive" + os.sep + "Desktop" + os.sep
	else :
		return home + os.sep + "Desktop" + os.sep
	
	
def getAbsPath(path):
	'''取得絕對路徑'''
	return Path(path).resolve()


def getName(path):
	'''取得檔名'''
	fullName = str(path.resolve())
	name = fullName[fullName.rfind(os.sep) + 1:]
	return name


def getSubName(path):
	'''取得副檔名'''
	fullName = str(path.resolve())
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
	f = open(filePath, "w", encoding = 'utf8')
	f.write(content)
	f.flush()
	f.close()


def saveToFileBytes(filePath, byteArry):
	f = open(filePath, "wb")
	f.write(byteArry)
	f.flush()
	f.close()
	

def loadFile(filePath):
	file = open(filePath, "r", encoding = 'utf8')
	return file.read() 


def loadFileBytes(filePath):
	file = open(filePath, "rb") 
	return file.read() 


if __name__ == '__main__':
	print(copyFile("C:/Users/Administrator/Desktop/southpark.rar", "c:/southpark.rar"))
	print("done..")
	
	
	