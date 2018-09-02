package com.example.englishtester.common;

import com.example.englishtester.common.Log;

import com.example.englishtester.Constant;
import com.example.englishtester.EnglishwordInfoDAO;
import com.example.englishtester.MainActivityDTO;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import gtu.number.RandomUtil;

/**
 * Created by gtu001 on 2018/5/4.
 */

public abstract class InitExamInterface {

    private static final String TAG = InitExamInterface.class.getSimpleName();

    public abstract void appendQuestionToEnglishProp(Map<String, EnglishwordInfoDAO.EnglishWord> englishProp) throws Exception;

    public String initExam(final MainActivityDTO dto, final File file) {
        String messageStr = null;

        dto.setWordsList(new ArrayList<String>());
        dto.setPickProp(new Properties());
        dto.setDoAnswerList(new ArrayList<String>());

        if (file != null) {
            dto.setEnglishFile(file);
        }

        try {
            if (dto.getEnglishProp() == null) {
                dto.setEnglishProp(new HashMap<String, EnglishwordInfoDAO.EnglishWord>());
            }

            this.appendQuestionToEnglishProp(dto.getEnglishProp());
        } catch (Exception ex) {
            messageStr = "錯誤 :" + ex.getMessage();
            Log.e(TAG, "initExam ERR : " + ex.getMessage(), ex);
            throw new RuntimeException("讀檔案失敗!:" + ex.getMessage());
        }

        //取得未測單字數
        String tmpMsg = this.getUntestCountMessage(dto);
        if (StringUtils.isNotBlank(tmpMsg)) {
            messageStr = tmpMsg;
        }

        // 隨機排序
        dto.setWordsList(RandomUtil.randomList(dto.getWordsList()));
        // 備份
        dto.setWordsListCopy((List<String>) ((ArrayList<String>) dto.getWordsList()).clone());

        // 建立中斷備份檔
        this.createInterruptFile(dto);

        return messageStr;
    }


    private void createInterruptFile(final MainActivityDTO dto) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constant.INTERRUPT_INIT_FILE));
            oos.writeObject(dto);
            oos.flush();
            oos.close();
            Log.v(TAG, "write interrupt init backup!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getUntestCountMessage(final MainActivityDTO dto) {
        String messageStr = "";
        int noExamCount = 0;
        for (String text : dto.getEnglishProp().keySet()) {
            dto.getWordsList().add(text);
            EnglishwordInfoDAO.EnglishWord word = dto.getEnglishProp().get(text);
            if (word.getExamTime() == 0) {
                noExamCount++;
            }
        }
        if (noExamCount != 0) {
            messageStr = "未測單字數:" + noExamCount;
        }
        return messageStr;
    }
}
