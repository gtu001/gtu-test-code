#### 抽象類寫法

```python
	
from abc import ABCMeta, abstractmethod

class Employee(object, metaclass=ABCMeta):

    def __init__(self, name):
        self._name = name

    @property
    def name(self):
        return self._name
        
    @name.setter        
    def name(self, name):
        self._name = name

    @abstractmethod
    def get_salary(self):
        pass

```



