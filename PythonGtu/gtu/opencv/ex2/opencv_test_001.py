import cv2 as cv2
import sys

from gtu.io import fileUtil
from gtu.reflect import checkSelf


def main() :
    img = cv2.imread('C:/Users/wistronits/Desktop/VanGogh-starry_night.jpg')
    if img is None:
        sys.exit("Could not read the image.")
    cv2.imshow("Display window", img)
    k = cv2.waitKey(0)
    if k == ord("s"):
        cv2.imwrite("starry_night.png", img)




if __name__ == '__main__':
    
    # checkSelf.checkMembersToHtml(cv2)
    main()

    print("done...")
