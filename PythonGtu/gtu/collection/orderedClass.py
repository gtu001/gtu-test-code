from collections import OrderedDict


class OrderedClass(object):
    def __init__(self):
        self._keys = []
        
    def __setattr__(self, key, value):
        # store new attribute (key, value) pairs in builtin __dict__
        self.__dict__[key] = value
        # store the keys in self._keys in the order that they are initialized
        # do not store '_keys' itelf and don't enter any key more than once 
        if key not in ['_keys'] + self._keys:
            self._keys.append(key)

    def items(self):
        # retrieve (key, value) pairs in the order they were initialized using _keys
        return [(k, self.__dict__[k]) for k in self._keys]
    
    def keys(self):
        return self._keys
    

class _TestBean(OrderedClass):
    def __init__(self):
        OrderedClass.__init__(self)
        self.x = "1"
        self.y = "2"
        self.z = "3"
        

if __name__ == '__main__':
    x = _TestBean()
    for k, v in x.items():
        print(k, v)