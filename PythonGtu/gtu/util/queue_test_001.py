from collections import deque


'''
from gtu.util import queue_test_001
'''

class DequeObj() :
    def __init__(self, lst=None) :
        if lst is not None:
            self.dq = deque(lst)  
        else:
            self.dq = deque() 
        self._allCount = 0
    def appendright(self, v):
        self.dq.append(v)
        self._allCount += 1
    def appendleft(self, v):
        self.dq.appendleft(v)
        self._allCount += 1
    def popright(self) :
        return self.dq.pop()
    def popleft(self) :
        return self.dq.popleft()
    def reversed(self) :
        return list(reversed(self.dq))
    def get(self) :
        return self.dq
    def length(self) :
        return len(self.dq)
    def allCount(self) :
        return self._allCount


def main() :
    d = DequeObj([1,2,3])
    d.appendright(4)
    v = d.popleft()
    print(v)
    print(d.get())


if __name__ == '__main__' :
    main()
    print("done...")