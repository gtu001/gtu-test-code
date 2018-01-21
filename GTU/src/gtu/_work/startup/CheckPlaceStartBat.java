package gtu._work.startup;

import gtu.runtime.ProcessWatcher;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 檢查所在位置是家裡還是公司等.. 執行bat檔
 * 
 * @author Troy
 */
public class CheckPlaceStartBat {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        CheckPlaceStartBat test = new CheckPlaceStartBat();
        test.execute();
        System.out.println("done...");
    }

    public void execute() throws IOException {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String arg = null;
        if (InetAddress.getByName("192.168.2.14").isReachable(3000)) {
            arg = "COMPANY";
        } else {
            arg = "HOME";
        }

        String command = String.format("cmd /c start %s %s", "c:\\start.bat", arg);
        System.out.println(command);
        ProcessWatcher.newInstance(Runtime.getRuntime().exec(command)).getStream();
        System.exit(0);
    }
}
