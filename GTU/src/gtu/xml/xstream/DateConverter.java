package gtu.xml.xstream;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.ibm.icu.text.DateFormat;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DateConverter implements Converter {

    //判斷要轉換的類型
    @Override
    public boolean canConvert(Class clz) {
        return Date.class.isAssignableFrom(clz);
    }

    //編寫Java對象到Xml轉換邏輯
    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.getDefault());
        writer.setValue(formatter.format(value));
    }

    //編寫Xml到Java對象的轉換邏輯
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.getDefault());
        try {
            calendar.setTime(formatter.parse(reader.getValue()));
        } catch (Exception ex) {
            throw new ConversionException(ex.getMessage(), ex);
        }
        return calendar.getGregorianChange();
    }

}
