import tensorflow as tf
from tensorflow.keras import layers

print(tf.VERSION)
print(tf.keras.__version__)


model = tf.keras.Sequential()
# Adds a densely-connected layer with 64 units to the model:
model.add(layers.Dense(64, activation='relu'))
# Add another:
model.add(layers.Dense(64, activation='relu'))
# Add a softmax layer with 10 output units:
model.add(layers.Dense(10, activation='softmax'))


'''
=== Layer ===
activation : 設定啟動方法到 layer. 這變數名子具體為內建方法或可呼叫物件. 預設不用設
kernel_initializer 和 bias_initializer: 初始計畫為建立 layer 權重 (核心或偏執 bias)  這變數是可呼叫物件. 缺省為 "Glorot uniform"
kernel_regularizer 和 bias_regularizer: 這正規化計畫應用 layer 權重 (核心或偏執 bias) ,諸如 L1,L2 正規化. 預設為無
'''

# Create a sigmoid layer:
layers.Dense(64, activation='sigmoid')
# Or:
layers.Dense(64, activation=tf.sigmoid)

# A linear layer with L1 regularization of factor 0.01 applied to the kernel matrix:
layers.Dense(64, kernel_regularizer=tf.keras.regularizers.l1(0.01))

# A linear layer with L2 regularization of factor 0.01 applied to the bias vector:
layers.Dense(64, bias_regularizer=tf.keras.regularizers.l2(0.01))

# A linear layer with a kernel initialized to a random orthogonal matrix:
layers.Dense(64, kernel_initializer='orthogonal')

# A linear layer with a bias vector initialized to 2.0s:
layers.Dense(64, bias_initializer=tf.keras.initializers.constant(2.0))





model = tf.keras.Sequential([
# Adds a densely-connected layer with 64 units to the model:
layers.Dense(64, activation='relu'),
# Add another:
layers.Dense(64, activation='relu'),
# Add a softmax layer with 10 output units:
layers.Dense(10, activation='softmax')])

model.compile(optimizer=tf.train.AdamOptimizer(0.001),
              loss='categorical_crossentropy',
              metrics=['accuracy'])


'''tf.keras.Model.compile 有三個重要參數:
optimizer: 此物件指定訓練程序. 傳進實體從 tf.train 模組, 諸如tf.train.AdamOptimizer, tf.train.RMSPropOptimizer 或是 tf.train.GradientDescentOptimizer
loss: 此方法執行最小化在優化時. 一般選擇中位平方錯誤 mean square error(mse), 分類跨熵categorical_crossentropy, 和 二元跨熵 binary_crossentropy. 損失方法是指定名稱或傳入可呼叫物件從 tf.keras.losses 模組
metrics: 使用監控訓練 monitor training. 這是字串名或可呼叫從 tf.keras.metrics 模組 
'''




# Configure a model for mean-squared error regression.
model.compile(optimizer=tf.train.AdamOptimizer(0.01),
              loss='mse',       # mean squared error
              metrics=['mae'])  # mean absolute error

# Configure a model for categorical classification.
model.compile(optimizer=tf.train.RMSPropOptimizer(0.01),
              loss=tf.keras.losses.categorical_crossentropy,
              metrics=[tf.keras.metrics.categorical_accuracy])




'''
餵養資料給model
'''
import numpy as np

data = np.random.random((1000, 32))
labels = np.random.random((1000, 10))

model.fit(data, labels, epochs=10, batch_size=32)

'''
tf.keras.Model.fit 三個重要變數:
epochs: 訓練是建構進入時期. 一個時期是一個遍歷完整輸入資料 (這是完成較小的批次)
batch_size: 當傳入NumPy 資料, 模組切片資料進入小的批次 且遍例這些批次在訓練過程. 這整數表明批次大小. 注意最後批次可能更小假如總數樣本是無法被整除
validation_data: 當多型化一個模組, 你要簡單監控執行效能再一些驗證資料. 傳入 argument-a tuple的輸入和 標籤允許的模組去顯示損失和公尺在表明模式在以傳入的資料上, 在一切時期結束時顯示
'''





# Instantiates a toy dataset instance:
dataset = tf.data.Dataset.from_tensor_slices((data, labels))
dataset = dataset.batch(32)
dataset = dataset.repeat()

# Don't forget to specify `steps_per_epoch` when calling `fit` on a dataset.
model.fit(dataset, epochs=10, steps_per_epoch=30)
'''
steps_per_epoch 表述訓練步驟數 當 模型執行多少數量會進入到下一個時期. 自從Dataset 生產 批次資料, 摘錄不需要 批次size
'''




'''
dataset也可以驗證
'''
dataset = tf.data.Dataset.from_tensor_slices((data, labels))
dataset = dataset.batch(32).repeat()

val_dataset = tf.data.Dataset.from_tensor_slices((val_data, val_labels))
val_dataset = val_dataset.batch(32).repeat()

model.fit(dataset, epochs=10, steps_per_epoch=30,
          validation_data=val_dataset,
          validation_steps=3)



















