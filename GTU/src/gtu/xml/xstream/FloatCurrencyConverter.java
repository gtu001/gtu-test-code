package gtu.xml.xstream;

import java.text.NumberFormat;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class FloatCurrencyConverter implements Converter {

    private Float data;
    private String style;

    public FloatCurrencyConverter(Float data, String style) {
        super();
        this.data = data;
        this.style = style;
    }

    public boolean canConvert(Class clazz) {
        return Float.class.isAssignableFrom(clazz);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        try {
            String dateshow = "";
            if (value instanceof java.lang.Float) {
                data = (java.lang.Float) value;
                dateshow = CurrencyFormat.format(data, style);
            }
            writer.setValue(dateshow);
        } catch (Exception ce) {
            ce.printStackTrace();
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        if (data != null) {
            return data;
        } else {
            return null;
        }
    }
}