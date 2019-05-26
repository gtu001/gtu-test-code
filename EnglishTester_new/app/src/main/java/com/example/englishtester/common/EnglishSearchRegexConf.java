package com.example.englishtester.common;

public class EnglishSearchRegexConf {

    private static final String FRANCE_CHARACTER = "àâéêèìôùûç";
    private static final String GERMAN_CHARACTER = "äöüÄÖÜß";
    private static final String CHINESE_CHARACTER = "\\u4e00-\\u9fa5";

    public static String getSearchRegex(boolean containSpace, boolean containChinese) {
        String containSpaceStr = containSpace ? "\\s" : "";
        String containChineseStr = containChinese ? CHINESE_CHARACTER : "";
        return "[a-zA-Z\\-" + FRANCE_CHARACTER + GERMAN_CHARACTER + containSpaceStr + containChineseStr + "]+";
    }
}
