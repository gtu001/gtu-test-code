#!/usr/bin/python
# Filename: using_sys.py

import sys

print('The command line arguments are:')

for i in sys.argv:
    print(i)
    
print('\n\nThe PYTHONPATH is', sys.path, '\n')

for i in sys.path:
    print('sys.path - ', i)

print('\n')
print('\n')

print(type(sys.path) is list);
print(isinstance(sys.path, list));