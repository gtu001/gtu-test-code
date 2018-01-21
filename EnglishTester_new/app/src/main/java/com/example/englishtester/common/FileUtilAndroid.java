package com.example.englishtester.common;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;

public class FileUtilAndroid {

    /**
     * 取得內部資料夾
     */
    public static File getFileDir(Context context){
        return context.getFilesDir();
    }

    /**
     * 建立公用目錄
     */
    public static File getExtermalStoragePublicDir(String albumName) {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if(file.mkdir()){
            File f = new File(file, albumName);
            if(f.mkdir()){
                return f;
            }
        }
        return new File(file, albumName);
    }

    /**
     * 外部儲存空間是否可讀寫
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 外部空間是否可以讀取
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 一般的copy
     */
    public static boolean copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] arrayOfByte = new byte[4096];
        BufferedInputStream input = new BufferedInputStream(inputStream);
        BufferedOutputStream output = new BufferedOutputStream(outputStream);
        int i;
        while ((i = input.read(arrayOfByte, 0, arrayOfByte.length)) != -1) {
            output.write(arrayOfByte, 0, i);
        }
        output.close();
        input.close();
        return true;
    }

    /**
     * 讀取檔案成為字串
     */
    public static String loadFileToString(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"));
        StringBuffer sb = new StringBuffer();
        for (String line = null; (line = reader.readLine()) != null;) {
            sb.append(line + "\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * 寫檔案
     */
    public static void saveToFile(File file, String text) throws IOException {
        LineNumberReader reader = new LineNumberReader(new StringReader(text));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"));
        for (String line = null; (line = reader.readLine()) != null;) {
            writer.write(line);
            writer.newLine();
        }
        reader.close();
        writer.flush();
        writer.close();
    }

}
