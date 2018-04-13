package gtu.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import gtu.swing.util.JCommonUtil;

public abstract class ClipboardListener extends Thread implements ClipboardOwner {
    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

    boolean isMointerOn = true;

    public void run() {
        Transferable trans = getContentsTransferable();
        regainOwnership(trans);
        System.out.println("Listening to board...");
        
        //just keep alive 看起來不需要
//        while (true) {
//            try {
//                Thread.sleep(10000L);
//            } catch (InterruptedException e) {
//            }
//        }
    }

    public void lostOwnership(Clipboard c, Transferable t) {
        Throwable ex = null;
        while (true) {
            try {
                sleep(50);

                Transferable contents = getContentsTransferable();
                if (contents == null) {
                    break;
                }

                boolean result = processContents(contents);

                // 非文字格式不重新取得owner
                if (result) {
                    regainOwnership(contents);
                }else {
                    continue;
                }

                break;
            } catch (Exception e) {
                ex = e;
            }
        }

        if (ex != null) {
            JCommonUtil.handleException(ex);
        }
    }

    private Transferable getContentsTransferable() {
        try {
            return sysClip.getContents(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public abstract void processText(String text);

    public boolean processContents(Transferable t) {
        if (!isMointerOn) {
            return true;
        }
        try {
            String text = (String) sysClip.getContents(null).getTransferData(DataFlavor.stringFlavor);
            System.out.println("ProcessContents : " + text);
            processText(text);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void regainOwnership(Transferable t) {
        sysClip.setContents(t, this);
    }

    public boolean isMointerOn() {
        return isMointerOn;
    }

    public void setMointerOn(boolean isMointerOn) {
        this.isMointerOn = isMointerOn;
    }
}