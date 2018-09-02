package gtu.opencv;

import org.opencv.core.Core;

public class OpenCVLoader {

    public static void init() {
        System.out.println("Welcome to OpenCV " + Core.VERSION);

        System.out.println(Core.NATIVE_LIBRARY_NAME);

        // 載入 DLL
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
