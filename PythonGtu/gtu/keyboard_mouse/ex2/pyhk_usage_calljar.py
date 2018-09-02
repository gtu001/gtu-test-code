import os
from pathlib import Path
import socket

from gtu.keyboard_mouse import pyhk
from gtu.win32 import systrayicon as systray


HOST = '127.0.0.1'
PORT = 65535

def fun():
    print("calling jar ...")
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    result = s.connect_ex((HOST, PORT))
    print(str(result))
    s.send("run_EnglishSearchUI".encode("utf8"))
    s.close()
    
def createSysTray():
    import itertools, glob
    icon = '/gtu/win32/favicon.ico'
    def hello(sysTrayIcon): 
        applyHotKey()
    menu_options = (('啟動', icon, hello),)
    def bye(sysTrayIcon): 
        print('Bye, then.')
        os._exit(0)
    systray.SysTrayIcon(icon, "EnglishSearchUI", menu_options, on_quit=bye, default_menu_index=1)

def applyHotKey():
    hot = pyhk.pyhk()
    hot.addHotkey(['Alt', 'Shift', 'X'], fun)
    hot.start()

def main():
    createSysTray()
 
if __name__ == '__main__' :
    main()
    
    

    
    
