package com.example.utils;

public class StringUtilsEx {

    public static String leftPad(String str, int length, char pad){
        str = defaultString(str);
        int total = length - str.length();
        StringBuilder sb = new StringBuilder();
        for(int ii = 0 ; ii < total; ii ++){
            sb.append(pad);
        }
        sb.append(str);
        return sb.toString();
    }
    
    public static String defaultString(String str){
        if(str == null){
            return "";
        }
        return str;
    }
}
