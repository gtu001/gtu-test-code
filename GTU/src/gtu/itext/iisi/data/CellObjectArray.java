/*
 * Copyright (c) 2009. 資拓科技. All right reserved.
 */
package gtu.itext.iisi.data;

/**
 * 當原始資料為陣列形式
 * 
 * @author 920111 在 2010/6/4 建立
 */
public class CellObjectArray implements CellDataSource {

    //================================================
    //== [static variables] Block Start
    //====
    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====
    /**
     * 
     */
    private int index;

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
    /**
     * 
     */
    public CellObjectArray(int index) {
        this.index = index;
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
    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Overrided Method] Block Start (Ex. toString/equals+hashCode)
    //====
    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====
    //####################################################################
    //## [Method] sub-block : 
    //####################################################################

    @Override
    public void reset() {
    };

    /**
     * 使用index取值 錯誤就回傳null
     */
    @Override
    public Object eval(Object dataObj) {
        return ((Object[]) dataObj)[this.index];
    }

    //====
    //== [Method] Block Stop 
    //================================================
}
