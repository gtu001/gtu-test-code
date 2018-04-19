package gtu.opencv;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.fuco.ocr.Main4;

public class ProtrudeWordProcessHelper {
    
    static {
        OpenCVLoader.init();
    }

    private static class BorderType {
        private static final int BORDER_CONSTANT = 0;
        private static final int BORDER_DEFAULT = 4;
        private static final int BORDER_ISOLATED = 16;
        private static final int BORDER_REFLECT = 2;
        private static final int BORDER_REFLECT101 = 4;
        private static final int BORDER_REFLECT_101 = 4;
        private static final int BORDER_REPLICATE = 1;
        private static final int BORDER_TRANSPARENT = 5;
        private static final int BORDER_WRAP = 3;
    }

    private ProtrudeWordProcessHelper() {
    }

    public static ProtrudeWordProcessHelper newInstance() {
        return new ProtrudeWordProcessHelper();
    }

    private Mat orignMat;

    public ProtrudeWordProcessHelper orignMat(Mat orignMat) {
        this.orignMat = orignMat;
        return this;
    }

    enum Process {
        MedianBlur() {
            @Override
            Mat _apply(Mat orign) {
                Imgproc.medianBlur(orign, orign, 3);
                return orign;
            }
        }, //
        Dilate() {
            @Override
            Mat _apply(Mat orign) {
                Mat kernel = new Kernel().getStructuringElement();
                Imgproc.dilate(orign, orign, kernel, new Point(-1, -1), 1);
                return orign;
            }
        }, //
        Erode() {
            @Override
            Mat _apply(Mat orign) {
                Mat kernel = new Kernel().getStructuringElement();
                Imgproc.erode(orign, orign, kernel, new Point(-1, -1), 2);
                return orign;
            }
        }, //
        Contrast() {
            @Override
            Mat _apply(Mat orign) {
                int value = 12;
                final Mat dst = new Mat(orign.rows(), orign.cols(), orign.type());
                orign.convertTo(dst, -1, 10d * value / 100, 0);
                return dst;
            }
        }, //
        ContrastNew() {
            @Override
            Mat _apply(Mat orign) {
                // double alpha = 1.0; /* < Simple contrast control */
                // int beta = 0; /* < Simple brightness control */
                double alpha = 1.3;
                int beta = 5;
                Mat copyTo = sameScaleNewMat(orign);
                for (int y = 0; y < orign.rows(); y++) {
                    for (int x = 0; x < orign.cols(); x++) {
                        double[] newArry = new double[3];
                        for (int c = 0; c < 3; c++) {
                            newArry[c] = (alpha * (orign.get(y, x)[c]) + beta);
                        }
                        copyTo.put(y, x, newArry);
                    }
                }
                return copyTo;
            }
        }, //
        BilateralFilter() {
            @Override
            Mat _apply(Mat orign) {
                int d = 5;
                double sigmaVal = 150;
                double sigmaColor = sigmaVal;
                double sigmaSpace = sigmaVal;
                int borderType = BorderType.BORDER_DEFAULT;
                Mat newMat = sameScaleNewMat(orign);
                Imgproc.bilateralFilter(orign, newMat, d, sigmaColor, sigmaSpace, borderType);
                return newMat;
            }
        }, //
        Blur() {
            @Override
            Mat _apply(Mat orign) {
                Size ksize = new Size(2, 2);
                Point anchor = new Point(-1, -1);
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.blur(orign, orign, ksize, anchor, borderType);
                return orign;
            }
        }, //
        BoxFilter() {
            @Override
            Mat _apply(Mat orign) {
                int ddepth = -1;
                Size ksize = new Size(2, 2);
                Point anchor = new Point(-1, -1);
                boolean normalize = true;
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.boxFilter(orign, orign, ddepth, ksize, anchor, normalize, borderType);
                return orign;
            }
        }, //
        Filter2D() {
            @Override
            Mat _apply(Mat orign) {
                int ddepth = -1;
                Mat kernel = new Kernel().getCustomKernal();
                Point anchor = new Point(-1, -1);
                double delta = 1;
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.filter2D(orign, orign, ddepth, kernel, anchor, delta, borderType);
                return orign;
            }
        }, //
        GaussianBlur() {
            @Override
            Mat _apply(Mat orign) {
                Size ksize = new Size(3, 3);
                double sigmaX = 150;
                double sigmaY = 150;
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.GaussianBlur(orign, orign, ksize, sigmaX, sigmaY, borderType);
                return orign;
            }
        }, //
        MorphologyEx() {
            @Override
            Mat _apply(Mat orign) {
                int op = 1;// 1
                Mat kernel = new Kernel().getStructuringElement();
                Point anchor = new Point(-1, -1);
                int iterations = 1;
                Imgproc.morphologyEx(orign, orign, op, kernel, anchor, iterations);
                return orign;
            }
        }, //
        Laplacian() {
            @Override
            Mat _apply(Mat orign) {
                int ddepth = -1;
                int ksize = 1;// +odd
                double scale = 1;
                double delta = 0;
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.Laplacian(orign, orign, ddepth, ksize, scale, delta, borderType);
                // orign.convertTo(orign, CvType.CV_64FC(19));
                return orign;
            }
        }, //
        Sobel() {
            @Override
            Mat _apply(Mat orign) {
                int ddepth = -1;
                int dx = 1;
                int dy = 1;
                int ksize = 3;// +odd
                double scale = 1;
                double delta = 0;
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.Sobel(orign, orign, ddepth, dx, dy, ksize, scale, delta, borderType);
                return orign;
            }
        }, //
        Gray() {
            @Override
            Mat _apply(Mat orign) {
                Imgproc.cvtColor(orign, orign, Imgproc.COLOR_BGR2GRAY);
                return orign;
            }
        }, //
        PyrDown() {
            @Override
            Mat _apply(Mat orign) {
                Size dstsize = new Size((orign.cols() + 1) / 2, (orign.rows() + 1) / 2);
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.pyrDown(orign, orign, dstsize, borderType);
                return orign;
            }
        }, //
        PyrUp() {
            @Override
            Mat _apply(Mat orign) {
                Size dstsize = new Size((orign.cols() * 2), (orign.rows() * 2));
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.pyrUp(orign, orign, dstsize, borderType);
                return orign;
            }
        }, //
        PyrMeanShiftFiltering() {
            @Override
            Mat _apply(Mat orign) {
                double sp = 0;
                double sr = 0;
                int maxLevel = 1;
                TermCriteria termcrit = new TermCriteria();
                Imgproc.pyrMeanShiftFiltering(orign, orign, sp, sr, maxLevel, termcrit);
                return orign;
            }
        }, //
        Scharr() {
            @Override
            Mat _apply(Mat orign) {
                int ddepth = -1;
                int dx = 1;
                int dy = (dx == 1 ? 0 : 1);
                double scale = 1;
                double delta = 0;
                Imgproc.Scharr(orign, orign, ddepth, dx, dy, scale, delta);
                return orign;
            }
        }, //
        SepFilter2D() {
            @Override
            Mat _apply(Mat orign) {
                int ddepth = -1;
                Kernel4SepFilter2D kernel = new Kernel4SepFilter2D();
                Point anchor = new Point(-1, -1);
                double delta = 0;
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.sepFilter2D(orign, orign, ddepth, kernel.getX(), kernel.getY(), anchor, delta, borderType);
                return orign;
            }
        }, //
        SqrBoxFilter() {// TODO
            @Override
            Mat _apply(Mat orign) {
                int ddepth = -1;
                Size ksize = new Size(3, 3);
                Point anchor = new Point(-1, -1);
                boolean normalize = true;
                int borderType = BorderType.BORDER_DEFAULT;
                Imgproc.sqrBoxFilter(orign, orign, ddepth, ksize, anchor, normalize, borderType);
                return orign;
            }
        }, //
        Reverse() {
            @Override
            Mat _apply(Mat orign) {
                Mat lookUpTable = new Mat(1, 256, CvType.CV_8U);
                for (int ii = 0; ii < 256; ii++) {
                    lookUpTable.put(0, ii, 255 - ii);
                }
                Core.LUT(orign, lookUpTable, orign);
                return orign;
            }
        }, //
        GammaCorrection() {
            @Override
            Mat _apply(Mat orign) {
                int gamma = 2;//>=0
                Mat lookUpTable = new Mat(1, 256, CvType.CV_8U);
                for (int ii = 0; ii < 256; ii++) {
                    lookUpTable.put(0, ii, Math.pow(ii / 255.0, gamma) * 255);
                }
                Core.LUT(orign, lookUpTable, orign);
                return orign;
            }
        }, //
        TEST() {// TODO
            @Override
            Mat _apply(Mat orign) {
                return orign;
            }
        },//

        ;

        public Mat apply(Mat orign) {
            // HighGui.imshow("before : " + this.name(), orign);
            Mat mat = _apply(orign.clone());
            // HighGui.imshow("after : " + this.name(), mat);
            return mat;
        }

        abstract Mat _apply(Mat orign);

        public static Process getByOrdinal(int val) {
            for (Process e : Process.values()) {
                if (e.ordinal() == val) {
                    return e;
                }
            }
            throw new RuntimeException("超出:" + val);
        }

        private static Mat sameScaleNewMat(Mat oriImg) {
            Mat distImg2 = new Mat(oriImg.rows(), oriImg.cols(), oriImg.type());
            return distImg2;
        }

        private class Kernel {
            private Mat getGaussianKernel() {
                int ksize = 3;// +odd
                double sigma = 150;
                int ktype__CV_64F = 6;
                int ktype__CV_32F = 5;
                Mat mat = Imgproc.getGaussianKernel(ksize, sigma, ktype__CV_32F);
                return mat;
            }

            private Mat getStructuringElement() {
                Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2), new Point(-1, -1));
                return kernel;
            }

            private Mat getCustomKernal() {
                // 自定义核
                // 0 -1 0
                // -1 5 -1
                // 0 -1 0
                Mat kernel = new Mat(3, 3, CvType.CV_16SC1);
                kernel.put(0, 0, 0, -1, 0, -1, 5, -1, 0, -1, 0);
                return kernel;
            }
        }

        private class Kernel4SepFilter2D {
            // cv::Sobel(image, dX, CV_32F, 1, 0);
            // cv::Sobel(image, dY, CV_32F, 0, 1);
            private Mat getX() {
                Mat mat = new Mat(1, 1, CvType.CV_32F);
                Imgproc.Sobel(mat, mat, -1, 1, 0, 3, 1, 0);
                return mat;
            }

            private Mat getY() {
                Mat mat = new Mat(1, 1, CvType.CV_32F);
                Imgproc.Sobel(mat, mat, -1, 0, 1, 3, 1, 0);
                return mat;
            }
        }
    }

    private static void showMat(Mat orign) {
        for (int x = 0; x < orign.rows(); x++) {
            for (int y = 0; y < orign.cols(); y++) {
                System.out.format("r%d, c%d = %s %n", x, y, Arrays.toString(orign.get(x, y)));
            }
        }
    }

    private Mat processMain(Mat mat) throws Exception {
        Mat orign = mat.clone();
        HighGui.imshow("before : ", orign);

        mat = Process.ContrastNew.apply(mat);
        mat = Process.MorphologyEx.apply(mat);
        mat = Process.Filter2D.apply(mat);

        HighGui.imshow("after : ", mat);
        return mat;
    }

    public Mat build() {
        try {
            Mat originImage = orignMat.clone();

            System.out.println("1originImage = " + originImage);

            Mat result = this.processMain(originImage);

            HighGui.waitKey();

            this.testOcr(result);

            return result;
        } catch (Exception ex) {
            throw new RuntimeException("ProtrudeWordProcessHelper.build ERR : " + ex.getMessage(), ex);
        } finally {
        }
    }

    private void testOcr(Mat mat) {
        try {
            String imagePath = "E:/workstuff/workstuff/workspace_ccocr/ccocr/src/common/test_card_number.png";
            String resultStr = Main4.doOCR(mat, imagePath, false, false);
            System.out.println("ORIGN:" + "4637819103321101");
            System.out.println("OCR  :" + resultStr);
        } catch (Exception ex) {
            throw new RuntimeException("Err : " + ex.getMessage(), ex);
        }
    }

    public static void main(String[] args) throws Exception {
        // showMembers(Imgcodecs.class);
        // showMembers(Imgproc.class);
        String imagePath = "E:/workstuff/workstuff/workspace_ccocr/ccocr/src/common/test_card_number.png";
        Mat oriImg = Imgcodecs.imread(imagePath);

        Mat resultMat = ProtrudeWordProcessHelper.newInstance().orignMat(oriImg).build();

        System.out.println("done..");
        System.exit(1);
    }

    public static void mainXX(String[] args) throws Exception {
        System.out.println("done..");
    }

    private static void showMembers(Class clz) {
        System.out.println("============================================================");
        System.out.println("START =>> " + clz.getSimpleName());
        Set<String> set = new LinkedHashSet<String>();
        for (Method m : clz.getDeclaredMethods()) {
            if (Modifier.isPublic(m.getModifiers())) {
                set.add(m.getName());
            }
        }
        for (String v : set) {
            System.out.println(v);
        }
    }
}
