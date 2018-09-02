/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.marker;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.lowagie.text.Image;

/**
 * 
 * @author tsaicf
 */
public class ImageCacheUtil {

    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ImageCacheUtil.class);

    final static RemovalListener<ImageCacheKey, Image[]> MY_LISTENER = new RemovalListener<ImageCacheKey, Image[]>() {
        @Override
        public void onRemoval(RemovalNotification<ImageCacheKey, Image[]> notification) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("remove cache : {}", Arrays.toString(notification.getKey().getSettings()));
            }
        }
    };

    static Cache<ImageCacheKey, Image[]> CACHE = CacheBuilder.newBuilder() //                                        
            .maximumSize(30) // maximum cache size
            .expireAfterAccess(5, TimeUnit.MINUTES)//
            .removalListener(MY_LISTENER) //
            .build();

    public static Image[] get(final Marker marker, final ImageCacheKey key) {
        final Callable<? extends Image[]> valueLoader = new Callable<Image[]>() {
            @Override
            public Image[] call() throws Exception {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("======================== GENERATE IMAGE FOR WATERMARK/SEAL : {}", Arrays.toString(key.getSettings()));
                }
                return marker.genImages(key);
            }
        };
        try {
            //LOGGER.debug("========================Cache SIZE Before: {}", CACHE.size());
            final Image[] images = CACHE.get(key, valueLoader);
            //LOGGER.debug("========================Cache SIZE After: {}", CACHE.size());
            return images;
        } catch (ExecutionException e) {
            return new Image[0];
        }

    }
}
