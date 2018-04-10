package gtu.runtime;

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
        while (true) {
            // 不設sleep會瘋狂吃掉cpu
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
            }
        }
    }

    public void lostOwnership(Clipboard c, Transferable t) {
        int time = 0;
        Throwable ex = null;
        while (true) {
            try {
                sleep(50);
                Transferable contents = getContentsTransferable();
                if (contents == null) {
                    break;
                }

                processContents(contents);
                regainOwnership(contents);
                break;
            } catch (Exception e) {
                ex = e;
                time++;
                if (time > 10) {
                    break;
                }
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

    public void processContents(Transferable t) {
        if (!isMointerOn) {
            return;
        }
        try {
            String text = (String) sysClip.getContents(null).getTransferData(DataFlavor.stringFlavor);
            System.out.println("ProcessContents : " + text);
            processText(text);
        } catch (Exception ex) {
            ex.printStackTrace();
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