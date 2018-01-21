from abc import abstractmethod, ABCMeta
from enum import Enum


class AbstractClass(metaclass=ABCMeta):
    @abstractmethod
    def __init__(self):
        pass
    @abstractmethod
    def test(self):
        pass
    
    
class _TestClass(AbstractClass):
    def __init__(self):
        pass
    def test(self):
        return "XXX"


if __name__ == '__main__':
    x = _TestClass()
    print(x.test())