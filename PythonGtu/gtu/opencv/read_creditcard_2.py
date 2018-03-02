# import the necessary packages
from imutils import contours
import numpy as np
import argparse
import imutils
import cv2

from gtu.reflect import checkSelf

def showImg(ref):
    cv2.imshow("Image", ref)
    cv2.waitKey(0)

'''
int main( int argc, char** argv ) {    
    IplImage* img = cvLoadImage( argv[1] );    
    cvNamedWindow( “Example1”, CV_WINDOW_AUTOSIZE );    
    cvShowImage( “Example1”, img );    
    cvWaitKey(0);    
    cvReleaseImage( &img );    
    cvDestroyWindow( “Example1” ); 
}
'''

#checkSelf.checkMembers(cv2)

image = cv2.imread("c:/Users/gtu00/OneDrive/Desktop/IMG_20180301_170454_HDR.jpg")

ref = cv2.imread("c:/Users/gtu00/OneDrive/Desktop/IMG_20180301_170454_HDR.jpg")
ref = cv2.cvtColor(ref, cv2.COLOR_BGR2GRAY) #轉成灰階
ref = cv2.threshold(ref, 150, 255, cv2.THRESH_BINARY_INV)[1] #顏色invert

showImg(ref)


