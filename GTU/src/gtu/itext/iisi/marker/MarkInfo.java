/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi.marker;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import tw.gov.moi.common.SystemConfig;

/**
 * 在使用 PDFDocumentGenerator 時，定義 浮水印/騎縫章樣式.
 */
public class MarkInfo {

    /** Logger Object. */
    final private static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MarkInfo.class);

    public enum Type {
        seamseal("seamseal") {
            @Override
            protected List<Marker> toMarker(File folder, String ssId, List<String[]> settings) throws MalformedURLException {
                List<Marker> markers = new ArrayList<Marker>(settings.size());
                for (String[] values : settings) {
                    final SealInfo info = SealInfo.create(folder, ssId, values);
                    final Marker marker = new SealMarker(info);
                    if (marker != null) {
                        markers.add(marker);
                    }
                }
                return markers;
            }
        },
        seamsealOn1Page("seamseal") {
            @Override
            protected List<Marker> toMarker(File folder, String ssId, List<String[]> settings) throws MalformedURLException {

                List<Marker> markers = new ArrayList<Marker>(settings.size());
                for (String[] values : settings) {
                    final SealInfo info = SealInfo.create(folder, ssId, values);
                    final Marker marker = new SealMarkerOnSinglePage(info);
                    if (marker != null) {
                        markers.add(marker);
                    }
                }
                return markers;
            }
        },
        watermark("watermark") {
            @Override
            protected List<Marker> toMarker(File folder, String ssId, List<String[]> settings) throws MalformedURLException {
                List<WatermarkInfo> infos = new ArrayList<WatermarkInfo>(settings.size());
                for (String[] values : settings) {
                    final WatermarkInfo info = WatermarkInfo.create(folder, ssId, values);
                    if (info != null) {
                        infos.add(info);
                    }
                }
                if (!infos.isEmpty()) {
                    return Arrays.<Marker> asList(new WatermarkMarker(infos));
                } else {
                    return Collections.emptyList();
                }
            }
        };

        final String catelog;

        private Type(String catelog) {
            this.catelog = catelog;
        }

        /**
         * @param systemConfig
         * @param markInfo
         * @return
         */
        public List<Marker> toMarker(SystemConfig systemConfig, MarkInfo markInfo) {

            try {
                final String sharePath = systemConfig.getSharePath();
                final String templateDir = sharePath + File.separator + "report" + File.separator + this.catelog;
                final File folder = new File(templateDir);
                final File templateFile = new File(folder, markInfo.id + File.separator + markInfo.id + ".txt");
                final List<String> readLines = FileUtils.readLines(templateFile);

                //final List<Marker> result = new ArrayList<Marker>();

                final List<String[]> settings = new ArrayList<String[]>(readLines.size());

                for (String strLine : readLines) {
                    final String[] values = TemplateUtils.parseLine(strLine, markInfo.param, markInfo.id);
                    if (values != null && values.length > 0) {
                        settings.add(values);
                    }
                }
                return toMarker(folder, markInfo.id, settings);

                //                for (String strLine : readLines) {
                //                    final String[] values = TemplateUtils.parseLine(strLine, markInfo.param, markInfo.id);
                //                    if (values == null || values.length == 0) {
                //                        continue;
                //                    }
                //                    LOGGER.debug("values:{}", strLine);
                //                    final Marker marker = toMarker(folder, markInfo.id, values);
                //                    if (marker != null) {
                //                        result.add(marker);
                //                    }
                //                }
                //return result;
            } catch (MalformedURLException e) {
                LOGGER.error("", e);
                return Collections.emptyList();
            } catch (IOException e) {
                LOGGER.error("", e);
                return Collections.emptyList();
            }
        }

        protected abstract List<Marker> toMarker(File folder, String ssId, List<String[]> values) throws MalformedURLException;

    }

    final Type type;

    final String id;

    final Map<String, String> param;

    private MarkInfo(Type type, String id, Map<String, String> param) {
        super();
        this.type = type;
        this.id = id;
        this.param = param;
    }

    public static MarkInfo watermark(String id) {
        return new MarkInfo(Type.watermark, id, Collections.<String, String> emptyMap());
    }

    public static MarkInfo watermark(String id, Map<String, String> param) {
        return new MarkInfo(Type.watermark, id, param);
    }

    public static MarkInfo seal(String id) {
        return new MarkInfo(Type.seamseal, id, Collections.<String, String> emptyMap());
    }

    public static MarkInfo seal(String id, Map<String, String> param) {
        return new MarkInfo(Type.seamseal, id, param);
    }

    public static MarkInfo sealOnOnePage(String id) {
        return new MarkInfo(Type.seamsealOn1Page, id, Collections.<String, String> emptyMap());
    }

    public static MarkInfo sealOnOnePage(String id, Map<String, String> param) {
        return new MarkInfo(Type.seamsealOn1Page, id, param);
    }

    /**
     * @param systemConfig
     * @param markInfos
     * @return
     */
    public static List<Marker> toMarkers(SystemConfig systemConfig, List<MarkInfo> markInfos) {
        final List<Marker> result = new ArrayList<Marker>();
        if (markInfos != null) {
            for (MarkInfo markInfo : markInfos) {
                final List<Marker> markers = markInfo.type.toMarker(systemConfig, markInfo);
                if (markers != null) {
                    result.addAll(markers);
                }
            }
        }
        return result;
    }

    /**
     * @param systemConfig
     * @param markInfos
     * @return
     */
    public static List<Marker> toMarkers(SystemConfig systemConfig, MarkInfo markInfo) {
        return markInfo.type.toMarker(systemConfig, markInfo);
    }

}
