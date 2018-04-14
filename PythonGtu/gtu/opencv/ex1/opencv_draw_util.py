
import numpy as np
import cv2
from matplotlib import pyplot as plt

from gtu.reflect import checkSelf
from gtu.opencv.ex1 import opencv_image_util as gtu_cv2

''' import 這行
from gtu.opencv.ex1 import opencv_draw_util
'''

class _ParentDraw :
    def __init__(self):
        self.thick1 = 1
        self.rgb1 = (0, 0, 255)
    def rgb(self, red, green, blue):
        self.rgb1 = (blue, green, red)
        return self
    def thick(self, val):
        self.thick1 = val
        return self
    def full(self):
        self.thick1 = -1
        return self
    

        
class Line(_ParentDraw) :
    def __init__(self):
        _ParentDraw.__init__(self)
        pass
    def start(self, x, y):
        self.startPos = (x, y)
        return self
    def end(self, x, y):
        self.endPos = (x, y)
        return self
    def full(self):
        raise Exception("不支援")
    def draw(self, img):
        cv2.line(img, self.startPos, self.endPos, self.rgb1, self.thick1)
        
        
        
class Rectangle(_ParentDraw) :
    def __init__(self):
        _ParentDraw.__init__(self)
        pass
    def start(self, x, y):
        self.startPos = (x, y)
        return self
    def end(self, x, y):
        self.endPos = (x, y)
        return self
    def draw(self, img):
        cv2.rectangle(img, self.startPos, self.endPos, self.rgb1, self.thick1)
        
        
        
class Circle(_ParentDraw) :
    def __init__(self):
        _ParentDraw.__init__(self)
        pass
    def center(self, x, y):
        self.centerPos = (x, y)
        return self
    def radius(self, radius):
        self.radius = radius
        return self
    def draw(self, img):
        cv2.circle(img, self.centerPos, self.radius, self.rgb1, self.thick1)
        
        

class Polygon(_ParentDraw) :
    def __init__(self):
        _ParentDraw.__init__(self)
        self.arry = []
        self.connectAll = True
        pass
    def point(self, x, y):
        self.arry.append([x, y])
        return self
    #False : 從第一個點連到最後一個點(不封閉)
    def connectClose(self, val):
        self.connectAll = val
        return self
    def draw(self, img):
        pts = np.array(self.arry, np.int32)
        pts = pts.reshape((-1, 1, 2))
        img = cv2.polylines(img, [pts], self.connectAll, self.rgb1)
    

class Ellipse(_ParentDraw):
    def __init__(self):
        _ParentDraw.__init__(self)
        self.startAngle = 0
        self.endAngle = 0
        self.circle = 360
        pass
    def center(self, x, y):
        self.centerPos = (x, y)
        return self
    #寬高
    def axes_wh(self, w, h):
        self.axes = (w, h)
        return self
    def startAngle(self, startAngle):
        self.startAngle = startAngle
        return self;
    def endAngle(self, endAngle):
        self.endAngle = endAngle
        return self;
    def circle(self, circle):
        self.circle = circle
        if not (self.circle >= 0 and self.circle <= 360):
            raise Exception("請輸入 0~360")
        return self
    def draw(self, img):
        cv2.ellipse(img, self.centerPos, self.axes, self.startAngle, self.endAngle, self.circle, self.rgb1, self.thick1)
        


class Text(_ParentDraw):
    def __init__(self):
        _ParentDraw.__init__(self)
        self.font1 = cv2.FONT_HERSHEY_SIMPLEX
        self.scale1 = 1
        self.thick1 = 2
        self.rgb(0, 0, 255)
        pass
    def full(self):
        raise Exception("不支援")
    def position(self, x, y):
        self.pos = (x, y)
        return self
    def font(self, font):
        self.font1 = font
        return self
    def scale(self, fontSize):
        self.scale1 = fontSize
        return self
    def draw(self, img, text):
        cv2.putText(img, text, self.pos, self.font1, self.scale1, self.rgb1, self.thick1)
        
        


if __name__ == '__main__':
    
    categoriesUtil = gtu_cv2.NonCategories()

    img = categoriesUtil.createBlackImg(500, 1000)

    Line().start(50, 70).end(600, 100).draw(img)
    Rectangle().start(100, 100).end(200, 200).draw(img)
    Circle().center(300, 200).radius(100).draw(img)
    Ellipse().center(400, 100).axes_wh(300, 100).draw(img)
    Polygon().point(10,105).point(20,130).point(70,120).point(50,110).connectClose(True).draw(img)
    Text().position(200, 400).draw(img, "test text ok !!")
    
    gtu_cv2.Imshow().default("test", img)
    
    print("done...")
    
