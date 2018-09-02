package gtu.net.sharefolder;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import gtu.file.FileUtil;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbNamedPipe;

public class SharedFolderCopyTest {

    /**
     * ErrorCode https://msdn.microsoft.com/en-us/library/cc246324.aspx
     */
    public void smb(String path) {
        try {

            NtlmPasswordAuthentication parm3 = new NtlmPasswordAuthentication("", "gtu001_5F", "123474736");
            int parm2 = SmbNamedPipe.PIPE_TYPE_RDONLY | SmbNamedPipe.PIPE_TYPE_CALL;
            System.out.println("----------XX---------------");
            SmbNamedPipe smbFile = new SmbNamedPipe(path, parm2, parm3);

            if (smbFile.isFile()) {
                System.out.println("----------isFile---------------");
            } else if (smbFile.isDirectory()) {
                System.out.println("----------isDirectory--------------");
                if (smbFile.listFiles().length > 0) {// 裡面有檔案
                    for (int i = 0; i < smbFile.list().length; i++) {
                        System.out.println("----------->>>" + smbFile.getCanonicalPath() + smbFile.list()[i].toString());

//                        loadFile(smbFile.getCanonicalPath() + smbFile.list()[i].toString());
                        loadFile3(smbFile.getCanonicalPath(), smbFile.list()[i].toString());
//                        loadFile(smbFile.listFiles()[i].getCanonicalPath());
//                        loadFile2(smbFile.listFiles()[i]);
                    }
                }
            }
            System.out.println("---------end");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void loadFile2(SmbFile smbFile) throws SmbException, IOException {
        if (smbFile.isFile()) {
            // bfreader=new BufferedReader()
            DataInputStream dsi = new DataInputStream(new BufferedInputStream(smbFile.getInputStream()));
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileOutputStream baos = new FileOutputStream(new File(FileUtil.DESKTOP_DIR, smbFile.getName()));

            byte[] b_reader = new byte[1024 * 4];
            int c = 0;
            while ((c = dsi.read(b_reader)) != -1) {
                baos.write(b_reader, 0, c);
            }
            baos.flush();

            String x = baos.toString();
            System.out.println("-------------->" + x);
        }
    }
    
    private void loadFile3(String name, String relative) throws SmbException, IOException {
        NtlmPasswordAuthentication parm3 = new NtlmPasswordAuthentication("", "gtu001_5F", "123474736");
        int parm2 = SmbNamedPipe.PIPE_TYPE_RDONLY | SmbNamedPipe.PIPE_TYPE_CALL;
        SmbFile sub_SmbFile = new SmbFile(name, relative, parm3);
        System.out.println("exists = " + sub_SmbFile.exists());
        if (sub_SmbFile.isFile()) {
            // bfreader=new BufferedReader()
            DataInputStream dsi = new DataInputStream(new BufferedInputStream(sub_SmbFile.getInputStream()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] b_reader = new byte[1024 * 4];
            int c = 0;
            while ((c = dsi.read(b_reader)) != -1) {
                baos.write(b_reader, 0, c);
                System.out.println(b_reader.length);
            }
            baos.flush();

            String x = baos.toString();
            System.out.println("-------------->" + x);
        }
    }

    /**
     * 不work
     */
    @Deprecated
    private void loadFile(String path) throws SmbException, IOException {
        NtlmPasswordAuthentication parm3 = new NtlmPasswordAuthentication("", "gtu001_5F", "123474736");
        int parm2 = SmbNamedPipe.PIPE_TYPE_RDONLY | SmbNamedPipe.PIPE_TYPE_CALL;
        SmbNamedPipe sub_SmbFile = new SmbNamedPipe(path, parm2, parm3);
        System.out.println("exists = " + sub_SmbFile.exists());
        if (sub_SmbFile.isFile()) {
            // bfreader=new BufferedReader()
            DataInputStream dsi = new DataInputStream(new BufferedInputStream(sub_SmbFile.getInputStream()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] b_reader = new byte[1024 * 4];
            int c = 0;
            while ((c = dsi.read(b_reader)) != -1) {
                baos.write(b_reader, 0, c);
                System.out.println(b_reader.length);
            }
            baos.flush();

            String x = baos.toString();
            System.out.println("-------------->" + x);
        }
    }

    public static void main(String args[]) {
        SharedFolderCopyTest t = new SharedFolderCopyTest();
        // new Smb("smb://192.168.1.39/1/"+Now.getNow_Date_String_2k()+"/");
        t.smb("Smb://GTU001_5F-PC/fun/");
    }
}
