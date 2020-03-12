
import io
from gtu.error import errorHandler

'''
from gtu.io import stringIoUtil
'''


def tempStringReader(text):
    f = io.StringIO(text)
    for i,line in enumerate(f, 0):
        print(i, line)
    f.close()

    

def multiInput(exitArry=("exit", "quit")):
    try :
        strVal = input("請輸入內容")
    except Exception as ex :
        errorHandler.printStackTrace2(ex)
    while True :
        try :
            line = input()
        except Exception as ex :
            errorHandler.printStackTrace2(ex)
        tmpLine = line.strip()
        if len(tmpLine) != 0 and tmpLine in exitArry :
            break
        strVal += line + "\r\n"
    return strVal
    


if __name__ == '__main__':
    data = multiInput()
    tempStringReader(data)