/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

/**
 * 
 * @author tsaicf
 */
public class PDFInfo {
    //================================================
    //== [Enumeration types] Block Start
    //====
    //====
    //== [Enumeration types] Block End 
    //================================================
    //== [static variables] Block Start
    //====
    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====

    final private int fileSize;

    final private int pages;

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
    public PDFInfo(int fileSize, int pages) {
        super();
        this.fileSize = fileSize;
        this.pages = pages;
    }

    //====
    //== [Constructors] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====
    //====
    //== [Static Method] Block Stop 
    //================================================
    //== [Accessor] Block Start
    //====

    public int getFileSize() {
        return this.fileSize;
    }

    /**
     * @return the pages
     */
    public int getPages() {
        return this.pages;
    }

    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Overrided Method] Block Start (Ex. toString/equals+hashCode)
    //====

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.pages + "/" + this.fileSize;
    }
    
    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====
    //####################################################################
    //## [Method] sub-block : 
    //####################################################################    
    //====
    //== [Method] Block Stop 
    //================================================

}
