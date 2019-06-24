package com.example.gtu001.qrcodemaker.common;

import org.apache.commons.lang3.StringUtils;

public class DateUtil {
    public static String wasteTotalTime(long wasteTotal) {
        wasteTotal = wasteTotal / 1000;
        long sec_ = wasteTotal % 60;
        wasteTotal = wasteTotal / 60;
        long min = wasteTotal % 60;
        wasteTotal = wasteTotal / 60;
        long hours = wasteTotal % 24;
        wasteTotal = wasteTotal / 24;
//        return wasteTotal + "天" + hours + "時" + min + "分" + sec_ + "秒";
        StringBuilder sb = new StringBuilder();
        sb.append((wasteTotal == 0 ? "" : wasteTotal + "天"));
        sb.append((hours == 0 ? "" : hours + "時"));
        sb.append((min == 0 ? "" : min + "分"));
        sb.append((sec_ == 0 ? "" : sec_ + "秒"));
        return sb.toString();
    }

    /**
     * 計算所耗時間
     *
     * @param wasteTotal
     * @return
     */
    public static String wasteTotalTime_HHmmss(long wasteTotal) {
        wasteTotal = wasteTotal / 1000;
        long sec_ = wasteTotal % 60;
        wasteTotal = wasteTotal / 60;
        long min = wasteTotal % 60;
        wasteTotal = wasteTotal / 60;
        long hours = wasteTotal % 24;
        wasteTotal = wasteTotal / 24;
//        return wasteTotal + "天" + hours + "時" + min + "分" + sec_ + "秒";
        StringBuilder sb = new StringBuilder();
//        sb.append((wasteTotal == 0 ? "" : wasteTotal + "天"));
        sb.append((hours == 0 ? "" : StringUtils.leftPad("" + hours, 2, "0") + ":"));
        sb.append((min == 0 ? "" :  StringUtils.leftPad("" + min, 2, "0") + ":"));
        sb.append((sec_ == 0 ? "" :  StringUtils.leftPad("" + sec_, 2, "0") + ""));
        return sb.toString();
    }
}