package com.example.englishtester.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Callable;

/**
 * Created by wistronits on 2018/9/5.
 * <p>
 * https://github.com/jiangzhenjie/Java-Android/blob/master/%E5%AF%B9Android%E4%BD%BF%E7%94%A8Bitmap%E6%97%B6%E5%87%BA%E7%8E%B0OOM%E6%83%85%E5%86%B5%E7%9A%84%E4%B8%80%E4%BA%9B%E5%AE%9E%E8%B7%B5%E4%B8%8E%E6%80%9D%E8%80%83.md
 */
public class OOMHandler2 {

    public static OOMHandler2 newInstance(Context context) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d("TAG", "Max memory is " + maxMemory + "KB");
        return new OOMHandler2(context);
    }

    public OOMHandler2(Context context) {
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
    public static BitmapFactory.Options getDecodingOptions(String filepath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
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

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//for calculateInSampleSize
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(InputStream inputStream,
                                                         int reqWidth, int reqHeight, int inSampleSize) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//for calculateInSampleSize
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = inSampleSize;
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(Callable<InputStream> inputStreamGetter,
                                                         int reqWidth, int reqHeight) {
        try {
            // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//for calculateInSampleSize
            BitmapFactory.decodeStream(inputStreamGetter.call(), null, options);
            // 调用上面定义的方法计算inSampleSize值
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            // 使用获取到的inSampleSize值再次解析图片
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(inputStreamGetter.call(), null, options);
        } catch (Exception ex) {
            throw new RuntimeException("decodeSampledBitmapFromResource ERR : " + ex.getMessage(), ex);
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Callable<InputStream> inputStreamGetter,
                                                         ApplyNewBitmapScale applyBitmapScale) {
        try {
            // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//for calculateInSampleSize
            BitmapFactory.decodeStream(inputStreamGetter.call(), null, options);
            Pair<Integer, Integer> wh = applyBitmapScale.apply(Pair.of(options.outWidth, options.outHeight));
            // 调用上面定义的方法计算inSampleSize值
            options.inSampleSize = calculateInSampleSize(options, wh.getLeft(), wh.getRight());
            // 使用获取到的inSampleSize值再次解析图片
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(inputStreamGetter.call(), null, options);
        } catch (Exception ex) {
            throw new RuntimeException("decodeSampledBitmapFromResource ERR : " + ex.getMessage(), ex);
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         ApplyNewBitmapScale applyBitmapScale) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//for calculateInSampleSize
        BitmapFactory.decodeResource(res, resId, options);
        Pair<Integer, Integer> wh = applyBitmapScale.apply(Pair.of(options.outWidth, options.outHeight));
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, wh.getLeft(), wh.getRight());
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // inputStream 只能被 open 一次
    public static Bitmap decodeSampledBitmapFromResource(String filepath,
                                                         ApplyNewBitmapScale applyBitmapScale) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//for calculateInSampleSize
        BitmapFactory.decodeFile(filepath, options);
        Pair<Integer, Integer> wh = applyBitmapScale.apply(Pair.of(options.outWidth, options.outHeight));
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, wh.getLeft(), wh.getRight());
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    interface ApplyNewBitmapScale {
        Pair<Integer, Integer> apply(Pair<Integer, Integer> widthNHeight);
    }

    public static ApplyNewBitmapScale getCustomFixHeight(final int fixHeight) {
        return new OOMHandler2.ApplyNewBitmapScale() {
            @Override
            public Pair<Integer, Integer> apply(Pair<Integer, Integer> widthNHeight) {

                int width = widthNHeight.getLeft();
                int height = widthNHeight.getRight();

                int newHeight1 = fixHeight;
                float scaleHeight = ((float) newHeight1) / height;
                int newWidth = (int) (scaleHeight * width);

                return Pair.of(newWidth, fixHeight);
            }
        };
    }

    public static ApplyNewBitmapScale getCustomFixWidth(final int fixWidth) {
        return new OOMHandler2.ApplyNewBitmapScale() {
            @Override
            public Pair<Integer, Integer> apply(Pair<Integer, Integer> widthNHeight) {

                int width = widthNHeight.getLeft();
                int height = widthNHeight.getRight();

                int newWidth1 = fixWidth;
                float scaleWidth = ((float) newWidth1) / width;
                int newHeight = (int) (scaleWidth * height);

                return Pair.of(fixWidth, newHeight);
            }
        };
    }
}
