# -*- mode: python -*-

block_cipher = None

import sys  
sys.setrecursionlimit(10000)  


# ***** please use buildexe_import_scan_001.py  scan ****
# remove excludes = 'collections' 'openpyxl''calendar', 'time''platform''random', 'socket''datetime''abc''pythoncom''codecs''functools''threading''urllib''inspect''json''logging''ctypes''math'
# ***** please use buildexe_import_scan_001.py  scan ****

excludes = [
			'win32con', 'multiprocessing', 
			'retry', 'scrapy', 
			'pyHook', 'configparser','csv', 'pyhk', 'pdftotext', 
			'itertools', 'win32ui', 'pymysql', 'py2exe',
			'pynput', 'win32clipboard', 
			'mpl_finance' , 'requests', 'PyPDF2', 'sqlalchemy', 'matplotlib', 
			'pandas', 'scipy', 'bge', 'distutils', 'numpy'
			]



a = Analysis(['excel_test_rpt_8110_cellCompare_UI.py'],
             pathex=['D:\\workstuff\\gtu-test-code\\PythonGtu\\', 'D:\\workstuff\\gtu-test-code\\PythonGtu\\gtu\\_tkinter\\ex\\ex7'],
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
          name='excel_test_rpt_8110_cellCompare_UI',
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
               name='excel_test_rpt_8110_cellCompare_UI')
