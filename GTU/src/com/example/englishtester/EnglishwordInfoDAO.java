package com.example.englishtester;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EnglishwordInfoDAO {

    public static class EnglishWord implements Serializable {
        private static final long serialVersionUID = -358966271246450347L;

        String englishId;
        String englishDesc;
        String pronounce;
        int browserTime;
        int examTime;
        int failTime;
        long insertDate;
        long examDate;
        long lastbrowerDate;
        long lastDuring;
        int lastResult;
        String btnAppendix;// 按鈕附加資訊

        public String getEnglishId() {
            return englishId;
        }

        public void setEnglishId(String englishId) {
            this.englishId = englishId;
        }

        public String getEnglishDesc() {
            return englishDesc;
        }

        public void setEnglishDesc(String englishDesc) {
            this.englishDesc = englishDesc;
        }

        public String getPronounce() {
            return pronounce;
        }

        public void setPronounce(String pronounce) {
            this.pronounce = pronounce;
        }

        public int getBrowserTime() {
            return browserTime;
        }

        public void setBrowserTime(int browserTime) {
            this.browserTime = browserTime;
        }

        public int getExamTime() {
            return examTime;
        }

        public void setExamTime(int examTime) {
            this.examTime = examTime;
        }

        public int getFailTime() {
            return failTime;
        }

        public void setFailTime(int failTime) {
            this.failTime = failTime;
        }

        public long getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(long insertDate) {
            this.insertDate = insertDate;
        }

        public long getExamDate() {
            return examDate;
        }

        public void setExamDate(long examDate) {
            this.examDate = examDate;
        }

        public long getLastbrowerDate() {
            return lastbrowerDate;
        }

        public void setLastbrowerDate(long lastbrowerDate) {
            this.lastbrowerDate = lastbrowerDate;
        }

        public long getLastDuring() {
            return lastDuring;
        }

        public void setLastDuring(long lastDuring) {
            this.lastDuring = lastDuring;
        }

        public int getLastResult() {
            return lastResult;
        }

        public void setLastResult(int lastResult) {
            this.lastResult = lastResult;
        }

        public String getBtnAppendix() {
            return btnAppendix;
        }

        public void setBtnAppendix(String btnAppendix) {
            this.btnAppendix = btnAppendix;
        }

        @Override
        public String toString() {
            return "EnglishWord [englishId=" + englishId + ", englishDesc=" + englishDesc + ", pronounce=" + pronounce + ", browserTime=" + browserTime + ", examTime=" + examTime + ", failTime="
                    + failTime + ", insertDate=" + insertDate + ", examDate=" + examDate + ", lastbrowerDate=" + lastbrowerDate + ", lastDuring=" + lastDuring + ", lastResult=" + lastResult + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((englishId == null) ? 0 : englishId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            EnglishWord other = (EnglishWord) obj;
            if (englishId == null) {
                if (other.englishId != null)
                    return false;
            } else if (!englishId.equals(other.englishId))
                return false;
            return true;
        }
    }
}
