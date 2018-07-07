package com.example.englishtester.common;

import android.content.Context;
import android.support.v4.content.ContextCompat;

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

    public static Map<String, File> getAllStorageLocations(Context context) {
        Map<String, File> map = new HashMap<>();
        File[] list = ContextCompat.getExternalFilesDirs(context, null);

        List<File> externalLst = new ArrayList<>();

        if (list != null) {
            for (File f : list) {
                if (f.getAbsolutePath().contains("/0/Android/")) {
                    String sdCardPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf("/0/Android/"));
                    map.put(SD_CARD, new File(sdCardPath + File.separator + "0" + File.separator));
                } else if (f.getAbsolutePath().contains("/Android/")) {
                    String externalSdPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf("/Android/"));
                    externalLst.add(new File(externalSdPath));
                }
            }

            for (int ii = 0; ii < externalLst.size(); ii++) {
                File file = externalLst.get(ii);
                if (ii == 0) {
                    map.put(EXTERNAL_SD_CARD, file);
                } else {
                    map.put(EXTERNAL_SD_CARD + ii, file);
                }
            }
        }
        return map;
    }
}
