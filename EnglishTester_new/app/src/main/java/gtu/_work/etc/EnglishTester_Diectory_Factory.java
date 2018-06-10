package gtu._work.etc;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

import gtu.util.StringUtil_;

public class EnglishTester_Diectory_Factory {
    private static final String TAG = EnglishTester_Diectory_Factory.class.getSimpleName();

    private EnglishTester_Diectory eng1 = new EnglishTester_Diectory();
    private EnglishTester_Diectory2 eng2 = new EnglishTester_Diectory2();

    public EnglishTester_Diectory.WordInfo parseToWordInfo(final String word, final Context context, final Handler handler) {
        EnglishTester_Diectory.WordInfo wordVo = eng1.parseToWordInfo(word, context, handler);

        if (StringUtils.isBlank(wordVo.getMeaning()) || !StringUtil_.hasChinese(wordVo.getMeaning())) {

            String meaning2 = this.getMeaningFromEng2(word);
            if (StringUtils.isNotBlank(meaning2) && StringUtil_.hasChinese(meaning2)) {
                wordVo.setMeaning(meaning2);
            }
        }

        return wordVo;
    }

    private String getMeaningFromEng2(String word) {
        EnglishTester_Diectory2.WordInfo2 wordvo2 = eng2.parseToWordInfo(word);

        if (!wordvo2.getMeaningList().isEmpty()) {
            Set<String> meaningSet = new LinkedHashSet<>();
            for (String meaning : wordvo2.getMeaningList()) {
                meaningSet.add(meaning);
            }
            if (!meaningSet.isEmpty()) {
                return StringUtils.join(meaningSet, ";");
            }
        }

        if (StringUtils.isNotBlank(wordvo2.getMeaning2())) {
            return wordvo2.getMeaning2();
        }
        return "";
    }
}
