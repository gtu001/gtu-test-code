import os
import sys

dir_of_interest = 'C:/python/files/folder1'
modules = {}

sys.path.append(dir_of_interest)
for module in os.listdir(dir_of_interest):
    if '.py' in module and '.pyc' not in module:
        current = module.replace('.py', '')
        modules[current] = __import__(current)