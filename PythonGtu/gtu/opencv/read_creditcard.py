'''
https://www.pyimagesearch.com/2017/07/17/credit-card-ocr-with-opencv-and-python/
'''
# construct the argument parser and parse the arguments

import argparse

import cv2
from imutils import contours
import imutils
import numpy

import numpy as np

from gtu.reflect import checkSelf
from gtu.io import tempFileUtil
from gtu.tesseract import tesseract_util



def rgb2gray(rgb):
    return np.dot(rgb[..., :3], [0.299, 0.587, 0.114])


def contrastImg(img, contrast=1.5, brightness=0.7):
    img = np.array(img, dtype=np.uint8)
    mean = np.mean(img)
    img = img - mean
    img = img * contrast + mean * brightness  # 修对比度和亮度
    img = img / 255.  # 非常关键，没有会白屏
    return img


def showImg(winId, ref):
    cv2.imshow(winId, ref)
    cv2.waitKey(0)


def getPicText(img):
    filepath = tempFileUtil.createTempFile(".png")
    cv2.imwrite(filepath, img)
    text = tesseract_util.image_to_string(filepath)
    print(text)
    

# 用來判斷 數值是否落在平均值
class InnerCompare :
    def __init__(self, percent=0.05):
        self.map = dict()
        self.map2 = dict()
        self.percent = percent
        pass
    
    def isLikeNum(self, val1, val2):
        v1 = val1 + (val1 * self.percent)
        v2 = val1 - (val1 * self.percent)
        if v1 >= val2 and v2 <= val2 :
            return True
        return False
    
    def putMapArry(self, k, arry):
        lst = list()
        if self.map2.__contains__(k) :
            lst = self.map2[k]
        lst.append(arry)
        self.map2[k] = lst
    
    def putMap(self, val, arry):
        for k, v in self.map.items():
            if self.isLikeNum(k, val):
                self.map[k] += 1
                self.putMapArry(k, arry)
                return
        self.map[val] = 1
        self.putMapArry(val, arry)
        
    def getLst(self, k):
        return self.map2.get(k)
    
# 用來判斷 數值是否落在平均值
class CreditBlockChecker :
 
    def __init__(self):
        self.lst = list()
        pass        
         
    def append(self, arry):
        (x, y, w, h) = arry
        self.lst.append(arry)
        
    def getAverageNum(self):
        # 比較Y軸位置
        compareY = InnerCompare()
        for (i, v) in enumerate(self.lst) :
            (x, y, w, h) = v
            compareY.putMap(y, v)
        
        for (k, v) in compareY.map.items():
            print("Y軸值為 {}, 有 {}組 --> {}".format(k , v, compareY.getLst(k)))
            if v == 4 :
                return compareY.getLst(k)
            elif v > 4 :
                # 比較寬度
                groupLst = compareY.map2[k]
                compareWidth = InnerCompare(0.20)
                for (i, v) in enumerate(groupLst):
                    (x, y, w, h) = v
                    compareWidth.putMap(w, v)
                
                for (k, v) in compareWidth.map.items():
                    if v == 4 :
                        print("寬度值為 {}, 有 {}組 --> {}".format(k , v, compareWidth.getLst(k)))
                        return compareWidth.map2[k]
        return None

#-----------------------------------------------------------------------------------------------
# main process
#-----------------------------------------------------------------------------------------------


ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", required=True, help="path to input image")
ap.add_argument("-r", "--reference", required=True, help="path to reference OCR-A image")
# args = vars(ap.parse_args()) #使用console帶參數
args = {
        "image":"C:/Users/gtu00/OneDrive/Desktop/dataSience/creditcard/IMG_20180301_170454_HDR.jpg",  # 正常(差一點)
#         "image":"C:/Users/gtu00/OneDrive/Desktop/dataSience/creditcard/IMG_20180312_120247.jpg",  #正常
#         "image":"C:/Users/gtu00/OneDrive/Desktop/dataSience/creditcard/IMG_20180312_120239.jpg", #沒抓到
#         "image":"C:/Users/gtu00/OneDrive/Desktop/dataSience/creditcard/IMG_20180312_120231.jpg",#正常(差一點)

#             "image":"C:/Users/gtu00/OneDrive/Desktop/dataSience/creditcard/IMG_20180312_131252.jpg", #一整塊
#             "image":"C:/Users/gtu00/OneDrive/Desktop/dataSience/creditcard/IMG_20180312_131302.jpg", #前面被判定為一塊
#             "image":"C:/Users/gtu00/OneDrive/Desktop/dataSience/creditcard/IMG_20180312_131310.jpg", #抓到三個
#             "image":"C:/Users/gtu00/OneDrive/Desktop/dataSience/creditcard/IMG_20180312_131319.jpg",
        "reference":"E:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/opencv/ocr-b-font-254x300.png"
        }


# define a dictionary that maps the first digit of a credit card
# number to the credit card type
FIRST_NUMBER = {
    "3": "American Express",
    "4": "Visa",
    "5": "MasterCard",
    "6": "Discover Card"
}


# load the reference OCR-A image from disk, convert it to grayscale,
# and threshold it, such that the digits appear as *white* on a
# *black* background
# and invert it, such that the digits appear as *white* on a *black*
ref = cv2.imread(args["reference"])
ref = cv2.cvtColor(ref, cv2.COLOR_BGR2GRAY)  # 轉成灰階
ref = cv2.threshold(ref, 10, 255, cv2.THRESH_BINARY_INV)[1]  # 顏色invert

# find contours in the OCR-A image (i.e,. the outlines of the digits)
# sort them from left to right, and initialize a dictionary to map
# digit name to the ROI
refCnts = cv2.findContours(ref.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
refCnts = refCnts[0] if imutils.is_cv2() else refCnts[1]
refCnts = contours.sort_contours(refCnts, method="left-to-right")[0]
digits = {}

# loop over the OCR-A reference contours
for (i, c) in enumerate(refCnts):
    # compute the bounding box for the digit, extract it, and resize
    # it to a fixed size
    (x, y, w, h) = cv2.boundingRect(c)
    roi = ref[y:y + h, x:x + w]
    roi = cv2.resize(roi, (57, 88))
    
    # update the digits dictionary, mapping the digit name to the ROI
    digits[i] = roi
    


# initialize a rectangular (wider than it is tall) and square
# structuring kernel
rectKernel = cv2.getStructuringElement(cv2.MORPH_RECT, (9, 3))
sqKernel = cv2.getStructuringElement(cv2.MORPH_RECT, (5, 5))





# load the input image, resize it, and convert it to grayscale
image = cv2.imread(args["image"])

image2 = contrastImg(image, contrast=3, brightness=1.2)  #---------------------------------custom
image2 = imutils.resize(image2, width=300)

image = imutils.resize(image, width=300)
image = image.astype("uint8")

gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)




# apply a tophat (whitehat) morphological operator to find light
# regions against a dark background (i.e., the credit card numbers)
tophat = cv2.morphologyEx(gray, cv2.MORPH_TOPHAT, rectKernel)


# compute the Scharr gradient of the tophat image, then scale
# the rest back into the range [0, 255]
gradX = cv2.Sobel(tophat, ddepth=cv2.CV_32F, dx=1, dy=0, ksize=-1)
gradX = np.absolute(gradX)
(minVal, maxVal) = (np.min(gradX), np.max(gradX))
gradX = (255 * ((gradX - minVal) / (maxVal - minVal)))
gradX = gradX.astype("uint8")




# apply a closing operation using the rectangular kernel to help
# cloes gaps in between credit card number digits, then apply
# Otsu's thresholding method to binarize the image
gradX = cv2.morphologyEx(gradX, cv2.MORPH_CLOSE, rectKernel)
thresh = cv2.threshold(gradX, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)[1]


 
# apply a second closing operation to the binary image, again
# to help close gaps between credit card number regions
thresh = cv2.morphologyEx(thresh, cv2.MORPH_CLOSE, sqKernel)


# find contours in the thresholded image, then initialize the
# list of digit locations
cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL,
    cv2.CHAIN_APPROX_SIMPLE)
cnts = cnts[0] if imutils.is_cv2() else cnts[1]
locs = []



checkSelf.document(cv2.dilate)
checkSelf.document(cv2.erode )
checkSelf.document(cv2.getStructuringElement)



# loop over the contours
for (i, c) in enumerate(cnts):
    
    def showInnerBlock(var):  #---------------------------------custom
        (x, y, w, h) = var
        group = gray[y - 5:y + h + 5, x - 5:x + w + 5]
        showImg("showInnerBlock", group)
        
        
    # compute the bounding box of the contour, then use the
    # bounding box coordinates to derive the aspect ratio
    (x, y, w, h) = cv2.boundingRect(c)
    ar = w / float(h)
    
    print("x={}, y={}, w={}, h={} --> ar:{}".format(x, y, w, h, ar))
    
#     showInnerBlock((x, y, w, h))
    
    # since credit cards used a fixed size fonts with 4 groups
    # of 4 digits, we can prune potential contours based on the
    # aspect ratio
    
    # if ar >= 2.5 and ar <= 4.0: #---------------------------------custom
    if ar >= 1.6 and ar <= 4.0:
        # contours can further be pruned on minimum/maximum width
        # and height
        # if (w >= 40 and w <= 55) and (h >= 10 and h <= 20):#---------------------------------custom
        if (w >= 40 and w <= 55) and (h >= 10 and h <= 30):
            # append the bounding box region of the digits group
            # to our locations list
            locs.append((x, y, w, h))
            # showInnerBlock((x, y, w, h)) #---------------------------------custom
            print("# append -", (x, y, w, h));
            


avg = CreditBlockChecker()  #---------------------------------custom
for (i, c) in enumerate(locs) :
    avg.append(c)  # 取得平均y的平均值 
avg.getAverageNum()
 
# for (i, c) in enumerate(locs):
#     if c[1] < avg.minVal or c[1] > avg.maxVal :
#         locs.remove(c)



            
# sort the digit locations from left-to-right, then initialize the
# list of classified digits
locs = sorted(locs, key=lambda x:x[0])
output = []



# loop over the 4 groupings of 4 digits
for (i, (gX, gY, gW, gH)) in enumerate(locs):
    # initialize the list of group digits
    groupOutput = []
 
    # extract the group ROI of 4 digits from the grayscale image,
    # then apply thresholding to segment the digits from the
    # background of the credit card
    group = gray[gY - 5:gY + gH + 5, gX - 5:gX + gW + 5]
    group = cv2.threshold(group, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)[1]
    
    
#     group2 = image2[gY - 5:gY + gH + 5, gX - 5:gX + gW + 5]
#     getPicText(group2)
#     showImg(group2)
 
    # detect the contours of each individual digit in the group,
    # then sort the digit contours from left to right
    digitCnts = cv2.findContours(group.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    digitCnts = digitCnts[0] if imutils.is_cv2() else digitCnts[1]
    digitCnts = contours.sort_contours(digitCnts, method="left-to-right")[0]
    
    
    # loop over the digit contours
    for c in digitCnts:
        # compute the bounding box of the individual digit, extract
        # the digit, and resize it to have the same fixed size as
        # the reference OCR-A images
        (x, y, w, h) = cv2.boundingRect(c)
        roi = group[y:y + h, x:x + w]
        roi = cv2.resize(roi, (57, 88))
 
        # initialize a list of template matching scores    
        scores = []
 
        # loop over the reference digit name and digit ROI
        for (digit, digitROI) in digits.items():
            # apply correlation-based template matching, take the
            # score, and update the scores list
            result = cv2.matchTemplate(roi, digitROI,
                cv2.TM_CCOEFF)
            (_, score, _, _) = cv2.minMaxLoc(result)
            scores.append(score)
 
        # the classification for the digit ROI will be the reference
        # digit name with the *largest* template matching score
        groupOutput.append(str(np.argmax(scores)))
        
        
        
    # draw the digit classifications around the group
    cv2.rectangle(image, (gX - 5, gY - 5),
           (gX + gW + 5, gY + gH + 5), (0, 0, 255), 2)
    cv2.putText(image, "".join(groupOutput), (gX, gY - 15),
           cv2.FONT_HERSHEY_SIMPLEX, 0.65, (0, 0, 255), 2)

    # update the output digits list
    output.extend(groupOutput)
    
    
# print("groupOutput : ", groupOutput)
# display the output credit card information to the screen
# print("Credit Card Type: {}".format(FIRST_NUMBER[output[0]]))
print("Credit Card #: {}".format("".join(output)))
cv2.imshow("Image", image)
cv2.waitKey(0)


