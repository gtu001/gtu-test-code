package com.example.englishtester;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.englishtester.common.Log;

import com.example.englishtester.EnglishwordInfoDAO.EnglishWord;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainActivityDTO implements Parcelable, Serializable {

    private static final String TAG = MainActivityDTO.class.getSimpleName();
    private static final long serialVersionUID = 1L;

    int correctBtnNum;//正確答案
    String currentText;//目前題目
    boolean showAnswerLabel;//是否顯示答案
    long transactionId = 0L;//交易代碼 判斷是否要記錄此筆單字資訊用
    long duringTime = 0L;

    List<String> doAnswerList;//有作答的清單
    List<String> wordsList;//剩餘題目數
    List<String> doPronounceList;
    Properties pickProp;//答錯項目 <-無法回復
    List<String> pickPropList;//答錯項目(為了回復此物件)
    Map<String, EnglishWord> englishProp;//題庫<-無法回復
    File englishFile;//題庫

    CurrentMode currentMode = CurrentMode.ANSWER;//現在模式

    List<String> wordsListCopy;//測驗題目 - 備份
    boolean isWhiteBackground = true;//被景色為白色
    boolean isListenTestMode = false;//是否為聽力測驗模式
    boolean isAutoChangeQuestion = true;//是否自動跳題
    boolean isAutoPronounce = false;//是否自動發音


    enum CurrentMode {
        ANSWER,//秀答案 
        QUESTION,//秀問題
        ;
    }

    void moveFrom(MainActivityDTO dto) {
        for (Field f : MainActivityDTO.class.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                Object val = f.get(dto);
                f.set(this, val);
            } catch (Exception e) {
            }
        }
    }

    enum ValueTransfer {
        correctBtnNum,
        currentText,
        showAnswerLabel,
        transactionId,
        duringTime,
        doAnswerList,
        wordsList,
        pickProp,
        englishProp,
        englishFile,
        currentMode,
        wordsListCopy,
        isWhiteBackground,
        isListenTestMode,
        doPronounceList,
        pickPropList,
        isAutoChangeQuestion,
        isAutoPronounce,;

        void createFromParcel(MainActivityDTO dto, Parcel paramParcel, ClassLoader loader) {
            try {
                Field field = MainActivityDTO.class.getDeclaredField(this.name());
                field.setAccessible(true);
                Object val = paramParcel.readValue(loader);
                field.set(dto, val);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void writeToParcel(MainActivityDTO dto, Parcel paramParcel, int paramInt) {
            try {
                Field field = MainActivityDTO.class.getDeclaredField(this.name());
                field.setAccessible(true);
                Object val = field.get(dto);
                paramParcel.writeValue(val);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    //↓↓↓↓↓↓  實作取得DTO的方式
    public static final Parcelable.Creator<MainActivityDTO> CREATOR = new Creator<MainActivityDTO>() {
        @Override
        public MainActivityDTO createFromParcel(Parcel paramParcel) {
            ClassLoader loader = this.getClass().getClassLoader();
            MainActivityDTO dto = new MainActivityDTO();
            for (ValueTransfer e : ValueTransfer.values()) {
                e.createFromParcel(dto, paramParcel, loader);
            }

            return dto;
        }

        @Override
        public MainActivityDTO[] newArray(int paramInt) {
            return new MainActivityDTO[paramInt];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel paramParcel, int paramInt) {
        for (ValueTransfer e : ValueTransfer.values()) {
            e.writeToParcel(this, paramParcel, paramInt);
        }
    }
    //↑↑↑↑↑↑  實作取得DTO的方式


    public int getCorrectBtnNum() {
        return correctBtnNum;
    }

    public void setCorrectBtnNum(int correctBtnNum) {
        this.correctBtnNum = correctBtnNum;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public boolean isShowAnswerLabel() {
        return showAnswerLabel;
    }

    public void setShowAnswerLabel(boolean showAnswerLabel) {
        this.showAnswerLabel = showAnswerLabel;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getDuringTime() {
        return duringTime;
    }

    public void setDuringTime(long duringTime) {
        this.duringTime = duringTime;
    }

    public List<String> getDoAnswerList() {
        return doAnswerList;
    }

    public void setDoAnswerList(List<String> doAnswerList) {
        this.doAnswerList = doAnswerList;
    }

    public List<String> getWordsList() {
        return wordsList;
    }

    public void setWordsList(List<String> wordsList) {
        this.wordsList = wordsList;
    }

    public List<String> getDoPronounceList() {
        return doPronounceList;
    }

    public void setDoPronounceList(List<String> doPronounceList) {
        this.doPronounceList = doPronounceList;
    }

    public Properties getPickProp() {
        return pickProp;
    }

    public void setPickProp(Properties pickProp) {
        this.pickProp = pickProp;
    }

    public List<String> getPickPropList() {
        return pickPropList;
    }

    public void setPickPropList(List<String> pickPropList) {
        this.pickPropList = pickPropList;
    }

    public Map<String, EnglishWord> getEnglishProp() {
        return englishProp;
    }

    public void setEnglishProp(Map<String, EnglishWord> englishProp) {
        this.englishProp = englishProp;
    }

    public File getEnglishFile() {
        return englishFile;
    }

    public void setEnglishFile(File englishFile) {
        this.englishFile = englishFile;
    }

    public CurrentMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(CurrentMode currentMode) {
        this.currentMode = currentMode;
    }

    public List<String> getWordsListCopy() {
        return wordsListCopy;
    }

    public void setWordsListCopy(List<String> wordsListCopy) {
        this.wordsListCopy = wordsListCopy;
    }

    public boolean isWhiteBackground() {
        return isWhiteBackground;
    }

    public void setWhiteBackground(boolean whiteBackground) {
        isWhiteBackground = whiteBackground;
    }

    public boolean isListenTestMode() {
        return isListenTestMode;
    }

    public void setListenTestMode(boolean listenTestMode) {
        isListenTestMode = listenTestMode;
    }

    public boolean isAutoChangeQuestion() {
        return isAutoChangeQuestion;
    }

    public void setAutoChangeQuestion(boolean autoChangeQuestion) {
        isAutoChangeQuestion = autoChangeQuestion;
    }

    public boolean isAutoPronounce() {
        return isAutoPronounce;
    }

    public void setAutoPronounce(boolean autoPronounce) {
        isAutoPronounce = autoPronounce;
    }
}
