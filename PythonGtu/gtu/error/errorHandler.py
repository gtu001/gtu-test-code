import sys
import traceback

from gtu.reflect import checkSelf


def printStackTrace():
    traceback.print_exc(file=sys.stdout)
    
def printStackTrace2(ex):
    traceback.print_tb(ex.__traceback__)
    
def getException():
    exc_info = sys.exc_info()
    return exc_info

def getExceptionArry():
    exc_type, exc_value, exc_traceback = sys.exc_info()
    return exc_type, exc_value, exc_traceback 

if __name__ == '__main__' :
    try:
        raise Exception("xxxxxxxxxxx")
    except Exception as ex :
        printStackTrace2(ex)