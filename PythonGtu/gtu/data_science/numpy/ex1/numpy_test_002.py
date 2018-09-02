import math
import numpy as np
from gtu.reflect import checkSelf


'''
两个向量的拼接

例如两个长度为 n
的一维向量，拼接为 2×n 的矩阵，可以使用np.vstack()，
'''

def showArry(x):
    for (i,v) in enumerate(x) :
        print(i, "--", v, type(v))

x, y = np.ones(3), np.zeros(3)
print(x, y)
arry = np.vstack((x, y))
print(arry)


'''
也可使用作为np.array()构造函数的参数，进行创建：
'''

arry2 = np.array([x, y])
print(arry2)