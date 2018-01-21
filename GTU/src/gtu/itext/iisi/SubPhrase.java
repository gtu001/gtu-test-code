/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

public class SubPhrase {
    final FontInfo fontInfo;

    final String text;

    public SubPhrase(FontInfo fontInfo, String text) {
        super();
        this.fontInfo = fontInfo;
        this.text = text;
    }

    /**
     * @return the fontInfo
     */
    public FontInfo getFontInfo() {
        return this.fontInfo;
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

}
