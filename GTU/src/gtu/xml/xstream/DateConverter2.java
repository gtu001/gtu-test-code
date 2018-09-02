package gtu.xml.xstream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DateConverter2 implements Converter {

    private String dateFromat;

    public DateConverter2(String dateFromat) {
        super();
        this.dateFromat = dateFromat;
    }

    public boolean canConvert(Class cls) {
        return cls.equals(Date.class);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        DateFormat formatter = new SimpleDateFormat(dateFromat);
        writer.setValue(formatter.format((Date) source));

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        DateFormat formatter = new SimpleDateFormat(dateFromat);
        try {
            String value = reader.getValue();
            if (StringUtils.isNotBlank(value)) {
                return formatter.parse(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
