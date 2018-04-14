from inspect import getmembers
import bge


'''
取得事件
'''
co = bge.logic.getCurrentController()
# 'Keyboard' is a keyboard sensor
sensor = co.sensors["Keyboard"]

for key,status in sensor.events:
    # key[0] == bge.events.keycode, key[1] = status
    if status == bge.logic.KX_INPUT_JUST_ACTIVATED:
        print(key, status)
            
            
'''
取得所有keyboard 常數
'''
constants = {k:v for k,v in getmembers(bge) if k.starts with('KX_')}            