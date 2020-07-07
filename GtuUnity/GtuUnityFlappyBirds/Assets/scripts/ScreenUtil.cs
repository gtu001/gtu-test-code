using UnityEngine;

public class ScreenUtil {

    public static void SetResolution(int width, int height, bool fullScreen, int fps) {
        Screen.SetResolution(width, height, fullScreen, fps);
    }
}