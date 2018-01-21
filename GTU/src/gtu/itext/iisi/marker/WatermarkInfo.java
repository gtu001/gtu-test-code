package gtu.itext.iisi.marker;

import gtu.itext.iisi.marker.ImageCacheKey.MarkerSetting;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.math.NumberUtils;

/***
 * 浮水印相關資訊
 * 
 * @author Sandy Chiu
 * 
 */
public class WatermarkInfo implements MarkerSetting {
    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WatermarkInfo.class);

    /** 圖片 :1 字 :2 */
    final private int typeImg;

    /** 浮水印上的字 */
    private String watermarkString;

    /** 浮水印圖片存放路徑 */
    private URL watermarkImg;

    /** 擺放位置起始點 */
    private boolean isCenterTranslate;

    /** 擺放位置X、Y軸 */
    private int x, y;

    /** 圖片旋轉角度 介於0~360 */
    private int rotateAngle;

    /** 圖片放大縮小倍數 */
    private float scaleRatio;

    /** 圖片濃淡程度 range between 0-255, 0 is 透明 , 255 is 不透明 */
    private int alpha;// 

    /** 浮水印上字體大小 */
    private int fontSize;

    /** 字型P：正常，即宋體＋實心字＋沒底線、U：底線、O：中空字、S：宋體、K：楷體 */
    private String fontType;

    /** 列印順序 0：字在浮水印之上，1：浮水印在字之上，該值可不設定，預設為字在浮水印上 */
    private String printOrder = "0";

    /** 字體顏色 */
    private String color;

    public enum Item {
        alpha, color, fontSize, fontType, isCenterTranslate, printOrder, rotateAngle, scaleRatio, typeImg, watermarkImg, watermarkString, x, y,
    }

    public static Map<Item, String> transToMap(String[] values) {

        final Map<Item, String> map = new EnumMap<Item, String>(Item.class);
        map.put(Item.typeImg, values[0]); // 1圖/2字
        if ("1".equals(values[0])) {
            map.put(Item.watermarkImg, values[1]);
            map.put(Item.isCenterTranslate, values[2]);
            map.put(Item.x, values[3]);
            map.put(Item.y, values[4]);
            map.put(Item.rotateAngle, values[5]);
            map.put(Item.scaleRatio, values[6]);
            map.put(Item.alpha, values[7]);
            if (values.length == 9) {
                map.put(Item.printOrder, values[8]);
            } else if (values.length == 8) {
            }
        } else {
            map.put(Item.watermarkString, values[1]);
            map.put(Item.isCenterTranslate, values[2]);
            map.put(Item.x, values[3]);
            map.put(Item.y, values[4]);
            map.put(Item.rotateAngle, values[5]);
            map.put(Item.scaleRatio, values[6]);
            map.put(Item.alpha, values[7]);
            if (values.length == 12) {
                map.put(Item.printOrder, values[8]);
                map.put(Item.fontSize, values[9]);
                map.put(Item.fontType, values[10]);
                map.put(Item.color, values[11]);
            } else if (values.length == 11) {

                map.put(Item.fontSize, values[8]);
                map.put(Item.fontType, values[9]);
                map.put(Item.color, values[10]);
            }
        }
        return map;

    }

    public static WatermarkInfo create(File templateFolder, String ssId, String[] values) throws MalformedURLException {

        final int typeImg = NumberUtils.toInt(values[0]); // 1圖/2字
        switch (typeImg) {
            case 1: {
                final WatermarkInfo info = new WatermarkInfo(typeImg);
                info.watermarkImg = new File(templateFolder, values[1]).toURI().toURL();
                info.isCenterTranslate = "1".equals(values[2]) ? true : false;
                info.x = NumberUtils.toInt(values[3]);
                info.y = NumberUtils.toInt(values[4]);
                info.rotateAngle = NumberUtils.toInt(values[5]);
                info.scaleRatio = NumberUtils.toFloat(values[6]);
                info.alpha = NumberUtils.toInt(values[7]);
                if (values.length == 9) {
                    info.printOrder = values[8].trim();
                    return info;
                } else if (values.length == 8) {
                    return info;
                }
                break;
            }
            case 2: {
                final WatermarkInfo info = new WatermarkInfo(typeImg);
                info.watermarkString = values[1];
                info.isCenterTranslate = values[2].equals("1") ? true : false;
                info.x = NumberUtils.toInt(values[3]);
                info.y = NumberUtils.toInt(values[4]);
                info.rotateAngle = NumberUtils.toInt(values[5]);
                info.scaleRatio = NumberUtils.toFloat(values[6]);
                info.alpha = NumberUtils.toInt(values[7]);
                if (values.length == 12) {
                    info.printOrder = values[8];
                    info.fontSize = NumberUtils.toInt(values[9]);
                    info.fontType = values[10];
                    info.color = values[11];
                    return info;
                } else if (values.length == 11) {

                    info.fontSize = NumberUtils.toInt(values[8]);
                    info.fontType = values[9];
                    info.color = values[10];
                    return info;
                }
                break;
            }
        }
        LOGGER.error("浮水印範本內容格式錯誤，請檢查{}.txt內容是否正確", ssId);
        return null;
    }

    /***
     * 圖片初始
     * 
     * @param typeImg_o
     * @param imgPath
     * @param x_o
     * @param y_o
     * @param rotateAngle_o
     * @param scaleRatio_o
     * @param alpha_o
     * @param isCenterTranslate_o
     */
    private WatermarkInfo(final int typeImg_o, final URL imgPath, final boolean isCenterTranslate_o, final int x_o, final int y_o,
            final int rotateAngle_o, final float scaleRatio_o, final int alpha_o) {
        this.typeImg = typeImg_o;
        this.watermarkImg = imgPath;
        this.x = x_o;
        this.y = y_o;
        this.rotateAngle = rotateAngle_o;
        this.scaleRatio = scaleRatio_o;
        this.alpha = alpha_o;
        this.isCenterTranslate = isCenterTranslate_o;
    }

    /***
     * 圖片初始
     * 
     * @param typeImg_o
     * @param imgPath
     * @param x_o
     * @param y_o
     * @param rotateAngle_o
     * @param scaleRatio_o
     * @param alpha_o
     * @param printOrder_o
     * @param isCenterTranslate_o
     */
    private WatermarkInfo(final int typeImg_o) {
        this.typeImg = typeImg_o;
    }

    /***
     * 文字初始
     * 
     * @param typeImg_o
     * @param word
     * @param x_o
     * @param y_o
     * @param rotateAngle_o
     * @param scaleRatio_o
     * @param alpha_o
     * @param fontSize_o
     * @param fontType_o
     * @param color_o
     * @param isCenterTranslate_o
     */
    private WatermarkInfo(final int typeImg_o, final String word, final boolean isCenterTranslate_o, final int x_o, final int y_o,
            final int rotateAngle_o, final float scaleRatio_o, final int alpha_o, final int fontSize_o, final String fontType_o//
            , final String color_o) {
        this.typeImg = typeImg_o;
        this.watermarkString = word;
        this.x = x_o;
        this.y = y_o;
        this.rotateAngle = rotateAngle_o;
        this.scaleRatio = scaleRatio_o;
        this.alpha = alpha_o;
        this.fontSize = fontSize_o;
        this.fontType = fontType_o;
        this.color = color_o;
        this.isCenterTranslate = isCenterTranslate_o;
    }

    /***
     * 圖片初始
     * 
     * @param typeImg_o
     * @param imgPath
     * @param x_o
     * @param y_o
     * @param rotateAngle_o
     * @param scaleRatio_o
     * @param alpha_o
     * @param printOrder_o
     * @param isCenterTranslate_o
     */
    private WatermarkInfo(final int typeImg_o, final URL imgPath, final boolean isCenterTranslate_o, final int x_o, final int y_o,
            final int rotateAngle_o, final float scaleRatio_o, final int alpha_o, final String printOrder_o) {
        this.typeImg = typeImg_o;
        this.watermarkImg = imgPath;
        this.x = x_o;
        this.y = y_o;
        this.rotateAngle = rotateAngle_o;
        this.scaleRatio = scaleRatio_o;
        this.alpha = alpha_o;
        this.printOrder = printOrder_o;
        this.isCenterTranslate = isCenterTranslate_o;
    }

    /***
     * 文字初始
     * 
     * @param typeImg_o
     * @param word
     * @param x_o
     * @param y_o
     * @param rotateAngle_o
     * @param scaleRatio_o
     * @param alpha_o
     * @param fontSize_o
     * @param fontType_o
     * @param printOrder_o
     * @param color_o
     * @param isCenterTranslate_o
     */
    private WatermarkInfo(final int typeImg_o, final String word, final boolean isCenterTranslate_o, final int x_o, final int y_o,
            final int rotateAngle_o, final float scaleRatio_o, final int alpha_o, final int fontSize_o, final String fontType_o,
            final String printOrder_o, final String color_o) {
        this.typeImg = typeImg_o;
        this.watermarkString = word;
        this.x = x_o;
        this.y = y_o;
        this.rotateAngle = rotateAngle_o;
        this.scaleRatio = scaleRatio_o;
        this.alpha = alpha_o;
        this.fontSize = fontSize_o;
        this.fontType = fontType_o;
        this.printOrder = printOrder_o;
        this.color = color_o;
        this.isCenterTranslate = isCenterTranslate_o;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public String getWatermarkString() {
        return this.watermarkString;
    }

    public URL getWatermarkImg() {
        return this.watermarkImg;
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

    public String getPrintOrder() {
        return this.printOrder;
    }

    public String getColor() {
        return this.color;
    }

    public boolean isCenterTranslate() {
        return this.isCenterTranslate;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder();
        b.append(this.typeImg);
        b.append(this.watermarkString);
        b.append(this.watermarkImg);
        b.append(this.isCenterTranslate);
        b.append(this.x);
        b.append(this.y);
        b.append(this.rotateAngle);
        b.append(this.scaleRatio);
        b.append(this.alpha);
        b.append(this.fontSize);
        b.append(this.fontType);
        b.append(this.printOrder);
        b.append(this.color);
        return b.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WatermarkInfo) {
            WatermarkInfo obj2 = (WatermarkInfo) obj;
            EqualsBuilder b = new EqualsBuilder();
            b.append(this.typeImg, obj2.typeImg);
            b.append(this.watermarkString, obj2.watermarkString);
            b.append(this.watermarkImg, obj2.watermarkImg);
            b.append(this.isCenterTranslate, obj2.isCenterTranslate);
            b.append(this.x, obj2.x);
            b.append(this.y, obj2.y);
            b.append(this.rotateAngle, obj2.rotateAngle);
            b.append(this.scaleRatio, obj2.scaleRatio);
            b.append(this.alpha, obj2.alpha);
            b.append(this.fontSize, obj2.fontSize);
            b.append(this.fontType, obj2.fontType);
            b.append(this.printOrder, obj2.printOrder);
            b.append(this.color, obj2.color);
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
        return "WatermarkInfo [" //
                + "  typeImg=" + this.typeImg //
                + ", watermarkString=" + this.watermarkString //
                + ", watermarkImg=" + this.watermarkImg //
                + ", isCenterTranslate=" + this.isCenterTranslate //
                + ", x=" + this.x //
                + ", y=" + this.y //
                + ", rotateAngle=" + this.rotateAngle //
                + ", scaleRatio=" + this.scaleRatio //
                + ", alpha=" + this.alpha //
                + ", fontSize=" + this.fontSize //
                + ", fontType=" + this.fontType //
                + ", printOrder=" + this.printOrder //
                + ", color=" + this.color //
                + "]";
    }

}
