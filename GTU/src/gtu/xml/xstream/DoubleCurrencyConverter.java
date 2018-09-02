package gtu.xml.xstream;

import com.ebao.job_report.pub.util.format.CurrencyFormat;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DoubleCurrencyConverter implements Converter {

    private Double doubleData;
    private final String style;

    public DoubleCurrencyConverter(Double data, String style) {
        super();
        this.doubleData = data;
        this.style = style;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return Double.class.isAssignableFrom(clazz);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        try {
            String dateshow = "";
            if (value instanceof java.lang.Double) {
                doubleData = (java.lang.Double) value;
                dateshow = CurrencyFormat.format(doubleData, style);
            }
            writer.setValue(dateshow);
        } catch (Exception ce) {
            ce.printStackTrace();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        if (doubleData != null) {
            return doubleData;
        } else {
            return null;
        }
    }
}
