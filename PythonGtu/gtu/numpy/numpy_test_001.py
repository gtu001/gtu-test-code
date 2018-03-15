import math
import numpy as np
from gtu.reflect import checkSelf

'''
一维数组构成的list，再进行像数组的转换
是以行的形式（而非列的形式）在拼接；
'''

X = np.random.randn(5, 3)
'''
隨機產生5x3 array
'''
print(X)

ops = [np.argmax, np.argmin]

arry = np.asarray([op(X, 1) for op in ops])

''' 
第一個array放X的最大值index
第二個array放X的最小值index
'''
print(arry)

