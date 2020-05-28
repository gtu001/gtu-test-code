package gtu.poi.hssf;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelColorCreater {

    private short INDEX_START = 0x40 - 1;// 含
    private short INDEX_END = 0x8;// 含
    private short colorIndex = INDEX_START;
    private HSSFWorkbook workbook;

    private ExcelColorCreater(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public static final ExcelColorCreater newInstance(HSSFWorkbook workbook) {
        return new ExcelColorCreater(workbook);
    }

    public HSSFColor of(String rgbStr) {
        rgbStr = StringUtils.trimToEmpty(rgbStr).replaceFirst("#", "");
        byte r = (byte) Integer.parseInt(StringUtils.substring(rgbStr, 0, 2), 16);
        byte g = (byte) Integer.parseInt(StringUtils.substring(rgbStr, 2, 4), 16);
        byte b = (byte) Integer.parseInt(StringUtils.substring(rgbStr, 4, 6), 16);
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor hssfColor = null;
        try {
            hssfColor = palette.findColor(r, g, b);
            if (hssfColor == null) {
                palette.setColorAtIndex(colorIndex, r, g, b);
                hssfColor = palette.getColor(colorIndex);
                if (colorIndex > INDEX_END) {
                    colorIndex--;
                } else {
                    System.out.println("Excel調色盤顏色格數已用盡!");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hssfColor;
    }
}
