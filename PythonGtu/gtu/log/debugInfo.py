from inspect import currentframe, getframeinfo
import inspect

frameinfo = getframeinfo(currentframe())
print(frameinfo.filename, frameinfo.lineno)


#or


frameinfo = inspect.getframeinfo(inspect.currentframe())
print(frameinfo.filename, frameinfo.lineno)