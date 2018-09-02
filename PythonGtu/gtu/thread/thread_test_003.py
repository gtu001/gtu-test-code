import  threading, time, random    

count = 0    
lock = threading.Lock()    

def  doAdd():    
    '' '''@summary:將全局變量count逐一的增加10000。   
    '''    
    global  count, lock    
    lock.acquire()    
    for  i  in  range(1000):    
        count = count + 1    
    lock.release()  
      
for  i  in  range(5):    
    threading.Thread(target=doAdd, args=(), name='thread-' + str(i)).start()    
    
time.sleep(2)  # 確保線程都執行完畢    
print("Count={0}!".format(count))  
