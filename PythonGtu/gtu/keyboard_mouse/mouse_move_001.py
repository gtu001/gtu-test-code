import time

import pyautogui


time.sleep(10)

pyautogui.moveTo(1000,1000,duration=1)
pyautogui.dragRel(100,0,duration=1)
pyautogui.dragRel(-100,0,duration=1)
pyautogui.dragRel(0,100,duration=1)
