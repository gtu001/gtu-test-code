import functools


class _TestClass ():
    def __init__(self, x, y):
        self.x = x
        self.y = y
    def __repr__(self):
        return "_TestClass[x={}, y={}]".format(self.x, self.y)


def getKey(x):
    return [x.x, x.y]


def defCmp(x, y):
    if x.x < y.x:
        return -1
    elif x.x > y.x:
        return 1
    return 0


def reverse_numeric(x, y):
    return y - x
        
if __name__ == '__main__' :
    a0 = _TestClass(7, 1)
    a1 = _TestClass(1, 3)
    a2 = _TestClass(1, 5)
    a3 = _TestClass(2, 7)
    
    lst = list()
    lst.append(a0)
    lst.append(a1)
    lst.append(a2)
    lst.append(a3)
    
    lst.sort(key=functools.cmp_to_key(defCmp), reverse=True)
    lst.sort(key=getKey, reverse=False)
    lst.sort(key=lambda x : [x.x, x.y])
    
    for i, row in enumerate(lst, 0):
        print(i, row)

        
