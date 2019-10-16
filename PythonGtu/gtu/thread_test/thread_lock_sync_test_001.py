import random, time
from threading import BoundedSemaphore, Thread
from threading import RLock

from gtu.reflect import checkSelf


# 一般 sync 寫法
class ThreadNormalLockTest :
    def __init__(self):
        self.lock = RLock()
        self.value = 0
    def addOne(self) :
        self.lock.acquire()
        self.value += 1
        time.sleep(0.1)
        self.lock.release()


# 帶有 block size 的 sync 寫法
class ThreadBoundedSemaphoreTest :
    def __init__(self):
        self.lock = BoundedSemaphore(100) # block size
        self.value = 0
    def addOne(self) :
        print(self.lock._value) # block size
        self.lock.acquire(True)
        self.value += 1
        time.sleep(0.1)
        self.lock.release()


class MyTestThread(Thread):
    def __init__(self, mThreadBoundedSemaphoreTest) :
        super().__init__()
        self.mThreadBoundedSemaphoreTest = mThreadBoundedSemaphoreTest
    def run(self) :
        self.mThreadBoundedSemaphoreTest.addOne()


def main() :
    # checkSelf.checkMembersToHtml(test)
    threadLst = list()
    mThreadBoundedSemaphoreTest = ThreadBoundedSemaphoreTest()
    for _ in range(1, 200) :
        t = MyTestThread(mThreadBoundedSemaphoreTest)
        threadLst.append(t)
        t.start()
    for t in threadLst :
        t.join()


if __name__ == '__main__' :
    main()
    print("done...")