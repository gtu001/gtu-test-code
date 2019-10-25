
import io

'''
from gtu.io import stringIoUtil
'''


def tempStringReader(text):
    f = io.StringIO(text)
    for i,line in enumerate(f, 0):
        print(i, line)
    f.close()

    

def multiInput():
    str = input("請輸入內容")
    while True :
        line = input()
        if line.strip() in ("exit", "quit"):
            break
        str += line + "\r\n"
    return str
    

if __name__ == '__main__':
    data = multiInput()
    tempStringReader(data)