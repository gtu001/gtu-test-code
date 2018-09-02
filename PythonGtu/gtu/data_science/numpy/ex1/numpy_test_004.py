
import numpy as np


M = np.ones((2, 3))
a = np.arange(3)

print(M)
print(a)

print(M.shape)
print(a.shape)

b = a + M

print(b)
print(b.shape)


print("-------------------")



a = np.arange(3).reshape((3, 1))
b = np.arange(3)

print(a)
print(b)
print(a.shape)
print(b.shape)

c = a + b

print(c)
print(c.shape)


print("-------------------")



M = np.ones((3, 2))
a = np.arange(3)

print(M)
print(a)

print(M.shape)
print(a.shape)
print("轉換shape", a[:, np.newaxis].shape)

b = M + a[:, np.newaxis]

print(b)
print(b.shape)
