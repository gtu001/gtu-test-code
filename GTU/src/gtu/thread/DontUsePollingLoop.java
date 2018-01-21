package gtu.thread;

import java.io.IOException;
import java.io.InputStream;

public class DontUsePollingLoop {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

    private static class ReadFromPipe_usePollingLoop extends Thread {
        private InputStream pipe;

        @Override
        public void run() {
            int data;
            while (true) {
                synchronized (pipe) {
                    try {
                        while ((data = pipe.read()) == -1) {
                            try {
                                sleep(200);
                                //占用cpu時間..因為仍然要求cpu向pipe詢問資料
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Process data
                }
            }
        }
    }
    
    private static class ReadFromPipe_smartWay extends Thread {
        private InputStream pipe;

        @Override
        public void run() {
            int data;
            while (true) {
                synchronized (pipe) {
                    try {
                        while ((data = pipe.read()) == -1) {
                            try {
                                pipe.wait();
                                //將保持暫停狀態..直到被notify或notifyAll
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Process data
                }
            }
        }
    }
}
