from __future__ import absolute_import, division, print_function, unicode_literals

import tensorflow as tf
from tensorflow.keras import layers

from gtu.reflect import checkSelf

# print(tf.version.VERSION)
print(tf.keras.__version__)


# checkSelf.checkMembers(tf, doc=False, ignorePrivate=True, ignoreInherit=True)


model = tf.keras.Sequential()
# Adds a densely-connected layer with 64 units to the model:
model.add(layers.Dense(64, activation='relu'))
# Add another:
model.add(layers.Dense(64, activation='relu'))
# Add a softmax layer with 10 output units:
model.add(layers.Dense(10, activation='softmax'))
