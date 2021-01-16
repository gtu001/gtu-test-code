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




'''
https://lufor129.medium.com/opencv%E5%9C%96%E7%89%87%E8%99%95%E7%90%86%E6%95%B4%E7%90%86-%E7%B7%A9%E6%85%A2%E6%9B%B4%E6%96%B0-b45e248d14bb
'''


#讀取圖片 默認為BGR 需要轉成RGB並plt印出來
img = cv.imread("./lHV8B.jpg")
img = cv.cvtColor(img,cv.COLOR_BGR2RGB)
plt.imshow(img)
plt.show()

# 轉黑白 imshow要設定cmap = gray
img_gray = cv.cvtColor(img,cv.COLOR_BGR2GRAY)
plt.imshow(img_gray,cmap="gray")
plt.show()

# 圖片縮放
#縮放方法：縮小圖像用INTER_AREA,放大圖像用INTER_CUBIC,效果較好
img_resize = cv.resize(img,(200,200),interpolation = cv.INTER_CUBIC)

#開啟鏡頭 (如果你有的話)
cap = cv.VideoCapture(0)

#設定影像尺寸大小
cap.set(cv.CAP_PROP_FRAME_WIDTH, 300)
cap.set(cv.CAP_PROP_FRAME_HEIGHT, 300)

# 寫入avi
fourcc = cv.VideoWriter_fourcc(*'XVID')

#path,格式,fps,size,灰階 (True為彩 False 為灰階)
out1 = cv.VideoWriter(path+"/"+str(now)+"_video.avi",fourcc, 1, (640,480),True)
out2 = cv.VideoWriter(path+"/"+str(now)+"_mask.avi",fourcc, 1, (640,480),False)

while(True):
    # 讀取影像
    ret, frame = cap.read() 
    # 顯示影像
    cv.imshow("vedio",frame）
    
    # 寫入影像
    out.write(frame)
    # 按下q離開
    if(cv.waitKey(1) & 0xFF == ord('q')):
        break
    
#關閉視窗
cv.destroyAllWindows()

#開啟影片檔案
cap = cv.VideoCapture('output.avi')

#從影片檔案讀取影像並顯示出來
while(True):
    ret, frame = cap.read()
    if ret == False:
        break
    
    cv.imshow('frame', frame)
    cv.waitKey(20)
    
#釋放攝影機
cap.release()

#關閉視窗
cv.destroyAllWindows()

# 分別代表(影像,頂點座標,對向頂點座標,顏色,線條寬度（-1為填滿）)
cv.rectangle(img,(x,y),(x+w,y+h),(0,255,0),2)

# 畫圓型 (影像,圓心座標,半徑,顏色,線條寬度)
cv.circle(img,(x,y),30,(0,255,0),3)

# 畫多邊形 (設定座標點)
arr = np.array([[20,20],[30,30],[50,30]],np.int32)

#多邊形（影像,頂點座標,封閉形,顏色,線條寬度）
cv.polylines(img, [arr], True, (255, 255, 0), 4)


# 二值化有四種模式 
# cv.THRESH_BINARY、cv.THRESH_BINARY_INV
# cv.THRESH_TRUNC、cv.THRESH_TOZERO# THRESH_BINARY設定門檻值，要是小於門檻則歸0(黑色)，大於門檻則依設定調整
ret, thresh1 = cv.threshold(image_gray,127,255,cv.THRESH_BINARY)

# THRESH_BINARY_INV恰好相反
ret, thresh2 = cv.threshold(image_gray,127,255,cv.THRESH_BINARY_INV)

# THRESH_TRUNC 小於門檻值不變，大於則設為門檻值，第三個參數沒有意義
ret, thresh3 = cv.threshold(image_gray,127,255,cv.THRESH_TRUNC)

# THRESH_TOZERO 大於門檻值不變，小於門檻值設為0，第三個參數沒有意義
ret, thresh4 = cv.threshold(image_gray,127,255,cv.THRESH_TOZERO)




# 找出Luna頭髮紫色部分
img = cv.imread("./lHV8B.jpg")
hsv_img = cv.cvtColor(img, cv.COLOR_BGR2HSV)

# 建立紫色上下界線並建立Mask
lower_purple = np.array([277//2,50,50])
upper_purple = np.array([300//2,255,255])
hsvMask = cv.inRange(hsv_img, lower_purple, upper_purple)

# mask 做bitwise運算
hsvMask_output = cv.bitwise_and(img,img,None, mask=hsvMask)





# 如果想要動態調整，可以自己寫一個bar來做調整。

img = cv.imread("./lHV8B.jpg")
HSV_Min = np.array([0, 0, 0])
HSV_Max = np.array([0, 0, 0])
#定義六個拉桿的最大最小值
def H_Lower(val):
    HSV_Min[0] = val
def H_Upper(val):
    HSV_Max[0] = valdef S_Lower(val):
    HSV_Min[1] = val
def S_Upper(val):
    HSV_Max[1] = valdef V_Lower(val):
    HSV_Min[2] = val
def V_Upper(val):
    HSV_Max[2] = valcv.namedWindow('HSV_TrackBar')
cv.createTrackbar('H_Lower', 'HSV_TrackBar', 0, 180, H_Lower) 
cv.createTrackbar('H_Upper', 'HSV_TrackBar', 0, 180, H_Upper)
cv.createTrackbar('S_Lower', 'HSV_TrackBar', 0, 255, S_Lower)
cv.createTrackbar('S_Upper', 'HSV_TrackBar', 0, 255, S_Upper)
cv.createTrackbar('V_Lower', 'HSV_TrackBar', 0, 255, V_Lower)
cv.createTrackbar('V_Upper', 'HSV_TrackBar', 0, 255, V_Upper)
#主程式
while True:
    #先將原圖檔(彩色BGR)轉成HSV色彩空間
    hsv_key = cv.cvtColor(img, cv.COLOR_BGR2HSV)
    
    #套用拉桿上的數值變化到HSV圖檔和原圖擋上
    hsv_result = cv.inRange(hsv_key, HSV_Min, HSV_Max) 
    hsvMask_output = cv.bitwise_and(img, img, None, mask =  hsv_result)
    
    #將圖檔顯示在 'HSV_TrackBar' 視窗並將原圖檔一併顯示出來做比較
    cv.imshow('HSV_TrackBar', hsv_result)
    cv.imshow('HSV_mask_result',hsvMask_output)
    
    #定義一個按鍵(這邊使用'esc')結束視窗
    if cv.waitKey(1) == 27:
        break
cv.destroyAllWindows()




# 侵蝕膨脹 (Erode、Dilate)
img = cv.imread("./lHV8B.jpg")
kernel = np.ones((5,5),np.float32)/25
# iteration 為kernel 執行次數
dilate_4 = cv.dilate(img,kernel,iterations = 4)
erode_4 = cv.erode(img,kernel,iterations = 4)
opening = cv.morphologyEx(img, cv.MORPH_OPEN, kernel, iterations=2)
closing = cv.morphologyEx(img, cv.MORPH_CLOSE, kernel, iterations=2)
# 膨脹圖-侵蝕圖 可以得到圖片大致輪廓
gradient = cv.morphologyEx(img,cv.MORPH_GRADIENT,kernel,iterations=1)




# 濾波器
img = cv.imread("./lHV8B.jpg")
img = cv.cvtColor(img,cv.COLOR_BGR2RGB)
# 方框濾波
BoxBlur = cv.boxFilter(img,0,(5,5))
# 均值濾波，常見模糊化方法
Blur = cv.blur(img,(5,5))
# 中值濾波，常用於除燥
medianBlur = cv.medianBlur(img,5)
# 雙邊，有去燥效果又能保持邊緣，但計算較久
bilateral = cv.bilateralFilter(img,9,75,75)
# 高斯濾波，模糊化效果比均值好
Gaussian = cv.GaussianBlur(img, (5, 5),0)



if __name__ == '__main__':
    
    # checkSelf.checkMembersToHtml(cv2)
    main()

    print("done...")
