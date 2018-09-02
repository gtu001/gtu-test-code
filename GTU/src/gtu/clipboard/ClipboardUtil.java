package gtu.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * 設定字串到剪貼簿
 * 
 * @author 吉他手 2010/02/27
 * @author Troy 改寫
 * 
 */

public class ClipboardUtil {
    private Clipboard getClipboard() {
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        return systemClipboard;
    }
    
    private static ClipboardUtil INSTANCE = new ClipboardUtil();

    public static ClipboardUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 設定剪貼內容
     * 
     * @param str
     */
    public void setContents(String str) {
        if (str == null) {
            return;
        }
        StringSelection contents = new StringSelection(str);
        getClipboard().setContents(contents, new ClipboardOwner() {
            public void lostOwnership(Clipboard paramClipboard, Transferable paramTransferable) {
            }
        });
    }

    /**
     * 設定剪貼內容
     * 
     * @param str
     */
    public void setContents(Object value) {
        if (value == null) {
            return;
        }
        setContents(value.toString());
    }

    public Object getContents(int dataFlavorIndex) {
        DataFlavor[] dataFlavor = getClipboard().getAvailableDataFlavors();
        Transferable content = getClipboard().getContents(null);
        for (int ii = 0; ii < dataFlavor.length; ii++) {
            try {
                System.out.format("dataFlavor[%d] = %s ==> \t%s\n", ii, dataFlavor[ii], content.getTransferData(dataFlavor[dataFlavorIndex]));
                System.out.format("getData[%d] =  ==> \t%s\n", ii, getClipboard().getData(dataFlavor[dataFlavorIndex]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            return content.getTransferData(dataFlavor[dataFlavorIndex]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 取出剪貼內容
     * 
     * @return
     */
    public String getContents() {
        Transferable content = null;
        while(true) {
            try {
                content = getClipboard().getContents(null);// 給他null照樣return所以懶的設
                break;
            }catch(IllegalStateException ex) {
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e) {
                }
            }
        }
        try {
            return (String) content.getTransferData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // 印出目前剪貼簿內容
        System.out.println(ClipboardUtil.getInstance().getContents());
    }

}