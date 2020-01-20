

# jupyter notebook
# https://www.youtube.com/watch?v=_uQrJ0TkZlc

import pandas as pd


df = pd.read_csv('vgsales.csv')
df = pd.read_csv('/media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/PythonGtu/gtu/data_science/pandas/ex4/vgsales.csv')
print(df.shape)


df.describe()