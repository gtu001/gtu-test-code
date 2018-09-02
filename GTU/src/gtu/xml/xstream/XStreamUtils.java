package gtu.xml.xstream;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamUtils {

    public static XStream getXStream() {
        XStream xStream = new XStream(new DomDriver("utf-8"));
        return xStream;
    }

    public static XStream getXStream(String dateFormat) {
        XStream xStream = new XStream(new DomDriver("utf-8"));
        if (StringUtils.isNotBlank(dateFormat)) {
            xStream.registerConverter(new DateConverter2(dateFormat));
        }
        return xStream;
    }
}
