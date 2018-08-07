package com.example.epub.com.example.epub.base;


import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by wistronits on 2018/8/7.
 */

public class MyParser implements Parser {
    private static final String TAG = MyParser.class.getSimpleName();

    @Override
    public void parse(Reader contentReader, ParserCallback parserCallback, boolean b) {
        Log.v(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        Log.v(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        Log.v(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        Log.v(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        Log.v(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        Log.v(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    }
}
