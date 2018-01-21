
def _test_filter():
    lst = ['a', 'b', 'a1', 'd', 'a3']
    lst2 = filter(lambda x : x.startswith("a"),  lst)
    for i in lst2:
        print("result", i)
        
    lst3 = [x for x in lst if x.startswith("a")]
    print(lst3)
    
def upper(lst):
    return [k.upper() for k in lst]

def sortLst(lst):
    lst.sort(key=lambda x : [x.statistic_yyy, x.statistic_mm, x.region, x.gender], reverse=False)

def writeToCSV(lst, excludeLst):
    keys = [x for x in lst[0].keys() if x not in excludeLst]
    
def getSex(self, rowIndex):
    f = lambda x: '1' if x == 'M' else ('2' if x == 'F' else "")