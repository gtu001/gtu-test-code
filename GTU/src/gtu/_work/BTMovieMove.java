package gtu._work;

import gtu.file.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class BTMovieMove {

    String btUnCompleteFileEnd = ".td";// 未完成的副檔名
    long bigFileSize = 5000000;// 5mb 大於此變數的檔案大小會被保留
    File movieDir = new File("D:\\TDDOWNLOAD");// 要處理的下載目錄
    boolean deleteNonWorkingDir = true;// 是否要刪除完成目錄

    Set<String> movieSubSet = new HashSet<String>();// 要特別保留的檔案格式
    Set<String> currentSubFileNameSet = new HashSet<String>();// 掃瞄過目錄發現的檔案格是
    StringBuffer logSb = new StringBuffer();

    public BTMovieMove() {
        String[] subFile = ".avi,.wmv,.mp4,.srt,.sub,.mkv,.rar,.rmvb,.idx,.zip,.7z,.flv,.asf,.ass".split(",", -1);
        movieSubSet.addAll(Arrays.asList(subFile));
    }

    public static void main(String[] args) {
        BTMovieMove mm = new BTMovieMove();
        mm.execute();
        System.out.println(mm.logSb);
    }

    public void execute() {
        checkSubFile();

        List<File> dirList = new ArrayList<File>();
        for (File d : movieDir.listFiles()) {
            if (d.isDirectory()) {
                dirList.add(d);
            }
        }
        for (File d : dirList) {
            moveOutCompleteFiles(d);
        }
        for (File d : dirList) {
            if (!isHasAnyDownloadFile(d)) {
                logAppend("可移除的目錄 =>" + d);
                deleteAllDir(d, deleteNonWorkingDir);
            }
        }
    }

    private void deleteAllDir(File dir, boolean doDelete) {
        List<File> listFile = new ArrayList<File>();
        FileUtil.searchFilefind(dir, ".*", listFile);
        for (File f : listFile) {
            logAppend("\tDel=>" + f.getName());
            if (doDelete) {
                f.delete();
                logAppend("\t" + (f.exists() ? "刪除失敗" : "刪除成功"));
            }
        }
        if (doDelete) {
            dir.delete();
        }
        FileUtil.deleteEmptyDir(dir);
    }

    private void checkSubFile() {
        List<File> listFile = new ArrayList<File>();
        FileUtil.searchFilefind(movieDir, ".*", listFile);
        int unCompleteCount = 0;
        for (File f : listFile) {
            String fileName = f.getName();
            fileName = fileName.substring(fileName.lastIndexOf("."));
            currentSubFileNameSet.add(fileName);
            if (f.getName().endsWith(btUnCompleteFileEnd) && f.length() > bigFileSize) {
                logAppend("下載中==>" + f);
                unCompleteCount++;
            }
        }
        for (File f : listFile) {
            if (!f.getName().endsWith(btUnCompleteFileEnd) && f.length() > bigFileSize) {
                logAppend("已完成==>" + f);
            }
        }
        logAppend("所有檔案格式:" + currentSubFileNameSet);
        logAppend("下載中未完成 : " + unCompleteCount);
    }

    private void moveOutCompleteFiles(File dir) {
        List<File> listFile = new ArrayList<File>();
        FileUtil.searchFilefind(dir, ".*", listFile);
        boolean findUnCompleteFile = false;
        for (File f : listFile) {
            if (f.getName().endsWith(btUnCompleteFileEnd)) {
                findUnCompleteFile = true;
                break;
            }
        }
        if (!findUnCompleteFile) {
            for (File f : listFile) {
                if (f.length() > bigFileSize) {
                    this.moveFile(f);
                }
                String fname = f.getName().toLowerCase();
                fname = fname.substring(fname.lastIndexOf("."));
                if (movieSubSet.contains(fname)) {
                    this.moveFile(f);
                }
            }
        }
    }

    private void moveFile(File f) {
        boolean result = false;
        File moveToFile = null;
        int index = 0;
        do {
            if(index > 10){
                break;
            }
            if (moveToFile == null) {
                moveToFile = new File(this.movieDir, f.getName());
            } else {
                String newName = new StringBuilder().append(f.getName().substring(0, f.getName().lastIndexOf("."))).append("_").append(index)
                        .append(f.getName().substring(f.getName().lastIndexOf("."))).toString();
                moveToFile = new File(this.movieDir, newName);
                ++index;
            }
            result = f.renameTo(moveToFile);
        } while (!(result));
        logAppend(new StringBuilder().append("\t搬擋案:").append(moveToFile).append((result) ? "成功" : "失敗").toString());
    }

    private boolean isHasAnyDownloadFile(File dir) {
        List<File> listFile = new ArrayList<File>();
        FileUtil.searchFilefind(dir, ".*", listFile);
        for (File f : listFile) {
            if (f.getName().equals("黑暗聖殿.zip")) {
                continue;
            }
            for (String subName : this.movieSubSet) {
                if (f.getName().toLowerCase().endsWith(subName)) {
                    return true;
                }
            }
            if (f.getName().endsWith(this.btUnCompleteFileEnd)) {
                return true;
            }
            if (f.length() > this.bigFileSize) {
                return true;
            }
        }
        return false;
    }

    private void logAppend(String message) {
        logSb.append(message + "\n");
    }

    public String getBtUnCompleteFileEnd() {
        return btUnCompleteFileEnd;
    }

    public void setBtUnCompleteFileEnd(String btUnCompleteFileEnd) {
        this.btUnCompleteFileEnd = btUnCompleteFileEnd;
    }

    public long getBigFileSize() {
        return bigFileSize;
    }

    public void setBigFileSize(long bigFileSize) {
        this.bigFileSize = bigFileSize;
    }

    public File getMovieDir() {
        return movieDir;
    }

    public void setMovieDir(File movieDir) {
        this.movieDir = movieDir;
    }

    public boolean isDeleteNonWorkingDir() {
        return deleteNonWorkingDir;
    }

    public void setDeleteNonWorkingDir(boolean deleteNonWorkingDir) {
        this.deleteNonWorkingDir = deleteNonWorkingDir;
    }

    public Set<String> getMovieSubSet() {
        return movieSubSet;
    }

    public void setMovieSubSet(Set<String> movieSubSet) {
        this.movieSubSet = movieSubSet;
    }

    public Set<String> getCurrentSubFileNameSet() {
        return currentSubFileNameSet;
    }

    public void setCurrentSubFileNameSet(Set<String> currentSubFileNameSet) {
        this.currentSubFileNameSet = currentSubFileNameSet;
    }

    public StringBuffer getLogSb() {
        return logSb;
    }

    public void setLogSb(StringBuffer logSb) {
        this.logSb = logSb;
    }
}
