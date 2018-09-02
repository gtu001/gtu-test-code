
import os
import subprocess

from gtu.os import envUtil
from gtu.io import tempFileUtil

### from gtu.tesseract import tesseract_util


TESSDATA_PREFIX_DIR = "E:/apps/tesseract-Win64/"
TESSDATA_PREFIX = TESSDATA_PREFIX_DIR + "shared/"


def image_to_string(img, cleanup=True, plus=''):
    envUtil.export("TESSDATA_PREFIX", TESSDATA_PREFIX)
    print("tessdata_prefix = ", envUtil.getEnv("TESSDATA_PREFIX"))
    
    # cleanup为True则识别完成后删除生成的文本文件
    # plus参数为给tesseract的附加高级参数
    subprocess.check_output(TESSDATA_PREFIX_DIR + 'tesseract ' + img + ' ' + img + ' ' + plus, shell=True)  # 生成同名txt文件
    text = ''
    with open(img + '.txt', 'r', encoding = 'utf8')  as f:
        text = f.read().strip()
    if cleanup:
        os.remove(img + '.txt')
    return text




if __name__ == '__main__' :
    img = "E:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/tesseract/test_orc.png"
    text = image_to_string(img)
    print("----->", text)