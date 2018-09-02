from gtu.base.baseExample import TestPoint


def equalsExclude(x, y, exclude):
    if isinstance(y, x.__class__):
        d1 = x.__dict__
        d2 = y.__dict__
        for key in exclude :
            d1.pop(key)
            d2.pop(key)
        return d1 == d2
    return False

def equalsInclude(x, y, include):
    if isinstance(y, x.__class__):
        d1 = x.__dict__
        d2 = y.__dict__
        m1 = dict()
        m2 = dict()
        for key in include :
            m1[key] = d1[key]
            m2[key] = d2[key]
        return m1 == m2
    return False


def hashExclude(x, exclude):
    d1 = x.__dict__
    for key in exclude :
        d1.pop(key)
    return hash(tuple(sorted(d1.items())))


def hashInclude(x, include):
    d1 = x.__dict__
    m1 = dict()
    for key in include :
        m1[key] = d1[key]
    return hash(tuple(sorted(m1.items())))


def equals(self, other):
    if isinstance(other, self.__class__):
        return self.__dict__ == other.__dict__
    return False
    
    
def hashcode(self):
    return hash(tuple(sorted(self.__dict__.items())))
    

if __name__ == '__main__':
    a = TestPoint(1,2)
    b = TestPoint(1,3)
    print(equalsInclude(a, b, ['x', 'y']))