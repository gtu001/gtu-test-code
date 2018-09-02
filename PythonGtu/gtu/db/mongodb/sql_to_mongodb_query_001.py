import re
from enum import Enum
from gtu.enum import enumUtil
from gtu.reflect import toStringUtil


def main(sql):
    ptn = re.compile(r"from\s+(?P<tableName>\w+)\s+where\s+(?P<column>\w+)\s+(?P<operator>.*?)\s+(?P<compareValue>.*)", re.I)
    mth = ptn.search(sql)
    if mth is not None :
        tableName = mth.group("tableName")
        column = mth.group("column")
        operator = mth.group("operator")
        compareValue = mth.group("compareValue")
        qc = __QueryConfig(tableName, column, operator, compareValue)
        mongoDbSql = qc.operatorEnum.toMongoDbSql(qc)
        print("結果為 : ", mongoDbSql)
    else :
        print("Not Match Script!")



class __OperatorEnum(Enum):
    Equality = ("=", "{{ {col} : {value} }}")
    LessThan = ("<", "{{ {col} : {{ $lt:{value} }} }}")
    LessThanEquals = ("<=", "{{ {col} : {{ $lte:{value} }} }}")
    GreaterThan = (">", "{{ {col} : {{ $gt:{value} }} }}")
    GreaterThanEquals = (">=", "{{ {col} : {{ $gte:{value} }} }}")
    NotEquals = (">=", "{{ {col} : {{ $ne:{value} }} }}")
    
    def __init__(self, operator, jsonFormat):
        self.operator = operator
        self.jsonFormat = jsonFormat
        
    def toMongoDbSql(self, qc):
        jsonStr = self.jsonFormat.format(col=qc.column, value=qc.compareValue)
        resultStr = "db.{tableName}.find({jsonStr}).pretty()".format(tableName=qc.tableName, jsonStr=jsonStr)
        return resultStr
    


class __QueryConfig():
    
    def __init__(self, tableName, column, operator, compareValue):
        self.tableName = tableName
        self.column = column
        self.operator = operator
        self.compareValue = compareValue
        self.operatorEnum = self.findOperatorEnum(operator)
    
    def findOperatorEnum(self, operator):
        def callFunc(i, mem):
            if mem.operator == operator:
                return (True, mem)
            
        util = enumUtil.EnumHelper("gtu.db.mongodb.sql_to_mongodb_query_001.__OperatorEnum")
        return util.loopAll(callFunc)
    
    def __repr__(self):
        return toStringUtil.toString(self, limitSize=100)
    
    

if __name__ == '__main__' :
    main("select * from TABLE WHERE COL >= '999' ")
    print("done...")
    
    
