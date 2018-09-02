package com.example.englishtester;

import android.content.Context;
import android.os.Handler;
import com.example.englishtester.common.Log;

import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory.WordInfo;
import gtu.number.SimilarityUtil;

public class QuestionFilterService {

    private static final String TAG = QuestionFilterService.class.getSimpleName();

    public QuestionFilterService() {
    }

    String englishWord;
    String englishDescription;
    EnglishwordInfoDAO dao;
    EnglishTester_Diectory diectory = new EnglishTester_Diectory();


    public QuestionFilterService(String englishWord, String englishDescription, Context context) {
        this.englishWord = englishWord;
        this.dao = new EnglishwordInfoDAO(context);
        this.englishDescription = englishDescription;

        if (!StringUtils.isBlank(englishDescription)) {
            return;
        }

        EnglishWord englishWordBean = this.dao.queryOneWord(englishWord);
        if (englishWordBean == null) {
            WordInfo info = diectory.parseToWordInfo(englishWord, context, null);
            englishDescription = info.getMeaning();
        } else {
            englishDescription = englishWordBean.englishDesc;
        }
    }

    public List<EnglishWord> getMatchList() {
        long startTime = System.currentTimeMillis();

        // 取得相似答案
        List<EnglishWord> list = getInitList();

        if (StringUtils.isNotBlank(englishDescription)) {

            // 取得解釋樣本清單
            List<String> descList = getQuestionDescList(englishDescription);

            // 以解釋樣本清單 去 移除掉重複解釋
            removeSameDescription(descList, list);
        }

        if (list == null) {
            list = new ArrayList<EnglishWord>();
        }

        long during = System.currentTimeMillis() - startTime;
        Log.v(TAG, "##取得樣本耗時 - " + during);
        return list;
    }

    /**
     * 初始化題目清單
     */
    private List<EnglishWord> getInitList() {
        String prefix = englishWord.substring(0, 1);
        List<EnglishWord> list = new ArrayList<EnglishWord>();
        String englishWord_ = englishWord.toString();
        if (StringUtils.contains(englishWord_, "'")) {
            englishWord_ = englishWord_.replaceAll("'", "''");
        }
        /*
        if (list.size() < 4) {
            list = dao.query(//
                    EnglishWordSchema.ENGLISH_ID + " like '" + prefix + "%' and " + //
                            EnglishWordSchema.ENGLISH_ID + " not like '" + englishWord_ + "%'", null);
        }
        if (list.size() < 4) {// 情境 : 輸入英文只有一個字母
            List<EnglishWord> list2 = dao.query(//
                    EnglishWordSchema.ENGLISH_ID + " like '" + prefix + "%' and " //
                            + EnglishWordSchema.ENGLISH_ID + " != '" + englishWord_ + "' ", null);
            list = _addAll(list, list2);
        }
        */
        //增快效能
        list = _getInitList(englishWord);
        Log.v(TAG, "query size = " + list.size());
        return list;
    }

    /**
     * 移除掉相同解釋答案
     */
    private void removeSameDescription(List<String> descList, List<EnglishWord> list) {
        for (int ii = 0; ii < list.size(); ii++) {
            EnglishWord eng = list.get(ii);
            if (_isSameDescription(eng.englishDesc, descList)) {
                list.remove(ii);
                ii--;
            }
        }
    }

    /**
     * 是否含有相同解釋
     */
    private boolean _isSameDescription(String englishDesc, List<String> descList) {
        for (String desc : descList) {
            if (englishDesc.contains(desc)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 新增題目解釋清單
     */
    private List<String> getQuestionDescList(String desc) {
        List<String> descList = new ArrayList<String>();
        char[] arry = desc.toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < arry.length; ii++) {
            char c = arry[ii];
            if (new String(new char[]{c}).getBytes().length != 1) {
                sb.append(c);
                if (ii + 1 < arry.length) {
                    if (new String(new char[]{arry[ii + 1]}).getBytes().length == 1) {
                        // 新增descList
                        sb = _addDesc(sb, descList);
                    }
                } else if (ii == arry.length - 1) {
                    // 新增descList
                    sb = _addDesc(sb, descList);
                }
            }
        }

        Log.v(TAG, "解釋樣本 : " + descList);
        return descList;
    }

    /**
     * 新增題目解釋清單
     */
    private StringBuilder _addDesc(StringBuilder sb, List<String> descList) {
        String str = sb.toString();
        str = str.replaceAll("的$", "");
        str = str.replaceAll("地$", "");
        if (StringUtils.isNotBlank(str)) {
            descList.add(str);
            sb = new StringBuilder();
        }
        return sb;
    }

    /**
     * addAll不重複
     */
    private List<EnglishWord> _addAll(List<EnglishWord> list1, List<EnglishWord> list2) {
        Set<EnglishWord> set1 = new LinkedHashSet<EnglishWord>();
        set1.addAll(list1);
        set1.addAll(list2);
        return new ArrayList<EnglishWord>(set1);
    }

    /**
     * 增加效能
     */
    private List<EnglishWord> _getInitList(String currentWord) {
        String prefix = currentWord.substring(0, 1);
        int count = 0;
        List<EnglishWord> rtnList = new ArrayList<EnglishWord>();
        final int _MAXSIZE = 500;

        A:
        for (EnglishWord e : dao.queryAll(false)) {
            if (e.englishId.startsWith(prefix) && !currentWord.equalsIgnoreCase(e.englishId)) {
                rtnList.add(e);
                count++;
                if (count == _MAXSIZE) {
                    break A;
                }
            }
        }
        return rtnList;
    }

    /**
     * 取得相似度排序
     */
    public static List<EnglishWord> similarityWordList(String englishWord, List<EnglishWord> list) {
        long startTime = System.currentTimeMillis();

        Map<Double, List<EnglishWord>> map = new HashMap<>();
        for (EnglishWord e : list) {
            Double d = SimilarityUtil.sim(englishWord, e.englishId);
            List<EnglishWord> lst = new ArrayList<>();
            if(map.containsKey(d)){
                lst = map.get(d);
            }
            lst.add(e);
            map.put(d, lst);
        }

        List<Double> valueList = new ArrayList<Double>(map.keySet());
        Collections.sort(valueList);
        Collections.reverse(valueList);

        List<EnglishWord> rtnList = new ArrayList<EnglishWord>();
        for (Double d : valueList) {
            rtnList.addAll(map.get(d));
        }

        long during = System.currentTimeMillis() - startTime;
        Log.v(TAG, "##相似比對耗時 - " + during);
        return rtnList;
    }
}
