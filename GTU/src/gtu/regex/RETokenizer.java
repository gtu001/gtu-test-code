package gtu.regex;
import java.util.*;
import java.util.regex.*;

public class RETokenizer {
    public static List<Token> tokenize(String source, List<Rule> rules) {
        List<Token> tokens = new ArrayList<Token>();
        int pos = 0;
        final int end = source.length();
        Matcher m = Pattern.compile("dummy").matcher(source);
        m.useTransparentBounds(true).useAnchoringBounds(false);
        while (pos < end) {
            m.region(pos, end);
            for (Rule r : rules) {
                if (m.usePattern(r.pattern).lookingAt()) {
                    String value = source.substring(m.start(), m.end());
                    tokens.add(new Token(r.name, value, m.start(), m.end()));
                    pos = m.end()-1;
                    break;
                }
            }
            pos++; // bump-along, in case no rule matched
        }
        return tokens;
    }

    public static class Rule {
        final String name;
        final Pattern pattern;

        Rule(String name, String regex) {
            this.name = name;
            pattern = Pattern.compile(regex);
        }

        public String getName() {
            return name;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    public static class Token {
        final String name;
        final String value;
        final int startPos;
        final int endPos;

        Token(String name, String value, int startPos, int endPos) {
            this.name = name;
            this.value = value;
            this.startPos = startPos;
            this.endPos = endPos;
        }

        @Override
        public String toString() {
            return String.format("Token [%2d, %2d, %s, %s]", startPos, endPos, name, value);
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public int getStartPos() {
            return startPos;
        }

        public int getEndPos() {
            return endPos;
        }
    }

//    PUBLIC STATIC VOID MAIN(STRING[] ARGS) THROWS EXCEPTION {
//        LIST<RULE> RULES = NEW ARRAYLIST<RULE>();
//        RULES.ADD(NEW RULE("WORD", "[A-ZA-Z]+"));
//        RULES.ADD(NEW RULE("QUOTED", "\"[^\"]*+\""));
//        RULES.ADD(NEW RULE("COMMENT", "//.*"));
//        RULES.ADD(NEW RULE("WHITESPACE", "\\S+"));
////        STRING STR = "FOO //IN \"COMMENT\"\NBAR \"NO //COMMENT\" END";
//        STRING STR = "R_ITEM_CODE LIKE '%S%%' OR ITEM_CODE LIKE '%S%%'";
//        LIST<TOKEN> RESULT = RETOKENIZER.TOKENIZE(STR, RULES);
//        FOR (TOKEN T : RESULT) {
//            SYSTEM.OUT.PRINTLN(T);
//        }
//    }
    public static void main(String[] args) throws Exception {
        List<Rule> rules = new ArrayList<Rule>();
        rules.add(new Rule("SPLIT", ",") );
        rules.add(new Rule("WORD", "[A-Za-z0-9\\s]+"));
        rules.add(new Rule("DEFAULT", "\\[{1}[A-Za-z0-9]+\\]{1}"));
        String str = "aaaaaa[333],xxxxxxx,dfdfdfd[33332222]";
        List<Token> result = RETokenizer.tokenize(str, rules);
        for (Token t : result) {
            System.out.println(t);
        }
    }
}