# -*- mode: python -*-

block_cipher = None

srcBasePath = 'D:\\workstuff\\workspace\\gtu-test-code\\PythonGtu'

a = Analysis(['pyhk_usage_calljar.py'],
             pathex=[srcBasePath + '\\gtu\\keyboard_mouse\\ex2', 
             	srcBasePath, \
             	],
             binaries=[],
             datas=[(srcBasePath + '\\gtu\\win32\\favicon.ico', '.\\gtu\\win32\\')],
             hiddenimports=['psutil'],
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
          name='pyhk_usage_calljar',
          debug=False,
          strip=False,
          upx=True,
          console=False ) #True
coll = COLLECT(exe,
               a.binaries,
               a.zipfiles,
               a.datas,
               strip=False,
               upx=True,
               name='pyhk_usage_calljar')
               

