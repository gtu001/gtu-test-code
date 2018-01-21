package gtu.google.steve.base;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * 
 * 
 * @author Steve Tien
 * @version 1.0, Sep 28, 2010
 */
public class SpRtTransformExcelToJsonPC {

    public static enum ImportCell {

        ORGANIZATION_ID(0) {
        },
        ITEM_NUMBER(1) {
        },
        SERIAL_NUMBER(2) {
        },
        SUBINVENTORY_CODE(3) {
        },
        LOCATOR(4) {
        },
        ;

        private final int cellNo;

        ImportCell(final int cellNo) {
            this.cellNo = cellNo;
        }

        public short cellNo() {
            return (short) cellNo;
        }

    }

    public static class RowParser {

        public static class Builder {

            private Builder() {
            }

            public RowParser build() {
                return new RowParser(this);
            }

        }

        public static Builder builder() {
            return new Builder();
        }

        private final Map<ImportCell, Function<String, String>> cellValueConverter;

        private RowParser(final Builder builder) {

            final Map<ImportCell, Function<String, String>> cellValueConverterBuilder = new EnumMap<ImportCell, Function<String, String>>(
                    ImportCell.class);
            // Functions.<String> identity() 建立Function範例免實作
            cellValueConverterBuilder.put(ImportCell.ORGANIZATION_ID, Functions.<String> identity());
            cellValueConverterBuilder.put(ImportCell.ITEM_NUMBER, Functions.<String> identity());
            cellValueConverterBuilder.put(ImportCell.SERIAL_NUMBER, Functions.<String> identity());
            cellValueConverterBuilder.put(ImportCell.SUBINVENTORY_CODE, Functions.<String> identity());
            cellValueConverterBuilder.put(ImportCell.LOCATOR, Functions.<String> identity());
            cellValueConverter = ImmutableMap.copyOf(cellValueConverterBuilder);

        }

        public JSONObject parse(final HSSFRow row) throws JSONException {

            final JSONObject jsonObject = new JSONObject();

            for (final ImportCell importCell : ImportCell.values()) {
                final String jsonObjectKey = importCell.toString();
                final String jsonObjectValue = cellValueConverter.get(importCell).apply(
                        parseCell(row.getCell(importCell.cellNo())));
                jsonObject.put(jsonObjectKey, jsonObjectValue);
            }

            return jsonObject;
        }

        private String parseCell(final HSSFCell cell) {

            if (cell == null) {
                return "";
            }

            final DecimalFormat decimalFormat = new DecimalFormat("####################0.##########");
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");

            switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_BLANK:
                return "";
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return Boolean.valueOf(cell.getBooleanCellValue()).toString().trim();
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return dateFormat.format(cell.getDateCellValue());
                }
                return decimalFormat.format(cell.getNumericCellValue());
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().toString().trim();
            case HSSFCell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case HSSFCell.CELL_TYPE_ERROR:
                return Byte.toString(cell.getErrorCellValue());
            default:
                return "##POI## Unknown cell type";
            }

        }
    }

    public static void main(String[] args) throws Exception {
        SpRtTransformExcelToJsonPC test = new SpRtTransformExcelToJsonPC();

        final String fromRequestName = Joiner.on("_")
                .appendTo(new StringBuilder("file."), "eform", "table_namae", "column_name", 1).toString();

        final RowParser rowParser = RowParser.builder().build();

        File file = new File(test.getClass().getResource("blood.xls").getFile());
        HSSFWorkbook wb = test.getWorkBook(file);
        HSSFSheet sheet = wb.getSheetAt(0);

        final ImmutableList.Builder<JSONObject> jsonObjectIterableBuilder = ImmutableList.builder();
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            final HSSFRow row = sheet.getRow(rowNum);
            final JSONObject jsonObject = rowParser.parse(row);
            jsonObjectIterableBuilder.add(jsonObject);
        }

        final String jsonObjectJoinText = Joiner.on(";").join(jsonObjectIterableBuilder.build());

        System.out.println(Arrays.toString(ImmutableList.of(jsonObjectJoinText).toArray()));
    }

    private HSSFWorkbook getWorkBook(File file) throws Exception {
        final int size = (int) (file.length() - file.length() % 512);
        final byte[] buffer = new byte[size];

        final InputStream inputStream = new FileInputStream(file);
        try {
            inputStream.read(buffer, 0, size);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final InputStream byteIS = new ByteArrayInputStream(buffer);

        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            wb = new HSSFWorkbook(byteIS);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteIS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return wb;
    }

}
