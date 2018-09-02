package gtu.xml.xstream;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DataConverter implements Converter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (value instanceof Date) {
            writer.setValue(DATE_FORMAT.format((Date) value));
        } else {
            writer.setValue(value.toString());
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Class<?> type = context.getRequiredType();
        String value = reader.getValue();
        if ("".equals(value)) {
            if (String.class.equals(type)) {
                return "";
            } else {
                return null;
            }
        }
        if (Date.class.equals(type)) {
            try {
                return DATE_FORMAT.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (boolean.class.equals(type)) {
            if ("Y".equals(value)) {
                return true;
            }
        }
        if (Boolean.class.equals(type)) {
            if ("Y".equals(value)) {
                return Boolean.TRUE;
            }
        }
        try {
            return type.getConstructor(String.class).newInstance(value);
        } catch (Exception e) {
            throw new IllegalStateException(type.getName() + "(" + value + ")");
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class clazz) {
        return Date.class.equals(clazz) || String.class.equals(clazz) || Integer.class.equals(clazz) || Long.class.equals(clazz) || BigDecimal.class.equals(clazz) || Boolean.class.equals(clazz);
    }
}