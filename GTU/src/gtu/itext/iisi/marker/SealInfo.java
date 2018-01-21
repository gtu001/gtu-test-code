package gtu.itext.iisi.marker;

import gtu.itext.iisi.marker.ImageCacheKey.MarkerSetting;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 騎縫章相關資訊
 * 
 * @author Sandy Chiu
 * 
 */
public class SealInfo implements MarkerSetting{

    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SealInfo.class);

    /* 圖片:true 字:false */
    final private int typeImg;

    /* 騎縫章上的字 */
    private String seamsealString;

    /* 騎縫章圖片存放路徑 */
    private URL seamsealImg;

    /* 圖片旋轉角度 介於0~360 */
    private int rotateAngle;

    /* 圖片放大縮小倍數 */
    private float scaleRatio;

    /* 圖片濃淡程度 */
    private int alpha;// range between 0-255, 0 is 透明 , 255 is 不透明

    /* 浮水印上字體大小 */
    private int fontSize;

    /* 字型 P：一般；B：粗體；I：斜體；U：底線 */
    private String fontType;

    /* 騎縫章裝訂位置 E：右邊；W：左邊；S：下面；N：上面 */
    private String binding;

    /* 裝訂否 1=是 0=否 */
    private boolean isBinding;

    /* 騎縫章擺放方式 Center：罝中；Random：隨機 */
    private int position;

    /* 騎縫章切割比率 Center：罝中；Random：隨機 */
    private int cutting;

    /* 蓋印方式，共有三種模式，1=二頁蓋一個章，但若只有一頁蓋半個章；2=二頁蓋一個章，但若只有一頁不蓋章；3=每一頁都蓋，且頭尾相接 */
    private PositionType positionType;

    public static SealInfo create(File templateFolder, String ssId, String[] values) throws MalformedURLException {
        final int typeImg = NumberUtils.toInt(values[0]); // 1圖/2字
        switch (typeImg) {
            case 1: {
                if (values.length == 10) {
                    //final String templateDir = globleSharePath + File.separator + "report" + File.separator + "seamseal" + File.separator + ssId;
                    final File imgFile = new File(templateFolder, values[1]);
                    final URL watermarkImg = imgFile.toURI().toURL(); // new URL("file:" + templateDir + File.separator + values[1]);
                    final SealInfo si = new SealInfo(typeImg);
                    si.seamsealImg = watermarkImg;
                    si.rotateAngle = NumberUtils.toInt(values[2]);
                    si.scaleRatio = NumberUtils.toFloat(values[3]);
                    si.alpha = NumberUtils.toInt(values[4]);
                    si.binding = values[5];
                    si.position = "Random".equalsIgnoreCase(values[6]) ? RandomUtils.nextInt(6) : 3;
                    si.positionType = PositionType.lookup(values[7]);
                    si.isBinding = values[8].equals("1") ? true : false;
                    si.cutting = "Random".equalsIgnoreCase(values[9]) ? RandomUtils.nextInt(6) : 3; //= values[9];
                    return si;
                }
                break;
            }
            case 2: {
                if (values.length == 9) {
                    final String watermarkString = values[1];
                    final int rotateAngle = NumberUtils.toInt(values[2]);
                    final float scaleRatio = NumberUtils.toFloat(values[3]);
                    final int alpha = NumberUtils.toInt(values[4]);
                    final int fontSize = NumberUtils.toInt(values[5]);
                    SealInfo si = new SealInfo(typeImg);
                    si.seamsealString = watermarkString;//                         
                    si.rotateAngle = rotateAngle;//                             
                    si.scaleRatio = scaleRatio;//                              
                    si.alpha = alpha;//                                   
                    si.fontSize = fontSize;//                                
                    si.fontType = values[6];//                               
                    si.positionType = PositionType.lookup(values[7]);//          
                    si.isBinding = values[8].equals("1") ? true : false;//    
                    return si;
                }
                break;
            }
        }
        LOGGER.error("騎縫章範本內容格式錯誤，請檢查{}.txt內容是否正確 : {}", ssId, Arrays.toString(values));
        return null;

    }

    public SealInfo(int typeImg) {
        this.typeImg = typeImg;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public int getRotateAngle() {
        return this.rotateAngle;
    }

    public float getScaleRatio() {
        return this.scaleRatio;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public String getFontType() {
        return this.fontType;
    }

    public int getTypeImg() {
        return this.typeImg;
    }

    public String getBinding() {
        return this.binding;
    }

    public int getPosition() {
        return this.position;
    }

    public int getCutting() {
        return this.cutting;
    }

    public String getSeamsealString() {
        return this.seamsealString;
    }

    public URL getSeamsealImg() {
        return this.seamsealImg;
    }

    public PositionType getPositionType() {
        return this.positionType;
    }

    public boolean isBinding() {
        return this.isBinding;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder();
        b.append(this.typeImg);
        b.append(this.seamsealString);
        b.append(this.seamsealImg);
        b.append(this.rotateAngle);
        b.append(this.scaleRatio);
        b.append(this.alpha);
        b.append(this.fontSize);
        b.append(this.fontType);
        b.append(this.binding);
        b.append(this.isBinding);
        b.append(this.position);
        b.append(this.cutting);
        b.append(this.positionType);
        return b.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SealInfo) {
            SealInfo obj2 = (SealInfo) obj;
            EqualsBuilder b = new EqualsBuilder();
            b.append(this.typeImg, obj2.typeImg);
            b.append(this.seamsealString, obj2.seamsealString);
            b.append(this.seamsealImg, obj2.seamsealImg);
            b.append(this.rotateAngle, obj2.rotateAngle);
            b.append(this.scaleRatio, obj2.scaleRatio);
            b.append(this.alpha, obj2.alpha);
            b.append(this.fontSize, obj2.fontSize);
            b.append(this.fontType, obj2.fontType);
            b.append(this.binding, obj2.binding);
            b.append(this.isBinding, obj2.isBinding);
            b.append(this.position, obj2.position);
            b.append(this.cutting, obj2.cutting);
            b.append(this.positionType, obj2.positionType);
            return b.isEquals();
        } else {
            return false;
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SealInfo [" //
                + "  typeImg=" + this.typeImg //
                + ", seamsealString=" + this.seamsealString //
                + ", seamsealImg=" + this.seamsealImg //
                + ", rotateAngle=" + this.rotateAngle //
                + ", scaleRatio=" + this.scaleRatio //
                + ", alpha=" + this.alpha //
                + ", fontSize=" + this.fontSize //
                + ", fontType=" + this.fontType //
                + ", binding=" + this.binding //
                + ", isBinding=" + this.isBinding //
                + ", position=" + this.position //
                + ", cutting=" + this.cutting //
                + ", positionType=" + this.positionType //
                + "]";
    }

    public static void main(String[] args) {
        AeObjectUtil.genEqualsAndHashCode(SealInfo.class);
        AeObjectUtil.genEqualsAndHashCode(WatermarkInfo.class);
    }
}
