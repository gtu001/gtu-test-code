# encoding: UTF-8
import _thread
import time


# 一个用于在线程中执行的函数
def func():
    for i in range(5):
        print('func')
        time.sleep(1)
    
    # 结束当前线程
    # 这个方法与thread.exit_thread()等价
    _thread.exit() # 当func返回时，线程同样会结束
        
# 启动一个线程，线程立即开始运行
# 这个方法与thread.start_new_thread()等价
# 第一个参数是方法，第二个参数是方法的参数
_thread.start_new(func, ()) # 方法没有参数时需要传入空tuple
 
# 创建一个锁（LockType，不能直接实例化）
# 这个方法与thread.allocate_lock()等价
lock = _thread.allocate()
 
# 判断锁是锁定状态还是释放状态
print(lock.locked())
 
# 锁通常用于控制对共享资源的访问
count = 0
 
# 获得锁，成功获得锁定后返回True
# 可选的timeout参数不填时将一直阻塞直到获得锁定
# 否则超时后将返回False
if lock.acquire():
    count += 1
    
    # 释放锁
    lock.release()
 
# thread模块提供的线程都将在主线程结束后同时结束
time.sleep(6)