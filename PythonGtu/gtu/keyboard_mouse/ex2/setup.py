from distutils.core import setup
import py2exe

# setup(windows=["pyhk_usage_calljar.py"])
setup(name="Simple Script",
    options={
        "py2exe": {
            "dll_excludes" : ["libmmd.dll", "libifcoremd.dll", "libiomp5md.dll", "libzmq.dll"],
            "dll_includes" : ["pywintypes34.dll", "pythoncom34.dll"],
        }
    }, console=['pyhk_usage_calljar.py'])

