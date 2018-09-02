package com.example.gtuandroid.component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 类名：ResManager.java
 * 
 * @author wader 类描述：获取工程中assets目下的文字、图片等资源 创建时间：2011-11-29 16:07
 */
public class ResManager {

    /**
     * 从工程资源加载图片资源（路径是assets/images/**.png）
     *
     * @param fileName
     *            图片资源路径
     */
    public static Bitmap loadImageRes(Activity activity, String fileName) {
        Bitmap bitmap = null;
        InputStream is = null;
        FileInputStream fis = null;
        try {
            is = activity.getAssets().open("images/" + fileName + ".png");
            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
            } finally {
                is = null;
                fis = null;
            }
        }
        return bitmap;
    }

    /**
     * 读取配置文件读取配置信息
     *
     * @param filename
     *            配置文件路径
     * @return 包含配置信息的hashmap键值对
     */
    public static ArrayList<String> loadProperties(String filename, Context context) {
        Log.d("loadProperties", "loadProperties");
        ArrayList<String> properties = new ArrayList<String>();
        InputStream is = null;
        FileInputStream fis = null;
        InputStreamReader rin = null;

        // 将配置文件放到res/raw/目录下，可以通过以下的方法获取
        // is = context.getResources().openRawResource(R.raw.system);

        // 这是读取配置文件的第二种方法
        // 将配置文件放到assets目录下，可以通过以下的方法获取
        // is = context.getAssets().open("system.properties");

        // 用来提取键值对的临时字符串
        StringBuffer tempStr = new StringBuffer();

        // 用来存放读取的每个字符
        int ch = 0;

        // 用来保存读取的配置文件一行的信息
        String line = null;
        try {
            Log.d("loadProperties", "the filename is: " + filename);
            is = context.getAssets().open("textRes/" + filename + ".properties");
            rin = new InputStreamReader(is, "UTF-8");

            while (ch != -1) {
                tempStr.delete(0, tempStr.length());
                while ((ch = rin.read()) != -1) {
                    if (ch != '\n') {
                        tempStr.append((char) ch);
                    } else {
                        break;
                    }
                }
                line = tempStr.toString().trim();
                Log.d("loadProperties", "line: " + line);
                // 判断读出的那行数据是否有效,#开头的代表注释,如果是注释行那么跳过下面,继续上面操作
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                properties.add(line);
            }
        } catch (IOException e) {
             Log.e("read property file", e.getMessage() + "fail");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (null != rin) {
                    rin.close();
                }
            } catch (IOException e) {
                Log.e("read property file", e.getMessage() + "fail");
            } finally {
                is = null;
                fis = null;
                rin = null;
            }
        }
        return properties;
    }
}
