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
import inspect


def showImg(ref):
    cv2.imshow("Image", ref)
    cv2.waitKey(0)
    

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", required=True,
    help="path to the input image")
# args = vars(ap.parse_args())


args = dict()
args["image"] = "C:/Users/gtu00/OneDrive/Desktop/creditcard/IMG_20180312_131319.jpg"

# load the image, convert it to grayscale, blur it slightly,
# and threshold it
image = cv2.imread(args["image"])
image = imutils.resize(image, width=500)

gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
blurred = cv2.GaussianBlur(gray, (5, 5), 0)
thresh = cv2.threshold(blurred, 60, 255, cv2.THRESH_BINARY)[1]
 
# find contours in the thresholded image
cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
print(inspect.getdoc(imutils.is_cv2))
cnts = cnts[0] if imutils.is_cv2() else cnts[1]


 
# loop over the contours
for c in cnts:
    # compute the center of the contour
    M = cv2.moments(c)
    cX = int(M["m10"] / M["m00"])
    cY = int(M["m01"] / M["m00"])
 
    # draw the contour and center of the shape on the image
    cv2.drawContours(image, [c], -1, (0, 255, 0), 2)
    cv2.circle(image, (cX, cY), 7, (255, 255, 255), -1)
    cv2.putText(image, "center", (cX - 20, cY - 20),
        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 2)
 
    # show the image
    cv2.imshow("Image", image)
    cv2.waitKey(0)