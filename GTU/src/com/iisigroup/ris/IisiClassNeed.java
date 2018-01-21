package com.iisigroup.ris;

import gtu.collection.ListUtil;
import gtu.collection.MapUtil;
import gtu.log.LogbackUtil;
import gtu.reflect.ReflectGetter;
import gtu.reflect.ReflectUtil;
import gtu.reflect.ToStringUtil;

import com.google.common.collect.ImmutableList;

public class IisiClassNeed {

    public static void main(String[] args) {

        System.out.println(ToStringUtil.toString(IisiClassNeed.class));

        ImmutableList.<Class<?>> builder()//
                .add(ToStringUtil.class)//
                .add(ReflectGetter.class)//
                .add(ReflectUtil.class)//
                .add(LogbackUtil.class)//
                .add(MapUtil.class)//
                .add(ListUtil.class)//
                .add(LogbackUtil.class)//
                .build();
    }
}
