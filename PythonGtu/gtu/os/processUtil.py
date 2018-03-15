from win32ui import FindWindow
import win32ui

import psutil   
import subprocess 
from unittest.test.testmock.support import is_instance

### from gtu.os import processUtil


def WindowExists(classname):
    try:
        win32ui.FindWindow(classname, None)
    except win32ui.error:
        return False
    else:
        return True


def processNameExists(processName):
    return processName in (p.name() for p in psutil.process_iter())


def processNameExists2(processname):
    tlcall = 'TASKLIST', '/FI', 'imagename eq %s' % processname
    # shell=True hides the shell window, stdout to PIPE enables
    # communicate() to get the tasklist command result
    tlproc = subprocess.Popen(tlcall, shell=True, stdout=subprocess.PIPE)
    # trimming it to the actual lines with information
    command = tlproc.communicate()
    if is_instance(command[0], bytes):
        command = str(command[0])
        
    tlout = command.strip().split('\r\n')
    # if TASKLIST returns single line without processname: it's not running
    if len(tlout) > 1 and processname in tlout[-1]:
        print('process "%s" is running!' % processname)
        return True
    else:
        print(tlout[0])
        print('process "%s" is NOT running!' % processname)
        return False
    
    
def running():
    n = 0  # number of instances of the program running 
    prog = [line.split() for line in subprocess.check_output("tasklist").splitlines()]
    [prog.pop(e) for e in [0, 1, 2]]  # useless 
    for task in prog:
        if task[0] == "itunes.exe":
            n = n + 1
    if n > 0:
        return True
    else:
        return False
    
    
    

if __name__ == '__main__' :
    if WindowExists("DropboxTrayIcon"):
        print("Dropbox is running, sir.")
    else:
        print("Dropbox is running..... not.")
        
    print(processNameExists("javaw.exe"))
    
    print(processNameExists2("javaw.exe"))
    
    print(running())
    
