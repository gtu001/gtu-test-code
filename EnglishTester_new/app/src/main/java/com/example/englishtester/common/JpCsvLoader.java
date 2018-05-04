package com.example.englishtester.common;

import android.content.Context;
import android.util.Log;

import com.example.englishtester.EnglishwordInfoDAO;
import com.opencsv.CSVReader;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by gtu001 on 2018/5/4.
 */

public class JpCsvLoader {

    private static final String TAG = JpCsvLoader.class.getSimpleName();

    private Jp50Handler handler;

    private JpCsvLoader(Context context) {
        try {
            if (handler != null) {
                return;
            }
            InputStream inputStream = context.getAssets().open("jp_50_character.csv");
            CsvFile csv = readCsv(inputStream);
            handler = new Jp50Handler(csv);
        } catch (IOException e) {
            throw new RuntimeException("JpCsvLoader ERR : " + e.getMessage(), e);
        }
    }

    public static JpCsvLoader newInstance(Context context) {
        return new JpCsvLoader(context);
    }

    public Map<String, EnglishwordInfoDAO.EnglishWord> getExamQuestion() {
        Map<String, EnglishwordInfoDAO.EnglishWord> rtnMap = new LinkedHashMap<>();
        Properties prop = handler.generateQuestionMap();
        for (Object key : prop.keySet()) {
            String question = (String) key;
            String answer = prop.getProperty(question);
            if (StringUtils.isNotBlank(answer)) {
                EnglishwordInfoDAO.EnglishWord word = new EnglishwordInfoDAO.EnglishWord();
                word.setEnglishId(question);
                word.setEnglishDesc(answer);
                word.setBtnAppendix(getBtnAppendixStr(question));
                rtnMap.put(question, word);
            }
        }
        return rtnMap;
    }

    private String getBtnAppendixStr(String englishId) {
        Jp50 tmp = null;
        for (Jp50 jp : handler.getJpLst()) {
            if (StringUtils.equals(jp.jp1, englishId) || StringUtils.equals(jp.jp2, englishId)) {
                tmp = jp;
                break;
            }
        }
        return String.format("\n<平:%s, 片:%s>", tmp.jp1, tmp.jp2);
    }

    private class Jp50Handler {
        List<Jp50> lst;

        public List<Jp50> getJpLst() {
            if (lst == null) {
                throw new RuntimeException("未初始化!");
            }
            return lst;
        }

        private Jp50Handler(CsvFile csv) {
            lst = getJpLst(csv);
        }

        private List<Jp50> getJpLst(CsvFile csv) {
            List<Jp50> lst = new ArrayList<>();
            for (String[] arry : csv.lst) {
                Log.v(TAG, Arrays.toString(arry));
                Jp50 jp = toJp50(arry);
                lst.add(jp);
            }
            return lst;
        }

        private String getStrFromArry(int index, String[] arry) {
            if (index >= arry.length) {
                return "";
            }
            return arry[index];
        }

        private Jp50 toJp50(String[] arry) {
            Jp50 jp = new Jp50();
            jp.jp1 = getStrFromArry(0, arry);
            jp.jp2 = getStrFromArry(1, arry);
            jp.prounce = getStrFromArry(2, arry);
            jp.chsProunce = getStrFromArry(3, arry);
            return jp;
        }

        public Properties generateQuestionMap() {
            Properties prop = new Properties();
            for (Jp50 p : lst) {
                String prounce = String.format("英:%s, 注:%s", p.prounce, p.chsProunce);
                prop.setProperty(p.jp1, prounce);
                prop.setProperty(p.jp2, prounce);
            }
            return prop;
        }
    }

    private class Jp50 {
        String jp1;//平假名
        String jp2;//片假名
        String prounce;
        String chsProunce;
    }

    private static class CsvFile {
        int minColumnLen = Integer.MAX_VALUE;
        int maxColumnLen = Integer.MIN_VALUE;
        List<String[]> lst = new ArrayList<>();
    }

    private CsvFile readCsv(InputStream inputStream) {
        try {
            CsvFile csv = new CsvFile();
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream, "UTF-8"));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                csv.lst.add(nextLine);
                csv.maxColumnLen = Math.max(csv.maxColumnLen, nextLine.length);
                csv.minColumnLen = Math.min(csv.minColumnLen, nextLine.length);
            }
            return csv;
        } catch (Exception ex) {
            Log.e(TAG, "readCsv ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("readCsv ERR : " + ex.getMessage(), ex);
        }
    }
}
