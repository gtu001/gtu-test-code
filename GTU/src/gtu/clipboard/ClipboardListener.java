package gtu.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.text.SimpleDateFormat;
import java.util.Date;

import gtu.swing.util.JCommonUtil;

public abstract class ClipboardListener extends Thread implements ClipboardOwner {

    public static void main(String[] args) {
        ClipboardListener listener = new ClipboardListener() {
            @Override
            public void processText(String text) {
            }
        };
        listener.start();
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS");

    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

    boolean isMointerOn = true;

    public void run() {
        Transferable trans = getContentsTransferable();
        regainOwnership(trans);
        System.out.println("Listening to board...");

        while (true) {
            sleepForce(60 * 60 * 1000);
        }
    }

    private void sleepForce(long time) {
        try {
            sleep(time);
        } catch (Throwable e) {
        }
    }

    public void lostOwnership(Clipboard c, Transferable t) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(getTimestamp() + " " + "lostOwnership");
                Throwable exceptoin = null;

                while (true) {
                    try {
                        sleepForce(50);

                        Transferable contents = getContentsTransferable();
                        if (contents == null) {
//                            System.out.println(getTimestamp() + " " + " retry lostOwnership");
                            sleepForce(1000);
                            continue;
                        }

                        boolean result = processContents(contents);

                        // 非文字格式不重新取得owner
                        if (result) {
                            regainOwnership(contents);
                            break;
                        } else {
//                            System.out.println(getTimestamp() + " " + " retry lostOwnership");
                            sleepForce(1000);
                            continue;
                        }
                    } catch (Throwable e) {
                        exceptoin = e;
                    }
                }

                if (exceptoin != null) {
                    JCommonUtil.handleException(exceptoin);
                }
            }
        }).start();
    }

    private Transferable getContentsTransferable() {
        DataFlavor[] flavors = null;
        try {
            flavors = sysClip.getAvailableDataFlavors();
            
            if(flavors == null) {
                return null;
            }

            // 跳過檔案不處理
            if (flavors.length == 1 && flavors[0].equals(DataFlavor.javaFileListFlavor)) {
                return null;
            }

            return sysClip.getContents(this);
        } catch (Throwable ex) {
            ex.printStackTrace();

            System.out.println("-----------------------------------------------------");
            try {
                for (DataFlavor d : flavors) {
                    Object val = sysClip.getData(d);
                    System.out.println(d + "\t" + val);
                }
                System.out.println("size = " + flavors.length);
            } catch (Throwable ex2) {
            }
            System.out.println("-----------------------------------------------------");

            return null;
        }
    }

    public abstract void processText(String text);

    public boolean processContents(Transferable t) {
        if (!isMointerOn) {
            return false;
        }
        try {
            String text = (String) sysClip.getContents(this).getTransferData(DataFlavor.stringFlavor);
            System.out.println("ProcessContents : " + text);
            processText(text);
            return true;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String getTimestamp() {
        return SDF.format(new Date());
    }

    private void regainOwnership(Transferable t) {
        System.out.println(getTimestamp() + " " + "regainOwnership");
        sysClip.setContents(t, this);
    }

    public boolean isMointerOn() {
        return isMointerOn;
    }

    public void setMointerOn(boolean isMointerOn) {
        System.out.println(getTimestamp() + " " + "setMointerOn = " + isMointerOn);
        this.isMointerOn = isMointerOn;
    }
}