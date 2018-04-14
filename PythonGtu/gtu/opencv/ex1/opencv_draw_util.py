
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
        self.thick = 1
        self.rgb = (0, 0, 255)
    def rgb(self, red, green, blue):
        self.rgb = (blue, green, red)
        return self
    def thick(self, val):
        self.thick = val
        return self
    def full(self):
        self.thick = -1
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
        cv2.line(img, self.startPos, self.endPos, self.rgb, self.thick)
        
        
        
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
        cv2.rectangle(img, self.startPos, self.endPos, self.rgb, self.thick)
        
        
        
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
        cv2.circle(img, self.centerPos, self.radius, self.rgb, self.thick)
        
        

class Polygon(_ParentDraw) :
    def __init__(self):
        _ParentDraw.__init__(self)
        self.arry = []
        pass
    def point(self, x, y):
        self.arry.append([x, y])
        return self
    def draw(self, img):
        pts = np.array(self.arry, np.int32)
        pts = pts.reshape((-1, 1, 2))
        img = cv2.polylines(img, [pts], True, self.rgb)
    

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
        cv2.ellipse(img, self.centerPos, self.axes, self.startAngle, self.endAngle, self.circle, self.rgb, self.thick)


if __name__ == '__main__':
    
    categoriesUtil = gtu_cv2.NonCategories()

    img = categoriesUtil.createBlackImg(500, 1000)

    Line().start(50, 70).end(600, 100).draw(img)
    Rectangle().start(100, 100).end(200, 200).draw(img)
    Circle().center(300, 200).radius(100).draw(img)
    Ellipse().center(400, 100).axes_wh(300, 100).draw(img)
    Polygon().point(10,5).point(20,30).point(70,20).point(50,10).draw(img)
    
    gtu_cv2.Imshow().default("test", img)

    print("done...")
    
