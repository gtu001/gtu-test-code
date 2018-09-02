package gtu.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** 
 * 請改使用 SimilarityUtil
 */
@Deprecated
public class FuzzyCompareUtil {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("aaa_bbb_ccc");
        list.add("aa_bb_cc");
        list.add("aaabbbccc");

    }

    private static final FuzzyCompareUtil INSTANCE = new FuzzyCompareUtil();

    public static FuzzyCompareUtil getInstance() {
        return INSTANCE;
    }

    public String pickone(String compare, List<String> list, boolean ignoreCase) {
        List<CompareResult> clist = new ArrayList<CompareResult>();
        for (String str : list) {
            CompareResult cr = new CompareResult();
            cr.compareStr = str;
            cr.result = this.fuzzyCompare(compare, str, ignoreCase);
            clist.add(cr);
        }
        Collections.sort(clist, comparator);
        if (clist.size() > 0) {
            return clist.get(0).compareStr;
        }
        return null;
    }

    Comparator<CompareResult> comparator = new Comparator<CompareResult>() {
        float compareResult(CompareResult o1) {
            return ((float) o1.result.length()) / ((float) o1.compareStr.length());
        }

        @Override
        public int compare(CompareResult o1, CompareResult o2) {
            if (compareResult(o1) > compareResult(o2)) {
                return -1;
            }
            if (compareResult(o1) < compareResult(o2)) {
                return 1;
            }
            return 0;
        }
    };

    static class CompareResult {
        String compareStr;
        String result;
    }

    String fuzzyCompare(String compare1, String compare2, boolean ignoreCase) {
        if (ignoreCase) {
            compare1 = compare1.toLowerCase();
            compare2 = compare2.toLowerCase();
        }
        char[] c1 = compare1.toCharArray();
        char[] c2 = compare2.toCharArray();
        char tmp = ' ';
        int c2Pos = 0;
        StringBuilder sb = new StringBuilder();
        f1: for (int ii = 0; ii < c1.length; ii++) {
            tmp = c1[ii];
            f2: for (int jj = c2Pos; jj < c2.length; jj++) {
                if (tmp == c2[jj]) {
                    sb.append(tmp);
                    if (jj + 1 <= c2.length - 1) {
                        c2Pos = jj + 1;
                    } else {
                        break f1;
                    }
                    break f2;
                }
            }
        }
        return sb.toString();
    }
}
