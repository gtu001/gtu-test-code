
''' import 這行
from gtu.opencv.ex1 import opencv_image_util
'''

import cv2
from matplotlib import pyplot as plt

from gtu.reflect import checkSelf
import numpy as np


class Imread :
    def __init__(self):
        pass
    
    '''
    Loads a color image. Any transparency of image will be neglected. It is the default flag.
    '''
    def default(self, path):
        print("imread", cv2.IMREAD_COLOR)
        img = cv2.imread(path, cv2.IMREAD_COLOR)
        return img
    
    '''
    Loads image in grayscale mode
    '''
    def grayscale(self, path):
        print("imread", cv2.IMREAD_GRAYSCALE)
        img = cv2.imread(path, cv2.IMREAD_GRAYSCALE)
        return img
    
    '''
    Loads image as such including alpha channel
    '''
    def unchanged(self, path):
        print("imread", cv2.IMREAD_UNCHANGED)
        img = cv2.imread(path, cv2.IMREAD_UNCHANGED)
        return img
    
    

class Imshow :
    def __init__(self):
        pass
    
    '''
            可調整畫面大小
    '''
    def resizeable(self, winId, img):
        cv2.namedWindow(winId, cv2.WINDOW_NORMAL)
        self.__process(winId, img)
        
    '''
            圖片原始大小
    '''
    def default(self, winId, img):
        cv2.namedWindow(winId, cv2.WINDOW_AUTOSIZE)
        self.__process(winId, img)
        
    def __process(self, winId, img):
        def logXYEvent(event, x, y, flags, param):
            if event == cv2.EVENT_LBUTTONDOWN:
                print("pos : x{}, y{}".format(x, y))
        cv2.setMouseCallback(winId, logXYEvent)
        cv2.imshow(winId, img)
        cv2.waitKey(0)
        cv2.destroyAllWindows()
        
    def showAsPlt(self, img):
        plt.imshow(img, cmap='gray', interpolation='bicubic')
        plt.xticks([]), plt.yticks([])  # to hide tick values on X and Y axis  隱藏x,y軸
        plt.show()
        
    def showThenSave(self, winId, img, savepath):
        cv2.namedWindow(winId, cv2.WINDOW_AUTOSIZE)
        cv2.imshow(winId, img)
        k = cv2.waitKey(0)
        cv2.waitKey(0)
        if k == 27:  # ESC
            pass
        elif k == ord('s'):  # s
            cv2.imwrite(savepath, img)
        cv2.destroyAllWindows()


class NonCategories :
    def __init__(self):
        pass
    
    def createBlackImg(self, height, width):
        img = np.zeros((height, width, 3), np.uint8)
        return img
    
    def drawLine(self, img, startX, startY, endX, endY):
        blue = 0
        green = 0
        red = 255
        rgb = (blue, green, red)
        thick = 2
        startPos = (startX, startY)
        endPos = (endX, endY)
        cv2.line(img, startPos, endPos, rgb, thick)
        
        


if __name__ == '__main__':
    
    img = Imread().default("E:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/opencv/ocr_a_font-254x300.jpg")
    
#     Imshow.showAsPlt("test", img)
    Imshow().default("test", img)

#     checkSelf.checkMatchPtnMembers(cv2, "^EVENT\_")
    checkSelf.document(cv2.waitKey)

    print("done...")
    
