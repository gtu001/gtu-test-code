package com.example.englishtester.common.html.image;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gtu001 on 2018/8/9.
 */

public abstract class ImageLoaderCandidateAbstract {

    protected boolean isGif(String srcData) {
        if (StringUtils.isNotBlank(srcData) && srcData.matches("(?i).*\\.gif")) {
            return true;
        }
        return false;
    }
}
