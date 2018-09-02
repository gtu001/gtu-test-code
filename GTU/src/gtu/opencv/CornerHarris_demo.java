package gtu.opencv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CornerHarris_demo {
    static {
        OpenCVLoader.init();
    }

    public static void main(String[] args) {
        String imagePath = "C:\\Users\\gtu00\\OneDrive\\Desktop\\33239101020_d20d6905a9_z.jpg";
        Mat oriImg = Imgcodecs.imread(imagePath);

        Imgproc.cvtColor(oriImg, oriImg, Imgproc.COLOR_BGR2GRAY);
        HighGui.namedWindow("source", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow("source", oriImg);

        cornerHarris_demo(oriImg);

        HighGui.waitKey();
        System.out.println("done...");
    }

    private static void cornerHarris_demo(Mat oriImg) {
        Mat dst = Mat.zeros(oriImg.size(), CvType.CV_32FC1);
        Mat dst_norm = new Mat(oriImg.size(), dst.type());
        Mat dst_norm_scaled = new Mat(oriImg.size(), dst.type());
        int blockSize = 2;
        int apertureSize = 3;
        double k = 0.04;

        int thresh = 200;

        Imgproc.cornerHarris(oriImg, dst, blockSize, apertureSize, k);
        Core.normalize(dst, dst_norm, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());
        Core.convertScaleAbs(dst, dst_norm_scaled);

        for (int j = 0; j < dst_norm.rows(); j++) {
            for (int i = 0; i < dst_norm.cols(); i++) {
                if (dst_norm.get(j, i)[0] > thresh) {
                    Imgproc.circle(dst_norm_scaled, new Point(i, j), 5, new Scalar(0), 2, 8, 0);
                }
            }
        }

        HighGui.namedWindow("cornerHarris", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow("cornerHarris", dst_norm_scaled);
    }
}
