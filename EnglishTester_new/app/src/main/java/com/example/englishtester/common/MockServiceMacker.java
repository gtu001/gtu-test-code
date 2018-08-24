package com.example.englishtester.common;

import android.content.Context;
import android.widget.Toast;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wistronits on 2018/8/24.
 */

public class MockServiceMacker {

    public static void getMockStuff__test001() {
//        DropboxFileLoadService stuff = mock(DropboxFileLoadService.class);
//
//        Answer mockAnswer = new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Toast.makeText(context, "dropbox accessToken未初始化!", Toast.LENGTH_SHORT).show();
//                return null;
//            }
//        };
//        Answer mockAnswer2 = new Answer<Void>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                Toast.makeText(context, "dropbox accessToken未初始化!", Toast.LENGTH_SHORT).show();
//                return null;
//            }
//        };
//
//        doAnswer(mockAnswer).when(stuff).downloadFile(anyString());
//        doAnswer(mockAnswer).when(stuff).downloadFile(anyString(), anyString());
//        doAnswer(mockAnswer).when(stuff).downloadHtmlReferencePicDir(anyString(), anyLong());
//        doAnswer(mockAnswer).when(stuff).listFile();
//        doAnswer(mockAnswer).when(stuff).listFileV2();
//        try {
//            doAnswer(mockAnswer).when(stuff).uploadFile((File) any(), anyString());
//        } catch (Exception e) {
//        }
//        return stuff;
    }

    public static <INTERFACE, SERVICE_CLZ> INTERFACE getMockStuff(final String mockMessage, final Context context, Class<SERVICE_CLZ> clz) {
        class Handler implements InvocationHandler {
            Context context;
            String message;

            public Handler(Context context, String message) {
                this.context = context;
                this.message = message;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        return (INTERFACE) Proxy.newProxyInstance(clz.getClassLoader(), clz.getInterfaces(), new Handler(context, mockMessage));
    }
}
