package gtu.youtube;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.date.DateUtil;
import gtu.file.FileUtil;

public class DownloadProgressHandler {
    private static final int BUFFER_SIZE = 2048;
    private static final double ONE_HUNDRED = 100;
    private static final double KB = 1024;

    // 百分比提醒度(小於 10mb 設 0, 100mb 設1, 大檔設2以上 )
    private int percentScale = 0;

    private long totalLength = 1;
    private BigDecimal percent = new BigDecimal(0);
    private long startTime = -1;
    private String totalLengthDescription;
    private InputStream instream;
    private OutputStream outstream;
    private List<DownloadProgress> processLst = new ArrayList<DownloadProgress>();

    public static class DownloadProgress {
        long currentLength;
        long totalLength;
        String totalLengthDescription;
        BigDecimal percent;
        int kbpers;
        long remainSec;
        String remainDescrpition;
        boolean isComplete = false;

        long startTime;
        long endTime;
        long during;
        List<DownloadProgress> processLst;

        public long getCurrentLength() {
            return currentLength;
        }

        public long getTotalLength() {
            return totalLength;
        }

        public String getTotalLengthDescription() {
            return totalLengthDescription;
        }

        public BigDecimal getPercent() {
            return percent;
        }

        public int getKbpers() {
            return kbpers;
        }

        public long getRemainSec() {
            return remainSec;
        }

        public String getRemainDescrpition() {
            return remainDescrpition;
        }

        public boolean isComplete() {
            return isComplete;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public long getDuring() {
            return during;
        }

        public List<DownloadProgress> getProcessLst() {
            return processLst;
        }

        public String toString() {
            return "大小 :" + FileUtil.getSizeDescription(currentLength) + " / " + totalLengthDescription + ", 百分比:" + percent + "% (" + kbpers + "KB/s) , 剩餘時間 : " + remainDescrpition;
        }
    }

    public void setProgressPerformd(ActionListener progressPerformd) {
        this.progressPerformd = progressPerformd;
    }

    private ActionListener progressPerformd = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DownloadProgress vo = (DownloadProgress) e.getSource();
            System.out.println(vo);
        }
    };

    private BigDecimal getPercent(long currentTotal) {
        try {
            BigDecimal p = new BigDecimal(((double) currentTotal / (double) totalLength) * ONE_HUNDRED);
            p = p.setScale(percentScale, BigDecimal.ROUND_DOWN);
            return p;
        } catch (Exception ex) {
            return new BigDecimal(-1);
        }
    }

    private void setCurrentTotal(long currentTotal) {
        BigDecimal percent = getPercent(currentTotal);

        if (!percent.equals(this.percent)) {
            // 計算KB/S
            int kbpers = 0;
            // 剩餘時間
            long remainSec = 999999L;
            try {
                double s = (System.currentTimeMillis() - startTime) / 1000;
                kbpers = (int) ((currentTotal / KB) / s);
                remainSec = (this.totalLength - currentTotal) / kbpers;
            } catch (Exception ex) {
            }

            String remainDescrpition = DateUtil.wasteTotalTime(remainSec);

            // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            DownloadProgress vo = new DownloadProgress();
            vo.currentLength = currentTotal;
            vo.kbpers = kbpers;
            vo.percent = percent;
            vo.totalLength = this.totalLength;
            vo.totalLengthDescription = FileUtil.getSizeDescription(currentTotal);
            vo.remainDescrpition = remainDescrpition;
            vo.startTime = this.startTime;
            processLst.add(vo);

            progressPerformd.actionPerformed(new ActionEvent(vo, -1, percent + "%"));
            // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

            this.percent = percent;
        }
    }

    private int caculatePercentScale(long totalLength) {
        // "byte","kb", "mb", "gb"
        Pattern ptn = Pattern.compile("([\\d\\.]+)(byte|kb|mb|gb)");
        Matcher mth = ptn.matcher(FileUtil.getSizeDescription(totalLength));
        if (!mth.find()) {
            throw new RuntimeException("無法計算檔案長度!");
        }
        float size = Float.parseFloat(mth.group(1));
        String unit = mth.group(2);

        if ("byte".equals(unit) || "kb".equals(unit)) {
            return 0;
        } else if ("mb".equals(unit) && size < 400) {
            return 1;
        } else if ("mb".equals(unit) && size >= 400) {
            return 2;
        }
        return 3;
    }

    public DownloadProgressHandler(long totalLength, InputStream instream, OutputStream outstream, Integer percentScale) throws IOException {
        this.totalLength = totalLength;
        this.totalLengthDescription = FileUtil.getSizeDescription(totalLength);

        this.instream = instream;
        this.outstream = outstream;

        this.processLst = new ArrayList<DownloadProgress>();

        // 計算percent敏銳度
        if (percentScale == null) {
            this.percentScale = caculatePercentScale(this.totalLength);
        } else {
            this.percentScale = percentScale;
        }
    }

    public void start() throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];

            long currentTotal = 0;
            int count = -1;

            this.startTime = System.currentTimeMillis();

            while ((count = instream.read(buffer)) != -1) {
                // 目前進行總長度
                currentTotal += count;
                this.setCurrentTotal(currentTotal);

                outstream.write(buffer, 0, count);
            }

            outstream.flush();
        } finally {
            outstream.close();

            long endtime = System.currentTimeMillis();
            long during = System.currentTimeMillis() - startTime;
            System.out.println("下載進度100% !!" + (during / 1000) + "秒");

            // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            DownloadProgress p = new DownloadProgress();
            p.currentLength = this.totalLength;
            p.totalLength = this.totalLength;
            p.totalLengthDescription = this.totalLengthDescription;
            p.percent = new BigDecimal(100);
            p.kbpers = 0;
            p.remainSec = 0;
            p.remainDescrpition = DateUtil.wasteTotalTime(during);
            p.isComplete = true;
            p.startTime = this.startTime;
            p.endTime = endtime;
            p.during = during;
            p.processLst = this.processLst;

            progressPerformd.actionPerformed(new ActionEvent(p, -1, percent + "%"));
            // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        }
    }
}