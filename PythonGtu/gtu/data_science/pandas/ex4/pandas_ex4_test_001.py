

# jupyter notebook
# https://www.youtube.com/watch?v=_uQrJ0TkZlc

import pandas as pd


df = pd.read_csv('vgsales.csv')
print(df.shape)


df.describe()