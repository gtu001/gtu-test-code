import inspect
import sys
import os


print("Python " + sys.version)
print()

print(__file__)                                        # script1.py
print(sys.argv[0])                                     # script1.py
print(inspect.stack()[0][1])                           # lib/script3.py
print(sys.path[0])                                     # C:\testpath
print()

print(os.path.realpath(__file__))                      # C:\testpath\script1.py
print(os.path.abspath(__file__))                       # C:\testpath\script1.py
print(os.path.basename(__file__))                      # script1.py
print(os.path.basename(os.path.realpath(sys.argv[0]))) # script1.py
print()

print(sys.path[0])                                     # C:\testpath
print(os.path.abspath(os.path.split(sys.argv[0])[0]))  # C:\testpath
print(os.path.dirname(os.path.abspath(__file__)))      # C:\testpath
print(os.path.dirname(os.path.realpath(sys.argv[0])))  # C:\testpath
print(os.path.dirname(__file__))                       # (empty string)
print()

print(inspect.getfile(inspect.currentframe()))         # lib/script3.py

print(os.path.abspath(inspect.getfile(inspect.currentframe()))) # C:\testpath\lib\script3.py
print(os.path.dirname(os.path.abspath(inspect.getfile(inspect.currentframe())))) # C:\testpath\lib
print()

print(os.path.abspath(inspect.stack()[0][1]))          # C:\testpath\lib\script3.py
print(os.path.dirname(os.path.abspath(inspect.stack()[0][1])))  # C:\testpath\lib

