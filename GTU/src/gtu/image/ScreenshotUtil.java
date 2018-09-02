package gtu.image;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import gtu.file.FileUtil;

public class ScreenshotUtil {

    private Robot robot;
    private Rectangle fullScreenRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    private File parentDir;
    private String prefix;
    private String dateFormat;
    private PicType picType = PicType.JPG;
    private int serialNo = 0;
    
    private ActionListener saveSuccessAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };
    private ActionListener saveFailedAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            throw new RuntimeException((Exception)e.getSource());
        }
    };

    public ScreenshotUtil(String prefix, String dateFormat, File parentDir) {
        try {
            this.dateFormat = dateFormat;
            this.prefix = prefix;
            if (parentDir == null) {
                parentDir = FileUtil.DESKTOP_DIR;
            }
            this.parentDir = parentDir;
            if (!this.parentDir.exists()) {
                this.parentDir.mkdirs();
            }
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private File getFormatFile() {
        if (!this.parentDir.exists()) {
            this.parentDir.mkdirs();
        }
        serialNo++;
        String prefixData = prefix != null ? prefix + "_" : "";
        String dateData = dateFormat != null ? DateFormatUtils.format(System.currentTimeMillis(), dateFormat) + "_" : "";
        String name = prefixData + dateData + StringUtils.leftPad(String.valueOf(serialNo), 4, '0') + "." + picType.subName;
        return new File(parentDir, name);
    }
    
    public void setSaveSuccessAction(ActionListener saveSuccessAction) {
        this.saveSuccessAction = saveSuccessAction;
    }
    
    public void setSaveFailedAction(ActionListener saveFailedAction) {
        this.saveFailedAction = saveFailedAction;
    }

    public void setPicType(PicType picType) {
        this.picType = picType;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Rectangle getFullScreenRectangle() {
        return fullScreenRectangle;
    }

    public void setFullScreenRectangle(Rectangle fullScreenRectangle) {
        this.fullScreenRectangle = fullScreenRectangle;
    }

    public File getParentDir() {
        return parentDir;
    }

    public void setParentDir(File parentDir) {
        this.parentDir = parentDir;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public PicType getPicType() {
        return picType;
    }

    public ActionListener getSaveSuccessAction() {
        return saveSuccessAction;
    }

    public ActionListener getSaveFailedAction() {
        return saveFailedAction;
    }

    public void prtsc() {
        try {
            BufferedImage capture = robot.createScreenCapture(fullScreenRectangle);
            File file = getFormatFile();
            ImageIO.write(capture, picType.name(), file);
            saveSuccessAction.actionPerformed(new ActionEvent(file, 0, "saveSuccess"));
        } catch (Exception ex) {
            saveFailedAction.actionPerformed(new ActionEvent(new Object[] {ex, this}, 0, "saveFailed"));
        }
    }
    
    public enum PicType {
        BMP("bmp"),//
        JPG("jpg"),//
        ;
        final String subName;
        PicType(String subName) {
            this.subName = subName;
        }
    }
}
