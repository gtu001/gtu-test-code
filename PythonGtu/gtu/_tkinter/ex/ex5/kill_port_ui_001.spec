# -*- mode: python -*-

block_cipher = None

import sys  
sys.setrecursionlimit(10000)  

excludes = ["zmq", 'pythoncom', 'configparser', 'sqlalchemy', 'PyPDF2', 'win32clipboard', '', 'pymysql', 'logging', 'pandas', 'pyHook', 'csv', 'numpy', 'bge', 'pyhk', '_thread', 'time', 'calendar', 'openpyxl', 'platform', 'win32con', 'random', 'pynput', 'matplotlib', 'distutils', 'itertools', 'win32ui', 'pdftotext', 'socket', 'math', '', 'py2exe', 'ctypes', 'datetime', '']


a = Analysis(['kill_port_ui_001.py'],
             pathex=['E:\\workstuff\\workspace\\gtu-test-code\\PythonGtuNew\\', 'e:\\workstuff\\workspace\\gtu-test-code\\PythonGtuNew\\gtu\\_tkinter\\ex\\ex5'],
             binaries=[],
             datas=[],
             hiddenimports=[],
             hookspath=[],
             runtime_hooks=[],
             excludes=excludes,
             win_no_prefer_redirects=False,
             win_private_assemblies=False,
             cipher=block_cipher)
pyz = PYZ(a.pure, a.zipped_data,
             cipher=block_cipher)
exe = EXE(pyz,
          a.scripts,
          exclude_binaries=True,
          name='kill_port_ui_001',
          debug=False,
          strip=False,
          upx=True,
          console=True )
coll = COLLECT(exe,
               a.binaries,
               a.zipfiles,
               a.datas,
               strip=False,
               upx=True,
               name='kill_port_ui_001')
