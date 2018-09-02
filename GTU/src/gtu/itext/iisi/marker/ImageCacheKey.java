/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.marker;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.lowagie.text.Rectangle;

/**
 * 
 * @author tsaicf
 */
public class ImageCacheKey {
    /**
     * 
     * @author tsaicf
     */
    public static interface MarkerSetting {
    }

    final private float width;

    final private float height;

    final private MarkerSetting[] setting;

    final private boolean isCognos;

    public ImageCacheKey(Rectangle pageSize, MarkerSetting[] setting, boolean isCognos) {
        super();
        this.width = pageSize.getWidth();
        this.height = pageSize.getHeight();
        this.setting = setting;
        this.isCognos = isCognos;
    }

    public ImageCacheKey(Rectangle pageSize, MarkerSetting setting, boolean isCognos) {
        super();
        this.width = pageSize.getWidth();
        this.height = pageSize.getHeight();
        this.setting = new MarkerSetting[] { setting };
        this.isCognos = isCognos;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder();
        b.append(this.width);
        b.append(this.height);
        b.append(this.setting);
        b.append(this.isCognos);
        return b.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ImageCacheKey) {
            ImageCacheKey obj2 = (ImageCacheKey) obj;
            EqualsBuilder b = new EqualsBuilder();
            b.append(this.width, obj2.width);
            b.append(this.height, obj2.height);
            b.append(this.setting, obj2.setting);
            b.append(this.isCognos, obj2.isCognos);
            return b.isEquals();
        } else {
            return false;
        }
    }

    /**
     * @return the height
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * @return the width
     */
    public float getWidth() {
        return this.width;
    }
    
    /**
     * @return the setting
     */
    public MarkerSetting[] getSettings() {
        return this.setting;
    }
    

    /**
     * @return the isCognos
     */
    public boolean isCognos() {
        return this.isCognos;
    }

}
