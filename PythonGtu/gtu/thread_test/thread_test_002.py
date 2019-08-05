
import  threading, time, random    


count = 0    
class  Counter(threading.Thread):    
    def  __init__(self , lock, threadName):    
        '''@summary:初始化對象。   
           
        @param lock: 瑣對象。   
        @param threadName: 線程名稱。   
        '''    
        super(Counter, self).__init__(name=threadName)  # 注意：一定要顯式的調用父類的初始化函數.  
        self.lock = lock    
        
    def  run(self):    
        '''@summary:重寫父類run方法，在線程啟動後執行該方法內的代碼. '''    
        global  count    
        self.lock.acquire()    
        for  i  in  range(1000):    
            count = count + 1    
        self .lock.release()    
        
lock = threading.Lock()    
for  i  in  range(5):     
    Counter(lock, "thread-" + str(i)).start()  # Open 5 隻線程  
    
time.sleep(2)  # 確保線程都執行完畢    
print("Count={0}!".format(count))   
