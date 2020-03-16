package gtu.mp4;

import org.apache.commons.lang3.StringUtils;

public class FfmpegHelper {
    String ffmpegExe;
    String sourceFile;
    Double volume = 1d;// 1=100%, 0.5=50% ..etc
    String targetFile;

    String fromPosition;// 00:00:00
    String toPosition;// 00:00:00

    boolean isCopy = false;

    StringBuffer sb = new StringBuffer();

    public FfmpegHelper setFfmpegExe(String ffmpegExe) {
        this.ffmpegExe = ffmpegExe;
        return this;
    }

    public FfmpegHelper setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
        return this;
    }

    public FfmpegHelper setVolume(Double volume) {
        this.volume = volume;
        return this;
    }

    public FfmpegHelper setTargetFile(String targetFile) {
        this.targetFile = targetFile;
        return this;
    }

    public FfmpegHelper setFromPosition(String fromPosition) {
        this.fromPosition = fromPosition;
        return this;
    }

    public FfmpegHelper setToPosition(String toPosition) {
        this.toPosition = toPosition;
        return this;
    }

    public FfmpegHelper setCopy(boolean isCopy) {
        this.isCopy = isCopy;
        return this;
    }

    private void applyParameter() {
        // 固定開頭 ------- ↓↓↓↓↓↓
        if (StringUtils.isNotBlank(ffmpegExe)) {
            sb.append(" \"").append(StringUtils.trimToEmpty(ffmpegExe)).append("\" ");
        }
        if (StringUtils.isNotBlank(sourceFile)) {
            sb.append(" -i \"").append(StringUtils.trimToEmpty(sourceFile)).append("\" ");
        }
        // 固定開頭 ------- ↑↑↑↑↑↑

        if (StringUtils.isNotBlank(fromPosition)) {
            sb.append(" -ss ").append(StringUtils.trimToEmpty(fromPosition)).append(" ");
        }
        if (StringUtils.isNotBlank(toPosition)) {
            sb.append(" -to ").append(StringUtils.trimToEmpty(toPosition)).append(" ");
        }

        boolean isCopyExists = false;
        if (isCopy && (StringUtils.isNotBlank(fromPosition) || StringUtils.isNotBlank(toPosition))) {
            sb.append(" -c copy ");
            isCopyExists = true;
        }
        if (!isCopyExists) {
            sb.append(String.format("  -b:a 192K -vn -af  \"volume=%s\" ", volume));
        }

        // 固定結尾 ------- ↓↓↓↓↓↓
        if (StringUtils.isNotBlank(targetFile)) {
            sb.append(" \"").append(StringUtils.trimToEmpty(targetFile)).append("\" ");
        }
        // 固定結尾 ------- ↑↑↑↑↑↑
    }

    private FfmpegHelper() {
    }

    public static FfmpegHelper newInstance() {
        return new FfmpegHelper();
    }

    public String build() {
        applyParameter();
        return sb.toString();
    }

}
