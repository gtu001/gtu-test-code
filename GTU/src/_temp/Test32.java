package _temp;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import gtu.runtime.ClipboardUtil;

public class Test32 {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String val = ClipboardUtil.getInstance().getContents();
                    if (StringUtils.isNotBlank(val)) {
                        System.out.println("----" + val);
                        ClipboardUtil.getInstance().setContents("");
                    } else {
                        try {
                            Thread.sleep(100l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
