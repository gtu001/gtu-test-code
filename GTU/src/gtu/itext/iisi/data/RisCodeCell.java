/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.data;

import tw.gov.moi.ae.codetable.RisCodeComponent;

/**
 * 
 * @author tsaicf
 */
public class RisCodeCell extends CellBeanProperty {
    final String category;

    /**
     * @param propertyName
     */
    public RisCodeCell(String category, String propertyName) {
        super(propertyName);
        this.category = category;
    }

    @Override
    final public Object eval(Object dataObj) {
        Object value = super.eval(dataObj);
        if (value == null) {
            return "";
        }
        RisCodeComponent risCodeComponent = AeBean.INSTANCE.getRisCodeComponent();
        if (risCodeComponent == null) {
            return "";
        }
        return risCodeComponent.getContentByCode(this.category, value.toString());
    }

}
