
import numpy as np
from gtu.reflect import checkSelf


X = np.random.random((10, 3))

print(X)
print("dimenNa", X.mean())
print("dimen0", X.mean(0))
print("dimen1", X.mean(1))