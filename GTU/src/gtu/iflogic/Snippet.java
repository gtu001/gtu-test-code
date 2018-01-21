package gtu.iflogic;

public class Snippet {
    
    public static void main(String[] args) {
        QuoteGroup q1 = new QuoteGroup();
        q1.conditionStr = "aa = bb and ((11 = 22 and 33 = 44) and (55 = 66 or 77 = 88))";
        q1.execute();

        for (String key : q1.logicMap.keySet()) {
            String condStr = q1.logicMap.get(key);
            System.out.println("######" + condStr);
            WhereCondition ocnd = new WhereCondition();
            ocnd.conditionStr = condStr;
            ocnd.execute();
        }
        System.out.println("done...");
    }
}
