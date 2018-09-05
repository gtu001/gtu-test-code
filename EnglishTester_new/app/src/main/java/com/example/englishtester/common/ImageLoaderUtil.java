package com.example.englishtester.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by wistronits on 2018/9/5.
 *
 * https://github.com/jiangzhenjie/Java-Android/blob/master/%E5%AF%B9Android%E4%BD%BF%E7%94%A8Bitmap%E6%97%B6%E5%87%BA%E7%8E%B0OOM%E6%83%85%E5%86%B5%E7%9A%84%E4%B8%80%E4%BA%9B%E5%AE%9E%E8%B7%B5%E4%B8%8E%E6%80%9D%E8%80%83.md
 *
 */
public class ImageLoaderUtil {

    public static ImageLoaderUtil newInstance(Context context) {
        return new ImageLoaderUtil(context);
    }

    public ImageLoaderUtil(Context context) {
        this.initImageLoader(context);
    }


    private void initImageLoader(Context context) {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().destroy();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).memoryCache(new WeakMemoryCache()).threadPoolSize(1)
                .build();
        ImageLoader.getInstance().init(config);
    }

//    对于显示图片
//    使用Bitmap.Config.RGB_565
//            使用resetViewBeforeLoading

    public static DisplayImageOptions getDisplayOptions(
            BitmapFactory.Options decodingOptions) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true).cacheOnDisk(false);
        if (decodingOptions != null) {
            builder = builder.decodingOptions(decodingOptions);
        }
        return builder.build();
    }

    //    对于加载图片
//            根据具体需要计算压缩率
    public static BitmapFactory.Options getDecodingOptions(String dir,
                                                           String fileName, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(dir + File.separator + fileName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return options;
    }

    //计算图片的压缩率
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
