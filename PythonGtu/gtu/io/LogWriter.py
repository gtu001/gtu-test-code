
from gtu.io import fileUtil
from gtu.string import stringUtil
from gtu.datetime import dateUtil
import datetime

'''
from gtu.io import LogWriter

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

class LogWriter :
    def __init__(self, file=None, filename="", mode="a", encoding="UTF8") :
        if stringUtil.isBlank(filename) :
            filename = "log_" + dateUtil.formatDatetimeByJavaFormat(datetime.datetime.now(), "yyyyMMdd_HHmmss") + ".txt"
        if file is None :
            file = fileUtil.getDesktopDir(filename)
        self.fs = open(file, mode, encoding=encoding, buffering=30)
    def write(self, line):
        self.fs.write(str(line))
        self.fs.flush()
    def write(self, *args):
        for i,line in enumerate(args) :
            self.fs.write(str(line))
        self.fs.flush()     
    def writeline(self, line):
        self.fs.write(str(line))
        self.fs.write("\n")
        self.fs.flush()
    def writeline(self, *args) :
        for i,line in enumerate(args) :
            self.fs.write(str(line))
        self.fs.write("\n")
        self.fs.flush()
    def close(self) :
        self.fs.flush()
        self.fs.close()

        
if __name__ == '__main__' :
    log = LogWriter()
    log.write("aaaa" + "\r\n")
    log.write("bbbb" + "\r\n")
    log.close()