/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

import java.util.Collection;

/**
 * 
 * @author tsaicf
 */
public enum FontStyle {
    NORMAL(0), //
    BOLD(1), //
    ITALIC(2), //
    BOLDITALIC(3), //    
    UNDERLINE(4), //
    STRIKETHRU(8), //
    ;
    final private int code;

    private FontStyle(int code) {
        this.code = code;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return this.code;
    }

    static public int toITextCode(Collection<FontStyle> styles) {
        int code = 0;
        for (FontStyle fontStyle : styles) {
            code += fontStyle.code;
        }
        return code;
    }
}
