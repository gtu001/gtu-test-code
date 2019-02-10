package com.example.gtu001.qrcodemaker.common;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gtu001 on 2018/7/8.
 */

public class ExternalStorageV2 {
    private static final String TAG = ExternalStorageV2.class.getSimpleName();

    public static final String SD_CARD = "sdCard";
    public static final String EXTERNAL_SD_CARD = "externalSdCard";

    private ExternalStorageV2() {
    }

    public static Map<String, File> getAllStorageLocations(Context context) {
        ExternalStorageV2_Handler _INST = new ExternalStorageV2_Handler(context);
        return _INST.map;
    }

    public static Pair<Integer, Integer> getExternalSdCardRange(Context context) {
        ExternalStorageV2_Handler _INST = new ExternalStorageV2_Handler(context);
        return Pair.of(_INST.min, _INST.max);
    }

    public static boolean isRangeValid(Pair<Integer, Integer> pair) {
        if (pair.getLeft() == -1 || pair.getRight() == -1) {
            return false;
        }
        return true;
    }

    private static class ExternalStorageV2_Handler {

        private Map<String, File> map = new HashMap<>();
        private int max = -1;
        private int min = -1;

        public ExternalStorageV2_Handler(Context context) {
            File[] list = ContextCompat.getExternalFilesDirs(context, null);

            File oldDeviceDir = Environment.getExternalStorageDirectory();
            if (!ArrayUtils.contains(list, oldDeviceDir)) {
                list = ArrayUtils.add(list, oldDeviceDir);
            }

            List<File> externalLst = new ArrayList<>();

            if (list != null) {
                for (File f : list) {
                    if (f == null) {
                        continue;
                    }
                    if (f.getAbsolutePath().contains("/0/Android/")) {
                        String sdCardPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf("/0/Android/"));
                        map.put(SD_CARD, new File(sdCardPath + File.separator + "0" + File.separator));
                    } else if (f.getAbsolutePath().contains("/Android/")) {
                        String externalSdPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf("/Android/"));
                        externalLst.add(new File(externalSdPath));
                    } else {
                        externalLst.add(f);
                    }
                }

                for (int ii = 0; ii < externalLst.size(); ii++) {
                    File file = externalLst.get(ii);
                    if (ii == 0) {
                        map.put(EXTERNAL_SD_CARD, file);
                    } else {
                        map.put(EXTERNAL_SD_CARD + ii, file);

                        //range start
                        if (min == -1) {
                            min = ii;
                        }

                        //range end
                        max = Math.max(max, ii);
                    }
                }
            }
        }
    }
}
