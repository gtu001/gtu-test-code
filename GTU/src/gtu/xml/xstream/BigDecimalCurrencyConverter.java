package gtu.xml.xstream;

import java.math.BigDecimal;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.ebao.job_report.pub.util.format.CurrencyFormat;

public class BigDecimalCurrencyConverter implements Converter {

    private BigDecimal data;
    private String style;

    public BigDecimalCurrencyConverter(BigDecimal data, String style) {
        super();
        this.data = data;
        this.style = style;
    }

    public boolean canConvert(Class clazz) {
        return BigDecimal.class.isAssignableFrom(clazz);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        try {
            String dateshow = "";
            if (value instanceof java.math.BigDecimal) {
                data = (java.math.BigDecimal) value;
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