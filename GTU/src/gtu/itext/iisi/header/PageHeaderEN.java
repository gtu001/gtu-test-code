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
public class PageHeaderEN extends AbatractPageHeader {

    public PageHeaderEN(int fontSize) {
        this(PagePattern.BOTH, fontSize);
    }

    public PageHeaderEN(PagePattern pattern, int fontSize) {
        super(pattern, fontSize);
        if (pattern == PagePattern.BOTH) {
            super.setPrefix("Page ");
            super.setConjunction(" of ");
        }
    }
}
