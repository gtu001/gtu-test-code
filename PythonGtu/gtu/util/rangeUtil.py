from gtu.reflect import toStringUtil, checkSelf


class RangeWrapper():
    def __init__(self, rangeObj):
        self.rangeObj = rangeObj
        self.left = rangeObj.start
        self.right = rangeObj.stop
        self.step = rangeObj.step
        self.size = len(rangeObj)
        
    def indexOf(self, val):
        try:
            return self.rangeObj.index(val)
        except :
            return -1
        
    def contain(self, val):
        if self.rangeObj.count(val) == 1:
            return True
        return False
    
    def get(self, index):
        return self.rangeObj[index]
        
    def __repr__(self):
        return toStringUtil.toString(self)
    
    
if __name__ == '__main__' :
    rng = RangeWrapper(range(10, 30))
    for index in range(0, rng.size) :
        print("idx:", index, " - ", rng.get(index))
    print("done...")