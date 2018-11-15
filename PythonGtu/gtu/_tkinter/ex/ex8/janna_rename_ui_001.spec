# -*- mode: python -*-

block_cipher = None

import sys  
sys.setrecursionlimit(10000)  


# ***** please use buildexe_import_scan_001.py  scan ****
# remove excludes = 'collections' 'codecs' 'abc' 'functools' 'inspect' 'platform' 'threading' 'datetime' 'random' 'socket' 'urllib' 'calendar' 'pythoncom' 'logging' 'json' 'ctypes'
# ***** please use buildexe_import_scan_001.py  scan ****

excludes = [
	'pyHook', 'math', 'bge', 'pynput', 
	'PyPDF2', 'pandas', 'py2exe', 'win32clipboard', 'numpy', 'matplotlib', 
	'configparser', 'flask', 'distutils', 'pdftotext', 
	'mpl_finance', 'scrapy', 'win32con', 'pyhk', 'multiprocessing', 
	'time', 'itertools', 'scipy', 'csv', 'pymysql', 
	'sqlalchemy', 'openpyxl', 'retry', 
	'win32ui', 'requests'
	]


a = Analysis(['janna_rename_ui_001.py'],
             pathex=['D:\\workstuff\\gtu-test-code\\PythonGtu\\', 'D:\\workstuff\\gtu-test-code\\PythonGtu\\gtu\\_tkinter\\ex\\ex8'],
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
          name='janna_rename_ui_001',
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
               name='janna_rename_ui_001')
