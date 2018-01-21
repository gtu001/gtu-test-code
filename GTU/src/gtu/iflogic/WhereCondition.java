package gtu.iflogic;

import gtu.string.StringUtil_.ScannerWithDelimiter;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class WhereCondition {
    String conditionStr;
    private final static Pattern basePattern = Pattern.compile("(and|or)", Pattern.CASE_INSENSITIVE);

    public void execute() {
        ScannerWithDelimiter scanner = new ScannerWithDelimiter() {
            public void appendReplacement(String inputStr, MatchResult matcher) {
                System.out.println("appendReplacement = " + inputStr + "<<<<" + matcher.group());
                checkResult(inputStr);
            }

            public void appendTail(String inputStr) {
                System.out.println("appendTail = " + inputStr);
                checkResult(inputStr);
            }

            private boolean checkResult(String intputStr) {
                OperatorCondition cond = new OperatorCondition();
                cond.conditionStr = intputStr;
                cond.execute();
                return false;
            }
        };
        scanner.execute(basePattern, conditionStr);
    }
}