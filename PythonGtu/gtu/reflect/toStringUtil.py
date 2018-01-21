from abc import ABCMeta, abstractmethod
import inspect
import os
import re


class _TestClass():
    intval = 0
    
    def __init__(self, a, b, c):
        self.x = a
        self.y = b
        self.z = c
        _TestClass.intval += 1
        self.x = property(self.getX, self.setX)
        
    def __repr__(self):
        return toString(self)
    
    def aaa(self):
        print("aaaa")
        
    def getX(self):
        self.x
    
    def setX(self, x):
        self.x = x
        

class ToStringBuilder(metaclass=ABCMeta):
    def __init__(self, instance, arg2):
        self.instance = instance
        self.props = dir(instance)
        self.clzName = instance.__class__.__name__
        lst = list()
        
        self.escapeLst = arg2.get("escapeLst", [])
        self.orderLst = arg2.get("order", [])
        self.multiline = arg2.get("multiline", False)
        
        self.multilineSep = "\n\t" if self.multiline else ""
        
        if len(self.orderLst) != 0:
            self.props = self.orderLst
            
    @abstractmethod
    def func(self, i, name, val):
        pass
            
    def apply(self):
        for i, name in enumerate(self.props, 0):
            if self.escapeLst.__contains__(name):
                continue
            if not re.match(r"^\_{2}.*\_{2}$", name):
                val = getattr(self.instance, name)
                if not isMethod(val):
                    self.func(i, name, val)
                    

def toStringSimple(instance, **arg2):
    class MyToStringBuilder(ToStringBuilder):
        def __init__(self, instance, arg2):
            ToStringBuilder.__init__(self, instance, arg2)
            self.lst = list()
        def func(self, i, name, val):
            self.lst.append(str(val))
    
    t = MyToStringBuilder(instance, arg2)
    t.apply()
    return t.clzName + "[" + (", " + t.multilineSep).join(t.lst) + "]"


def toString(instance, **arg2):
    class MyToStringBuilder(ToStringBuilder):
        def __init__(self, instance, arg2):
            ToStringBuilder.__init__(self, instance, arg2)
            self.lst = list()
        def func(self, i, name, val):
            self.lst.append(name + "=" + str(val))
    
    t = MyToStringBuilder(instance, arg2)
    t.apply()
    return t.clzName + "[" + (", " + t.multilineSep).join(t.lst) + "]"
    
    
def generate(instance, **arg2):
    class MyToStringBuilder(ToStringBuilder):
        def __init__(self, instance, arg2):
            ToStringBuilder.__init__(self, instance, arg2)
            self.klst = list()
            self.klst2 = list()
            self.multilineSep = "\\n\\t" if self.multiline else ""
        def func(self, i, name, val):
            self.klst.append(name + "={}")
            self.klst2.append("self." + name)
    
    t = MyToStringBuilder(instance, arg2)
    t.apply()
    rtnVal = t.clzName + "[" + (", " + t.multilineSep).join(t.klst) + "]\".format(" + ", ".join(t.klst2) + ")"
    return "def __repr__(self):\n\treturn " + rtnVal
    

def isMethod(fun):
    return inspect.ismethod(fun) or callable(fun)


if __name__ == '__main__' :
    t1 = _TestClass(1, 2, 3)
    t2 = _TestClass(4, 5, 6)
    t3 = _TestClass(7, 8, 9)
    print(t1)
    print(t2)
    print(t3)
    
    print(generate(t1))
