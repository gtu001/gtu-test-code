/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.table;

import java.awt.Color;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * 單一欄位外觀描述
 * 
 * @author 920111 在 2008/3/21 建立
 */
public class CellFormat implements Cloneable {

    public enum FontType {
        Default, //
        戶役政宋體, //
        戶役政楷體, //
        標楷體, //
        新細明體, //
        ; // 預設 = 新細明體,

        public static FontType lookup(String fontName) {
            for (FontType fontType : FontType.values()) {
                if (fontType.name().equals(fontName)) {
                    return fontType;
                }
            }
            return Default;
        }

    }

    //================================================
    //== [static variables] Block Start
    //====
    //####################################################################
    //## [static variables] sub-block : 對齊常數 
    //####################################################################

    public static final int ALIGN_UNDEFINED = -1;

    public static final int ALIGN_LEFT = 0;

    public static final int ALIGN_CENTER = 1;

    public static final int ALIGN_RIGHT = 2;

    public static final int ALIGN_JUSTIFIED = 3;

    public static final int ALIGN_TOP = 4;

    public static final int ALIGN_MIDDLE = 5;

    public static final int ALIGN_BOTTOM = 6;

    public static final int ALIGN_BASELINE = 7;

    public static final int ALIGN_JUSTIFIED_ALL = 8;

    //####################################################################
    //## [static variables] sub-block : 邊框常數 
    //####################################################################

    public static final int RECTANGLE_UNDEFINED = -1;

    public static final int RECTANGLE_NO_BORDER = 0;

    public static final int RECTANGLE_TOP = 1;

    public static final int RECTANGLE_BOTTOM = 2;

    public static final int RECTANGLE_LEFT = 4;

    public static final int RECTANGLE_RIGHT = 8;

    public static final int RECTANGLE_BOX = 15;

    //####################################################################
    //## [static variables] sub-block : 邊框常數 
    //####################################################################

    final public static Color C_DARK_BLUE = new Color(0, 0, 128);

    final public static Color C_DARK_GREEN = new Color(0, 128, 0);

    final public static Color C_DARK_RED = new Color(128, 0, 0);

    final public static Color C_LIGHT_GREEN = new Color(204, 255, 204);

    final public static Color C_LIGHT_TURQUOISE = new Color(204, 255, 255);

    final public static Color C_LIME = new Color(153, 204, 0);

    final public static Color C_ORANGE = new Color(255, 102, 0);

    final public static Color C_PALE_BLUE = new Color(153, 204, 255);

    final public static Color C_LIGHT_YELLOW = new Color(255, 255, 153);

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====

    protected FontType fontName = FontType.Default;

    protected int fontSize = 12;

    protected boolean fontBold = false;

    /** 對齊方式 */
    protected int alignH = ALIGN_UNDEFINED;

    /** 對齊方式 */
    protected int alignV = ALIGN_UNDEFINED;

    protected int border = RECTANGLE_BOX;

    protected float borderWidth = 0.5f;

    protected Color backgroundColor;

    protected String textFormat;

    /**
     * Padding (For PDF) 若設為 0 會使文字輸出下移觸線
     * */
    protected float paddingV = 0f;

    protected float paddingR = 3f;

    protected float paddingL = 3f;

    /** 單位: CM. */
    protected Float minimumHeight = null;

    //====
    //== [instance variables] Block Stop 
    //================================================
    //== [static Constructor] Block Start
    //====
    //====
    //== [static Constructor] Block Stop 
    //================================================
    //== [Constructors] Block Start (含init method)
    //====
    //====
    //== [Constructors] Block Stop 
    //================================================
    //== [Overrided Method] Block Start (toString/equals+hashCode)
    //====

    /**
     * 複製資料.
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("取得CellFormat定義失敗!");
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj2) {

        CellFormat obj = (CellFormat) obj2;
        EqualsBuilder b = new EqualsBuilder();
        b.append(this.fontName, obj.fontName);
        b.append(this.fontSize, obj.fontSize);
        b.append(this.isFontBold(), obj.isFontBold());
        b.append(this.alignH, obj.alignH);
        b.append(this.alignV, obj.alignV);
        b.append(this.border, obj.border);
        b.append(this.borderWidth, obj.borderWidth);
        b.append(this.backgroundColor, obj.backgroundColor);
        b.append(this.textFormat, obj.textFormat);
        b.append(this.paddingV, obj.paddingV);
        b.append(this.paddingR, obj.paddingR);
        b.append(this.paddingL, obj.paddingL);

        return b.isEquals();

    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        //return 1;
        //PerformanceTester.getInstance().startPoint("H1");
        HashCodeBuilder b = new HashCodeBuilder();
        b.append(this.fontName);
        b.append(this.fontSize);
        b.append(this.isFontBold());
        b.append(this.alignH);
        b.append(this.alignV);
        b.append(this.border);
        b.append(this.borderWidth);
        b.append(this.backgroundColor);
        b.append(this.textFormat);
        b.append(this.paddingV);
        b.append(this.paddingR);
        b.append(this.paddingL);
        final int reflectionHashCode = b.toHashCode();
        //PerformanceTester.getInstance().stopPoint("H1");
        return reflectionHashCode;

    }

    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Accessor] Block Start
    //====

    /**
     * @return 傳回 alignH。
     */
    public int getAlignH() {
        return this.alignH;
    }

    /**
     * @param alignH 要設定的 alignH。
     */
    public CellFormat setAlignH(int alignH) {
        this.alignH = alignH;
        return this;
    }

    /**
     * @return 傳回 alignV。
     */
    public int getAlignV() {
        return this.alignV;
    }

    /**
     * @param alignV 要設定的 alignV。
     */
    public CellFormat setAlignV(int alignV) {
        this.alignV = alignV;
        return this;
    }

    /**
     * @return 傳回 border。
     */
    public int getBorder() {
        return this.border;
    }

    /**
     * @param border 要設定的 border。
     */
    public CellFormat setBorder(int border) {
        this.border = border;
        return this;
    }

    /**
     * @return 傳回 borderWidth。
     */
    public float getBorderWidth() {
        return this.borderWidth;
    }

    /**
     * @param borderWidth 要設定的 borderWidth。
     */
    public CellFormat setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    /**
     * @return 傳回 backgroundColor。
     */
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * @param backgroundColor 要設定的 backgroundColor。
     */
    public CellFormat setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * @return 傳回 minimumHeight。
     */
    public final Float getMinimumHeight() {
        return this.minimumHeight;
    }

    /**
     * @param minimumHeight 要設定的 minimumHeight。
     */
    public final CellFormat setMinimumHeight(Float minimumHeight) {
        this.minimumHeight = minimumHeight;
        return this;
    }

    /**
     * @return 傳回 fontSize。
     */
    public int getFontSize() {
        return this.fontSize;
    }

    /**
     * @return 傳回 fontSize。
     */
    public int getFontSizeIfNotPositive(int defaultValue) {
        if (this.fontSize > 0) {
            return this.fontSize;
        } else {
            return defaultValue;
        }
    }

    /**
     * @param fontSize 要設定的 fontSize。
     */
    public CellFormat setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * @return 傳回 fontBold。
     */
    public boolean isFontBold() {
        return this.fontBold;
    }

    /**
     * @param fontBold 要設定的 fontBold。
     */
    public CellFormat setFontBold(boolean fontBold) {
        this.fontBold = fontBold;
        return this;
    }

    /**
     * @return 傳回 textFormat。
     */
    public String getTextFormat() {
        return this.textFormat;
    }

    /**
     * @param textFormat 要設定的 textFormat。
     */
    public CellFormat setTextFormat(String textFormat) {
        this.textFormat = textFormat;
        return this;
    }

    /**
     * @return 傳回 fontName。
     */
    public final FontType getFontName() {
        return this.fontName;
    }

    /**
     * @param fontName 要設定的 fontName。
     */
    public final CellFormat setFontName(String fontName) {
        this.fontName = FontType.lookup(fontName);
        return this;
    }

    /**
     * @param fontName 要設定的 fontName。
     */
    public final CellFormat setFontName(FontType fontName) {
        this.fontName = fontName;
        return this;
    }

    /**
     * @return 傳回 padding。
     */
    public final float getPaddingV() {
        return this.paddingV;
    }

    /**
     * @return the paddingL
     */
    public float getPaddingL() {
        return this.paddingL;
    }

    /**
     * @return the paddingR
     */
    public float getPaddingR() {
        return this.paddingR;
    }

    /**
     * @param paddingL the paddingL to set
     */
    public CellFormat setPaddingL(float paddingL) {
        this.paddingL = paddingL;
        return this;
    }

    /**
     * @param paddingR the paddingR to set
     */
    public CellFormat setPaddingH(float paddingH) {
        this.paddingR = paddingH;
        this.paddingL = paddingH;
        return this;
    }

    /**
     * @param paddingR the paddingR to set
     */
    public CellFormat setPaddingR(float paddingR) {
        this.paddingR = paddingR;
        return this;
    }

    /**
     * @param paddingV the paddingV to set
     */
    public CellFormat setPaddingV(float paddingV) {
        this.paddingV = paddingV;
        return this;
    }

    /**
     * @param padding 要設定的 padding。
     */
    public final CellFormat setPadding(float padding) {
        this.paddingV = padding;
        this.paddingR = padding;
        this.paddingL = padding;
        return this;
    }

    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====
    //====
    //== [Static Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====
    //####################################################################
    //## [Method] sub-block : XXXX(可依功能區分) 
    //####################################################################
    //====
    //== [Method] Block Stop 
    //================================================
    //== [Main Method] Block Start
    //====
    //    public static void main(String[] args) throws CloneNotSupportedException {
    //        CellFormat cf = new CellFormat();
    //        System.out.println(cf);
    //        System.out.println(cf.clone());
    //        System.out.println(cf);
    //    }
    //====
    //== [Main Method] Block Stop 
    //================================================

}
