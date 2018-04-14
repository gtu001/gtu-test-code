import sys
from pynput.keyboard import Key, KeyCode, Controller

from gtu.reflect import checkSelf


def obtainKeyCode_forLinux():
    import tty
    tty.setcbreak(sys.stdin)
    while True:
        print(ord(sys.stdin.read(1)))
        
        
def obtainKeyCode_forWin():
    k = input("請輸入")
    if len(k) >= 1:
        print(ord(k[0]))
    
    
if __name__ == '__main__' :
    checkSelf.checkMembers(KeyCode, doc=True, ignoreInherit=True)
    
    