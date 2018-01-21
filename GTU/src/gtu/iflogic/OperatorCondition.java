package gtu.iflogic;

import gtu.string.StringUtil_.ScannerWithDelimiter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class OperatorCondition {
    String conditionStr;
    List<PairOperatorCheck> pairList;
    private final static Pattern basePattern = Pattern.compile("(>|<|>\\=|<\\=|\\=|!\\=)", Pattern.CASE_INSENSITIVE);

    public void execute() {
        pairList = new ArrayList<PairOperatorCheck>();
        final PairOperatorCheck pck = new PairOperatorCheck();
        ScannerWithDelimiter scanner = new ScannerWithDelimiter() {
            public void appendReplacement(String inputStr, MatchResult matcher) {
                pck.left = inputStr;
                pck.middle = matcher.group();
            }

            public void appendTail(String inputStr) {
                pck.right = inputStr;
                try {
                    pairList.add((PairOperatorCheck) pck.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                pck.clean();
            }
        };
        scanner.execute(basePattern, conditionStr);
        for (PairOperatorCheck chk : pairList) {
            System.out.println("pair ==> " + chk);
        }
    }
}