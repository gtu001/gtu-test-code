package gtu.jsf.tag.primeface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesPickList extends UIInput implements org.primefaces.component.api.Widget {

    static Logger log = LoggerFactory.getLogger(PropertiesPickList.class);

    public PropertiesPickList() {
        setRendererType("com.iisigroup.ris.ui.tag.PropertiesPickList");
    }

    public String getFamily() {
        return "com.iisigroup.ris.ui.tag";
    }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    String getMoveDownLabel() {
        return (String) getValue("moveDownLabel", "Move Down");
    }

    String getItemLabel() {
        return (String) getValue("itemLabel", "ITEM_LABEL"); //原為null
    }

    String getOnTransfer() {
        return (String) getValue("onTransfer", null);
    }

    String getLabel() {
        return (String) getValue("label", null);
    }

    String getMoveUpLabel() {
        return (String) getValue("moveUpLabel", "Move Up");
    }

    String getStyle() {
        return (String) getValue("style", null);
    }

    String getEffectSpeed() {
        return (String) getValue("effectSpeed", "fast");
    }

    String getMoveBottomLabel() {
        return (String) getValue("moveBottomLabel", "Move Bottom");
    }

    String getEffect() {
        return (String) getValue("effect", "fade");
    }

    String getAddLabel() {
        return (String) getValue("addLabel", "增加");
    }

    String getAddAllLabel() {
        return (String) getValue("addAllLabel", "增加全部");
    }

    String getRemoveLabel() {
        return (String) getValue("removeLabel", "移除");
    }

    String getRemoveAllLabel() {
        return (String) getValue("removeAllLabel", "移除全部");
    }

    String getWidgetVar() {
        return (String) getValue("widgetVar", null);
    }

    String getVar() {
        return (String) getValue("var", "var");//原來是null
    }

    String getMoveTopLabel() {
        return (String) getValue("moveTopLabel", "Move Top");
    }

    Object getItemValue() {
        return (Object) getValue("itemValue", "ITEM_VALUE"); //原為null
    }

    String getStyleClass() {
        return (String) getValue("styleClass", null);
    }

    boolean isShowTargetControls() {
        return (Boolean) getValue("showTargetControls", false);
    }

    boolean isShowSourceControls() {
        return (Boolean) getValue("showSourceControls", false);
    }

    boolean isDisabled() {
        return (Boolean) getValue("disabled", false);
    }

    boolean isItemDisabled() {
        return (Boolean) getValue("choiceOther", false);
    }

    void setOther(String other) {
        setValue("other", other);
    }

    String getOther() {
        return (String) getValue("other", null);
    }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    Object getValue(Serializable key, Object defaultValue) {
        return getStateHelper().eval(key, defaultValue);
    }

    void setValue(String key, Object value) {
        getStateHelper().put(key, value);
        handleAttribute(key, value);
    }

    public String resolveWidgetVar() {
        FacesContext context = FacesContext.getCurrentInstance();
        String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null)
            return userWidgetVar;
        else
            return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    void handleAttribute(String name, Object value) {
        List<String> setAttributes = (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            String cname = this.getClass().getName();
            if (cname != null && cname.startsWith("com.iisigroup.ris.")) {
                setAttributes = new ArrayList<String>(6);
                this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }
        if (setAttributes != null) {
            if (value == null) {
                ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                } else if (!setAttributes.contains(name)) {
                    setAttributes.add(name);
                }
            }
        }
    }
}