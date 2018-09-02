

import _win32sysloader

p = _win32sysloader.GetModuleFilename("pywintypes34.dll")

print(p)