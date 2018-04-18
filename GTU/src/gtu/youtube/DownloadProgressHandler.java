package gtu.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.file.FileUtil;

public class DownloadProgressHandler {
    private static final int BUFFER_SIZE = 2048;
    private static final double ONE_HUNDRED = 100;
    private static final double KB = 1024;

    // 百分比提醒度(小於 10mb 設 0, 100mb 設1, 大檔設2以上 )
    int percentScale = 0;

    long totalLength = 1;
    BigDecimal percent = new BigDecimal(0);
    long startTime = -1;

    String totalLengthDescription;

    private BigDecimal getPercent(long currentTotal) {
        BigDecimal p = new BigDecimal(((double) currentTotal / (double) totalLength) * ONE_HUNDRED);
        p = p.setScale(percentScale, BigDecimal.ROUND_DOWN);
        return p;
    }

    private void setCurrentTotal(long currentTotal) {
        BigDecimal percent = getPercent(currentTotal);

        if (!percent.equals(this.percent)) {
            // 計算KB/S
            double s = (System.currentTimeMillis() - startTime) / 1000;
            int kbpers = (int) ((currentTotal / KB) / s);

            // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            System.out.println("大小 :" + FileUtil.getSizeDescription(currentTotal) + " / " + totalLengthDescription + ", 百分比:" + percent + "% (" + kbpers + "KB/s)");
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
        try {
            this.totalLength = totalLength;
            this.totalLengthDescription = FileUtil.getSizeDescription(totalLength);

            // 計算percent敏銳度
            if (percentScale == null) {
                this.percentScale = caculatePercentScale(this.totalLength);
            } else {
                this.percentScale = percentScale;
            }
            
            // main process ------------------------
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
            System.out.println("下載進度100% !!");
        }
    }
}