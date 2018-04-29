# -*- mode: python -*-

import sys  
sys.setrecursionlimit(10000)  

block_cipher = None

srcBasePath = "e:\\workstuff\\workspace\\gtu-test-code\\PythonGtu"

a = Analysis(['excel_test_006_ui.py'],
             pathex=[srcBasePath + '\\gtu\\_tkinter\\ex\\ex4', \
             srcBasePath,
             ],
             binaries=[],
             datas=[],
             hiddenimports=['openpyxl'],
             hookspath=[],
             runtime_hooks=[],
             excludes=[],
             win_no_prefer_redirects=False,
             win_private_assemblies=False,
             cipher=block_cipher)
pyz = PYZ(a.pure, a.zipped_data,
             cipher=block_cipher)
exe = EXE(pyz,
          a.scripts,
          exclude_binaries=True,
          name='excel_test_006_ui',
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
               name='excel_test_006_ui')
