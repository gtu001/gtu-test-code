from collections import deque


'''
from gtu.util import queue_test_001
'''

class DequeObj() :
    def __init__(self, lst=None, keepOrigin=False) :
        self.keepOrigin = keepOrigin
        if lst is not None:
            self.dq = deque(lst)
            self.dqCopy = deque(lst)    
        else:
            self.dq = deque()
            self.dqCopy = deque()    
    def appendright(self, v):
        self.dq.append(v)
        if self.keepOrigin :
            self.dqCopy.append(v)
    def appendleft(self, v):
        self.dq.appendleft(v)
        if self.keepOrigin :
            self.dqCopy.appendleft(v)
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


def main() :
    d = DequeObj([1,2,3])
    d.appendright(4)
    v = d.popleft()
    print(v)
    print(d.get())


if __name__ == '__main__' :
    main()
    print("done...")