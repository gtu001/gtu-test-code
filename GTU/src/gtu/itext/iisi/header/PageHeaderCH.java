/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.header;

/**
 * 
 * @author tsaicf
 */
public class PageHeaderCH extends AbatractPageHeader {

    public PageHeaderCH(int fontSize) {
        this(PagePattern.BOTH, fontSize);
    }

    public PageHeaderCH(PagePattern pattern, int fontSize) {
        super(pattern, fontSize);
        if (pattern == PagePattern.BOTH) {
            super.setPrefix("第");
            super.setConjunction("頁，共");
            super.setSuffix("頁");
        }
    }

}
