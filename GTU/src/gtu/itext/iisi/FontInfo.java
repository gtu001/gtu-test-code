/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

import java.awt.Color;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.lowagie.text.DocumentException;

public class FontInfo {
    final CHTFontFactory fontFactory;

    final int size;

    final int style;

    final Color color;

    final Color backgroundColor;

    FontInfo(CHTFontFactory fontFactory, int size, int style, Color color) {
        super();
        this.fontFactory = fontFactory;
        this.size = size <= 0 ? 10 : size;
        this.style = style <= 0 ? 0 : style;
        this.color = ObjectUtils.defaultIfNull(color, Color.BLACK);
        this.backgroundColor = null; // 無色
    }

    FontInfo(CHTFontFactory fontFactory, int size, int style, Color color, Color bColor) {
        super();
        this.fontFactory = fontFactory;
        this.size = size <= 0 ? 10 : size;
        this.style = style <= 0 ? 0 : style;
        this.color = ObjectUtils.defaultIfNull(color, Color.BLACK);
        this.backgroundColor = bColor;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FontInfo [" //
                + "  fontFactory=" + this.fontFactory //
                + ", size=" + this.size //
                + ", style=" + this.style //
                + ", color=" + this.color //
                + ", backgroundColor=" + this.backgroundColor //
                + "]";
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder();
        b.append(this.fontFactory);
        b.append(this.size);
        b.append(this.color);
        b.append(this.style);
        //b.append(this.backgroundColor);
        return b.toHashCode();
    }

    /**
     * 用於 FontFactory 判別是否已生過字型，所以背景色無需列入判斷.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj2) {
        if (this == obj2) {
            return true;
        }
        if (obj2 instanceof FontInfo) {
            FontInfo obj = (FontInfo) obj2;
            EqualsBuilder b = new EqualsBuilder();
            b.append(this.fontFactory, obj.fontFactory);
            b.append(this.size, obj.size);
            b.append(this.color, obj.color);
            b.append(this.style, obj.style);
            //b.append(this.backgroundColor, obj.backgroundColor);
            return b.isEquals();
        } else {
            return false;
        }
    }

    public RisFontSelector getFontSelector() throws DocumentException {
        return this.fontFactory.getFontSelector(this);
    }
    
    /**
     * @return the size
     */
    public int getSize() {
        return this.size;
    }
}
