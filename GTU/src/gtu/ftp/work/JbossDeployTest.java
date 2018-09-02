package gtu.ftp.work;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import gtu.ftp.SftpUtil;
import gtu.ftp.SftpUtil.SftpFileInfo;

public class JbossDeployTest {

    SftpUtil test = new SftpUtil();
    String prePath = "/jboss/jboss-eap-7.0/standalone/deployments";
    File dir = new File("C:/Users/gtu00/OneDrive/文件/delpoyments/20180207");
    
    List<ChkStartOk> threadLst = new ArrayList<ChkStartOk>();
    List<ChannelSftp> pushConnLst = new ArrayList<ChannelSftp>();

    public static void main(String[] args) throws JSchException, SftpException, ExecutionException, InterruptedException, TimeoutException {
        JbossDeployTest t = new JbossDeployTest();
        t.execute();
    }

    private void backendDeploy() throws SftpException {
        List<String> backLst = new ArrayList<String>();
        for (File f : dir.listFiles()) {
            if (!f.getName().equals("Web.war")) {
                backLst.add(f.getName());
            }
        }
        
        if(backLst.isEmpty()) {
            System.out.println("後臺無war檔");
            return;
        }
        
        ChannelSftp sftp = test.connect("10.10.2.108", 22, "root", "root123");
        pushConnLst.add(sftp);
        List<SftpFileInfo> currentRemoteLst = new ArrayList<SftpFileInfo>();
        test.scanFileFind(prePath, ".*", currentRemoteLst, false, sftp);

        for (String name : backLst) {
            if (!new File(dir + File.separator + name).isFile()) {
                continue;
            }
            test.upload(prePath, dir + File.separator + name, sftp);
            System.out.println("upload : " + name);

            // 監控
            ChkStartOk t1 = new ChkStartOk(name, prePath, sftp, test);
            t1.start();
            threadLst.add(t1);
        }
    }

    private void frontendDeploy() throws SftpException {
        List<String> frontLst = new ArrayList<String>();
        for (File f : dir.listFiles()) {
            if (f.getName().equals("Web.war")) {
                frontLst.add(f.getName());
            }
        }
        
        if(frontLst.isEmpty()) {
            System.out.println("前臺無war檔");
            return;
        }
        
        ChannelSftp sftp = test.connect("10.10.2.109", 22, "root", "root123");
        pushConnLst.add(sftp);
        List<SftpFileInfo> currentRemoteLst = new ArrayList<SftpFileInfo>();
        test.scanFileFind(prePath, ".*", currentRemoteLst, false, sftp);

        for (String name : frontLst) {
            if (!new File(dir + File.separator + name).isFile()) {
                continue;
            }
            test.upload(prePath, dir + File.separator + name, sftp);
            System.out.println("upload : " + name);

            // 監控
            ChkStartOk t1 = new ChkStartOk(name, prePath, sftp, test);
            t1.start();
            threadLst.add(t1);
        }
    }
    
    private boolean getResult() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Boolean> getVal = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                A: while (true) {
                    boolean allDone = true;
                    B: for (ChkStartOk c : threadLst) {
                        if (c.isDone == false) {
                            allDone = false;
                            break B;
                        }
                    }
                    if (allDone) {
                        break A;
                    }
                    Thread.sleep(500L);
                }
                System.out.println("全部完成!");
                return true;
            }
        });
        return getVal.get(5 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    private void disconnectAll() {
        try {
            for(ChannelSftp s : pushConnLst) {
                s.disconnect();
            }
        }catch(Exception ex) {
        }
    }
    
    private void execute() throws SftpException, ExecutionException, InterruptedException, TimeoutException {
        this.frontendDeploy();
        this.backendDeploy();
        this.getResult();
        this.disconnectAll();
        System.out.println("done...");
    }

    private class ChkStartOk extends Thread {
        String warName;
        String remoteDir;
        ChannelSftp sftp;
        SftpUtil test;
        boolean isStart = false;
        boolean isDone = false;

        private ChkStartOk(String warName, String remoteDir, ChannelSftp sftp, SftpUtil test) {
            this.warName = warName;
            this.remoteDir = remoteDir;
            this.sftp = sftp;
            this.test = test;
            this.setDaemon(false);
        }

        private boolean isStartDeployExists() throws SftpException {
            final String startName = String.format("%s.isdeploying", warName);
            Vector<LsEntry> lst = test.listFiles(remoteDir, sftp);
            for (LsEntry en : lst) {
                if (en.getFilename().equals(startName)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isDeployeDoneExists() throws SftpException {
            final String okName = String.format("%s.deployed", warName);
            Vector<LsEntry> lst = test.listFiles(remoteDir, sftp);
            for (LsEntry en : lst) {
                if (en.getFilename().equals(okName)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void run() {
            try {
                int doneCount = 0;
                while (true) {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                    }

                    if (!isStart) {
                        if (isStartDeployExists()) {
                            isStart = true;
                        }
                    } else {
                        if (doneCount == 3) {
                            isDone = true;
                            System.out.println("### " + warName + " deploye completed !");
                            break;
                        }
                        if (!isStartDeployExists() && isDeployeDoneExists()) {
                            doneCount++;
                        }
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
