
import numpy as np

'''
from gtu.numpy import numpyUtil
'''


def createEmptyMartix(row, col):
    return np.empty(shape=(row, col), dtype='object')


def __test_reshape_put_into_martix():
    arry1 = createEmptyMartix(5, 5)
    arry2 = np.array([3,3,3])
    arry3 = np.reshape(arry2, (3, 1), order='C')
    print(arry3)
    arry1[0:3, 0:1] = arry3
    print(arry1)


if __name__ == '__main__':
    __test_reshape_put_into_martix()