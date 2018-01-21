package gtu.string;

import java.util.HashSet;
import java.util.Set;

public class FullCharChecker {

    /**
     * 判斷是否為全行字形
     */
    public static final boolean isFullType(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || (c >= 0x4e00) && (c <= 0x9fbb) || fullTypedExceptionCharacters.contains((int) c)) {
            return true;
        }
        if ((int) c >= Integer.parseInt("4E00", 16)) {
            fullTypedExceptionCharacters.add((int) c);
        }
        return false;
    }

    private static final Set<Integer> fullTypedExceptionCharacters = new HashSet<Integer>();

    static {
        int[] fullTypedIntegers = new int[] { 59102, 63047, 61113, 62677, 62491, 62981, 62326, 59357, 62808, 62942, 62782, 62810, 65105, 59375, 59708, 61698, 57344, 57627, 65117, 65118, 60071, 58664,
                58759, 63004, 58905, 62933, 62987, 58170, 59859, 57345, 61112, 57438, 63220, 59800, 58822, 58182, 61848, 63442, 60032, 60777 };
        for (int i : fullTypedIntegers) {
            fullTypedExceptionCharacters.add(i);
        }
    }
}
