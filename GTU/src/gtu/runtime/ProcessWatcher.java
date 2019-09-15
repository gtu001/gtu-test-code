package gtu.runtime;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import gtu.thread.util.ThreadUtil;

public class ProcessWatcher {

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        Process p = Runtime.getRuntime().exec("cmd /c start telnet ptt.cc");
        ProcessWatcher.newInstance(p).getStreamSync();
        System.out.println(p);
        System.out.println(p.waitFor());
        System.out.println(p.exitValue());
        System.out.println("done...");
    }

    private String encode = "big5";
    private Process process;
    private AtomicReference<byte[]> inputStreamBytes = new AtomicReference<byte[]>();
    private AtomicReference<byte[]> errorStreamBytes = new AtomicReference<byte[]>();
    private long timeout = 0;

    private ProcessWatcher(Process process) {
        this.process = process;
    }

    public ProcessWatcher getStreamSync() {
        System.out.println("watcher --- 1");
        getStreamSync(0);
        System.out.println("watcher --- 2");
        return this;
    }

    public ProcessWatcher getStreamSync(long timeout) {
        this.timeout = timeout;
        try {
            inputStreamBytes.set(getInputStream("input", timeout));
            errorStreamBytes.set(getInputStream("error", timeout));
        } catch (Exception e) {
            throw new RuntimeException("getStreamSync ERR " + e.getMessage(), e);
        }
        return this;
    }

    public ProcessWatcher getStreamAsync() throws IOException, TimeoutException {
        System.out.println("watcher --- 1");
        processInputStreamAsync("input");
        processInputStreamAsync("error");
        System.out.println("watcher --- 2");
        return this;
    }

    public static ProcessWatcher newInstance(Process process) {
        return new ProcessWatcher(process);
    }

    private void processInputStreamAsync(final String type) throws IOException, TimeoutException {
        // linux 不work
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = null;
                    if ("input".equals(type)) {
                        is = process.getInputStream();
                    } else if ("error".equals(type)) {
                        is = process.getErrorStream();
                    }
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] content = new byte[1024];
                    int pos = -1;
                    while ((pos = bis.read(content)) != -1) {
                        baos.write(content, 0, pos);
                    }
                    bis.close();
                    baos.flush();
                    baos.close();
                    byte[] arry = baos.toByteArray();
                    if ("input".equals(type)) {
                        inputStreamBytes.set(arry);
                        ;
                        System.out.println("# input stream done!!!");
                    } else if ("error".equals(type)) {
                        errorStreamBytes.set(arry);
                        ;
                        System.out.println("# error stream done!!!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void processAsyncCallback(final ActionListener listener, final long waittimeLimit) {
        final long LOOP_TIME = 3000L;
        new Thread(new Runnable() {
            @Override
            public void run() {
                long currentWaitSummary = 0;
                while ((inputStreamBytes.get() == null || errorStreamBytes.get() == null)) {
                    if ((waittimeLimit > 0 && currentWaitSummary >= waittimeLimit)) {
                        break;
                    }
                    try {
                        Thread.sleep(LOOP_TIME);
                        currentWaitSummary += LOOP_TIME;
                    } catch (InterruptedException e) {
                    }
                }
                ActionEvent e = new ActionEvent(ProcessWatcher.this, -1, "ok");
                listener.actionPerformed(e);
            }
        }).start();
    }

    private byte[] Thread(Runnable runnable) {
        // TODO Auto-generated method stub
        return null;
    }

    private byte[] getInputStream(final String type, long timeout) throws IOException, TimeoutException {
        // linux 不work
        return ThreadUtil.getFutureResult(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                InputStream is = null;
                if ("input".equals(type)) {
                    is = process.getInputStream();
                } else if ("error".equals(type)) {
                    is = process.getErrorStream();
                }
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] content = new byte[1024];
                int pos = -1;
                while ((pos = bis.read(content)) != -1) {
                    baos.write(content, 0, pos);
                }
                bis.close();
                baos.flush();
                baos.close();
                return baos.toByteArray();
            }
        }, timeout);
    }

    public Process getProcess() {
        return process;
    }

    public ProcessWatcher encode(String encode) {
        this.encode = encode;
        return this;
    }

    public String getInputStreamToString() {
        byte[] bs = getInputStreamBytes();
        if (bs == null) {
            return "";
        }
        try {
            return new String(bs, encode);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getErrorStreamToString() {
        byte[] bs = getErrorStreamBytes();
        if (bs == null) {
            return "";
        }
        try {
            return new String(bs, encode);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getInputStreamBytes() {
        return inputStreamBytes.get();
    }

    public byte[] getErrorStreamBytes() {
        return errorStreamBytes.get();
    }

    public static String getOutputStreamString(Process process, String type, String encoding) {
        try {
            InputStream is = null;
            if ("input".equals(type)) {
                is = process.getInputStream();
            } else if ("error".equals(type)) {
                is = process.getErrorStream();
            }
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] content = new byte[1024];
            int pos = -1;

            while ((pos = bis.read(content)) != -1) {
                baos.write(content, 0, pos);
            }
            bis.close();
            baos.flush();
            baos.close();
            return new String(baos.toByteArray(), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "getOutputStreamString ERROR";
    }
}
