package gtu.runtime;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.IOUtils;

import gtu.thread.util.ThreadUtil;

public class ProcessWatcher {

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        Process p = Runtime.getRuntime().exec("cmd /c start telnet ptt.cc");
        ProcessWatcher.newInstance(p).getStream();
        System.out.println(p);
        System.out.println(p.waitFor());
        System.out.println(p.exitValue());
        System.out.println("done...");
    }

    private String encode = "big5";
    private Process process;
    private byte[] inputStreamBytes;
    private byte[] errorStreamBytes;
    private long timeout = 0;

    private ProcessWatcher(Process process) {
        this.process = process;
    }

    public ProcessWatcher getStream() throws IOException, TimeoutException {
        getStream(0);
        return this;
    }
    
    public ProcessWatcher getStream(long timeout) throws IOException, TimeoutException {
        this.timeout = timeout;
        inputStreamBytes = getInputStream(process.getInputStream());
        errorStreamBytes = getInputStream(process.getErrorStream());
        return this;
    }

    public static ProcessWatcher newInstance(Process process) {
        return new ProcessWatcher(process);
    }

    private byte[] getInputStream(final InputStream is) throws IOException, TimeoutException {
        return ThreadUtil.getFutureResult(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
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
        return inputStreamBytes;
    }

    public byte[] getErrorStreamBytes() {
        return errorStreamBytes;
    }
}
