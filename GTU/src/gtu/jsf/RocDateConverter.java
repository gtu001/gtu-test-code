package gtu.jsf;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;

import com.iisigroup.ris.util.DateUtil;

/**
 * 民國年日期 Converter.
 */
public class RocDateConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (StringUtils.isNotBlank(submittedValue)) {
            return DateUtil.parseTwyDateBySlash(submittedValue);
        } else {
            return null;
        }
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (null == value) {
            return DateUtil.formatDateAsTwyBySlash((Date) value);
        } else {
            return null;
        }
    }

}
