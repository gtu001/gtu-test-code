/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.log.finder;

import gtu.log.finder.DebugMointerParameterParserMF.MethodOrFieldInvoke;

import java.util.ArrayList;
import java.util.List;

public class DebugMointerParameterParserMaster {

    public static void main(String[] args) {
//        String parseStr = "vvvvvvvvvvvv[0].eeee('vvvvveeee', 3, 3L, vvvv[3].toString().aaaa()).substring(3,5)[3].toXml()";
//        String parseStr = "[2].table[3].toString()";
        String parseStr = "_#[0].[0]#_";
        List<MethodOrFieldInvoke> list = DebugMointerParameterParserMaster.newInstance(parseStr).execute();
        for(MethodOrFieldInvoke moi : list){
            System.out.println(moi);
        }
        System.out.println("done...");
    }
    
    private DebugMointerParameterParserMaster(){
    }
    
    private String parseStr;
    public static DebugMointerParameterParserMaster newInstance(String parseStr){
        DebugMointerParameterParserMaster test = new DebugMointerParameterParserMaster();
        test.parseStr = parseStr;
        test.execute();
        return test;
    }
    
    public List<MethodOrFieldInvoke> execute(){
        DebugMointerParameterParserDelimit delimit = DebugMointerParameterParserDelimit.doParse(parseStr);
        List<MethodOrFieldInvoke> list = new ArrayList<MethodOrFieldInvoke>();
        for(String parseStr : delimit.getFinalList()){
            MethodOrFieldInvoke moi = DebugMointerParameterParserMF.newInstance(parseStr, delimit).execute();
            list.add(moi);
        }
        return list;
    }
}
