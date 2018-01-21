
import os
import time

from PIL import ImageGrab #需要Image模組  

from gtu.io import fileUtil


def prtSc():
    # 屏幕抓图实现
    pic_name = time.strftime('%Y%m%d%H%M%S', time.localtime(time.time()))
    pic = ImageGrab.grab()
    # 保存成为以日期命名的图片
    pic.save(fileUtil.getDesktopDir() + os.sep + pic_name + ".png")


if __name__ == '__main__' :
    prtSc()
    print("do prtSc()")