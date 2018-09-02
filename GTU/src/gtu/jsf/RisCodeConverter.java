package gtu.jsf;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.jsf.FacesContextUtils;

import com.iisigroup.ris.ae.codetable.RisCodeComponent;

@FacesConverter("com.iisigroup.ris.ae.ui.converter.RisCodeConverter")
public class RisCodeConverter implements Converter, Serializable {

    private static final long serialVersionUID = 6175175496318319107L;

    private String category;

    private boolean showCode;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {

        if (StringUtils.isBlank(submittedValue)) {
            return null;
        }

        RisCodeComponent risCodeComponent = FacesContextUtils.getWebApplicationContext(facesContext).getBean(
                RisCodeComponent.class);

        return risCodeComponent.getReferenceCodeByWorld(category, submittedValue.trim());
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {

        if (value == null) {
            return "";
        }

        RisCodeComponent risCodeComponent = FacesContextUtils.getWebApplicationContext(facesContext).getBean(
                RisCodeComponent.class);

        String code = ((String) value).trim();

        if (StringUtils.isBlank(code)) {
            return "";
        }

        String word = risCodeComponent.getReferenceWorldByCode(category, code);

        if (showCode) {
            return String.format("%s-%s", code, word);
        } else {
            return word;
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setShowCode(boolean showCode) {
        this.showCode = showCode;
    }

}
