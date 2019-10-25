import sys
import io

def tempSysPrintFix() :
    sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='ascii', errors='xmlcharrefreplace')


if __name__ == '__main__' :
    tempSysPrintFix()
    print('Jalape\u00f1o')