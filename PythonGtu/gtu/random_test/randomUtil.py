from random import randint

class CountMap() :
    def __init__(self) :
        self.map = dict()
    def put(self, key) :
        val = 0
        if self.map.__contains__(key) :
            val = self.map[key]
        val += 1
        self.map[key] = val


def randomInt(start, end) :
    return randint(start, end)


def randomLst(lst) :
    idxLst = list()
    for i in range(0, len(lst) - 1) :
        idxLst.append(i)
    rtnLst = list()
    while len(idxLst) > 0 :
        idx = randint(0, len(idxLst) - 1)
        val = lst[idxLst[idx]]
        rtnLst.append(val)
        del idxLst[idx]
    return rtnLst


def _getCharArry(type) :
    lst = list()
    if 'l' in type or 'L' in type :
        for i in range(ord('a'), ord('z')) :
            lst.append(chr(i))
    if 'u' in type or 'U' in type :
        for i in range(ord('A'), ord('Z')) :
            lst.append(chr(i))  
    if 'd' in type or 'D' in type :
        for i in range(0, 9) :
            lst.append(str(i))  
    return lst


def randomString(length, type="LUD", appendChars="") :
    arry = _getCharArry(type)
    if appendChars :
        arry = arry + list(appendChars)
    end = len(arry) - 1
    rtnStr = ""
    for i in range(0, length) :
        rtnStr += arry[randint(0, end)]
    return rtnStr



def main() :
    print(randomString(100, type="L", appendChars="~!@#$%^&*()_+"))
    

if __name__ == '__main__' :
    main()
    print("done...")