import io
from gtu.io import stringIoUtil
from gtu.string import stringUtil
from gtu.io import LogWriter
from gtu.io import fileUtil
from gtu.regex import regexUtil
import re


class TableAndColumn() :
    def __init__(self, table, column, chinese, desc) :
        self.table = table
        self.column = column
        self.chinese = chinese
        self.desc = desc

    def __repr__(self) :
        return "{table},{column},{chinese},{desc}".format(table=self.table, column=self.column, chinese=self.chinese, desc=self.desc)


def main(filePath) :
    # strVal = stringIoUtil.multiInput()
    # print(strVal)

    lineLst = fileUtil.loadFile_asList(filePath, trim=True, distinct=False, ignoreBlankLine=False)

    log = LogWriter.LogWriter(filename="LC_table.csv", encoding="BIG5", mode="w")

    ptn = re.compile(r"(?P<chinese>.*?)\s+(?P<column>\w+?)\s+\:(?P<desc>.*)", re.I)
    
    tableName = ""
    for i,line in enumerate(lineLst) :
        if regexUtil.match(r'^\w+$', False, line) :
            tableName = line.strip()

        elif stringUtil.isNotBlank(tableName):
            mth = ptn.search(line)
            if mth :
                column = mth.group("column")
                chinese = mth.group("chinese")
                desc = mth.group("desc")
                tabColumn = TableAndColumn(tableName, column, chinese, desc)
                log.writeline(tabColumn)
    
    log.close()




if __name__ == '__main__' :
    filePath = "C:/Users/wistronits/Desktop/LC_table.txt"
    main(filePath)
    print("done...")
