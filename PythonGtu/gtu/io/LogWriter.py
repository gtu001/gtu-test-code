
from gtu.io import fileUtil
from gtu.string import stringUtil
from gtu.datetime import dateUtil
import datetime

'''
from gtu.io import LogWriter
'''

class LogWriter :
    def __init__(self, file=None, filename="", mode="a") :
        if stringUtil.isBlank(filename) :
            filename = "log_" + dateUtil.formatDatetimeByJavaFormat(datetime.datetime.now(), "yyyyMMdd_HHmmss") + ".txt"
        if file is None :
            file = fileUtil.getDesktopDir(filename)
        self.fs = open(file, mode, encoding='UTF8', buffering=30)
    def write(self, line):
        self.fs.write(str(line))
        self.fs.flush()
    def write(self, *args):
        for i,line in enumerate(args) :
            self.fs.write(str(line))
        self.fs.flush()     
    def writeline(self, line):
        self.fs.write(str(line))
        self.fs.write("\r\n")
        self.fs.flush()
    def writeline(self, *args) :
        for i,line in enumerate(args) :
            self.fs.write(str(line))
        self.fs.write("\r\n")
        self.fs.flush()
    def close(self) :
        self.fs.flush()
        self.fs.close()

        
if __name__ == '__main__' :
    log = LogWriter()
    log.write("aaaa" + "\r\n")
    log.write("bbbb" + "\r\n")
    log.close()