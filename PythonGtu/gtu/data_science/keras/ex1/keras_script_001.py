
import numpy as np

from keras.models import Sequential
from keras import optimizers
from gtu.data_science.keras.ex1.dataGenerator import DataGenerator

# Parameters
params = {'dim': (32,32,32),
          'batch_size': 64,
          'n_classes': 6,
          'n_channels': 1,
          'shuffle': True}

# Datasets
partition = {'train': ['id-1', 'id-2', 'id-3'], 'validation': ['id-4']} # IDs
labels = {'id-1': 0, 'id-2': 1, 'id-3': 2, 'id-4': 1} # Labels

# Generators
training_generator = DataGenerator(partition['train'], labels, **params)
validation_generator = DataGenerator(partition['validation'], labels, **params)

# Design model
model = Sequential()
sgd = optimizers.SGD(lr=0.01, decay=1e-6, momentum=0.9, nesterov=True)
model.compile(loss='mean_squared_error', optimizer=sgd)

# Train model on dataset
model.fit_generator(generator=training_generator,
                    validation_data=validation_generator,
                    use_multiprocessing=True,
                    workers=6)