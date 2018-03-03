package gtu.runtime;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public abstract class ClipboardListener extends Thread implements ClipboardOwner {
    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

    boolean isMointerOn = true;

    public void run() {
        Transferable trans = sysClip.getContents(this);
        if (isMointerOn) {
            regainOwnership(trans);
        }
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
        while (true) {
            try {
                sleep(50);
                Transferable contents = sysClip.getContents(this);
                processContents(contents);
                regainOwnership(contents);
                break;
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                time ++;
                if(time > 10) {
                    break;
                }
            }
        }
    }

    public void processContents(Transferable t) {
        try {
            String text = (String) sysClip.getContents(null).getTransferData(DataFlavor.stringFlavor);
            System.out.println("ProcessContents : " + text);
            processText(text);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public abstract void processText(String text);

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