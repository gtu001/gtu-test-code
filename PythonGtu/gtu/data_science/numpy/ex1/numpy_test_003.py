

import numpy as np

a = np.array([0, 1, 2])
b = np.array([5, 5, 5])
c = a + b
print(c)

d = a + 5
print(d)
print("----------------------")




M = np.ones((3, 3))
print(M)
M2 = M + a
print(M2)

print("----------------------")



a = np.arange(3)
b = np.arange(3)[:, np.newaxis]
print(a)
print(b)
c = a + b
print(c)



print("----------------------")

a = np.arange(3) + 5
print("a", a)
b = np.ones((3, 3)) + np.arange(3)
print("b", b)
c = np.arange(3).reshape((3, 1)) + np.arange(3)
print("c", c)

print("----------------------")

