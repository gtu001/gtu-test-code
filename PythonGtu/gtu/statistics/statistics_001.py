# 統計資訊

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


plt.style.use('ggplot')

df1 = pd.read_csv("2012MLB.csv", encoding="big5")

# df1.cov()  #共變數

df1.describe()  # 跟R的summary一樣、列出一些統計資訊
