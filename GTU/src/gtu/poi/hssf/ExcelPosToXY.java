package gtu.poi.hssf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelPosToXY {

    public static void main(String[] args) {
        ExcelPosToXY t = new ExcelPosToXY("AA10");
        System.out.println(t.toString());
    }
    
    private String label;
    private String columnLabel;
    private String rowLabel;
    private int x;
    private int y;
    
    public ExcelPosToXY(String label) {
        this.label = label;
        Pattern ptn = Pattern.compile("([a-zA-Z]+)(\\d+)");
        Matcher mth = ptn.matcher(label);
        if(!mth.matches()) {
            throw new RuntimeException("格式必須為  \"英文再來是數字\" Ex : A5");
        }
        columnLabel = mth.group(1);
        rowLabel = mth.group(2);
        
        y = Integer.parseInt(rowLabel) -1;
        x = ExcelUtil.cellEnglishToPos(columnLabel);
    }

    public String getLabel() {
        return label;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "ExcelPosToXY [label=" + label + ", columnLabel=" + columnLabel + ", rowLabel=" + rowLabel + ", x=" + x + ", y=" + y + "]";
    }
}
