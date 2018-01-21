package gtu.jsf;

import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesValidator("personNameValidator")
public class PersonNameValidator implements javax.faces.validator.Validator {

    private static Logger log = LoggerFactory.getLogger(PersonNameValidator.class);
    private static final String REQUEST_GROUP_KEY = PersonNameValidator.class.getName();

    private static final String CONFIG_ERROR_MSG = "id不mapping,請設定[全名id=*_full\\d+],[姓id=*_frist\\d+],[名=*_last\\d+],結尾數字設定則必須一致";
    private static FacesMessage INVALID_MSG = new FacesMessage(FacesMessage.SEVERITY_ERROR, "錯誤", "姓名欄位不符!");
    private static ValidatorException INVALID_EXCEPTION = new ValidatorException(INVALID_MSG);

    enum UIType {
        FULL("full", "^(\\w*):?(\\d*):?(\\w*)_(full)(\\d*)$"), //
        FRIST("frist", "^(\\w*):?(\\d*):?(\\w*)_(frist)(\\d*)$"), //
        LAST("last", "^(\\w*):?(\\d*):?(\\w*)_(last)(\\d*)$")//
        ;

        final String val;
        final Pattern pattern;

        UIType(String val, String format) {
            this.val = val;
            pattern = Pattern.compile(format);
        }

        static UI filter(FacesContext context, UIComponent component, UIType uiType, Object value) {
            String clientId = component.getClientId(context);
            Matcher matcher = uiType.pattern.matcher(clientId);
            if (matcher.find()) {
                return new UI(matcher, component, value);
            }
            return null;
        }

        static UIType getUIType(String val) {
            for (UIType u : UIType.values()) {
                if (u.val.equals(val)) {
                    return u;
                }
            }
            return null;
        }
    }

    static class UI {
        String prefix;
        String tableIndex;
        String name;
        String rearIndex;
        UIType uiType;
        Object value;
        UIComponent component;
        String clientId;

        public UI(Matcher matcher, UIComponent component, Object value) {
            super();
            prefix = matcher.group(1);
            tableIndex = matcher.group(2);
            name = matcher.group(3);
            uiType = UIType.getUIType(matcher.group(4));
            rearIndex = matcher.group(5);
            this.component = component;
            clientId = component.getClientId();
            if (value != null) {
                this.value = value;
            } else if (component instanceof UIOutput) {
                this.value = ((UIOutput) component).getValue();
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((rearIndex == null) ? 0 : rearIndex.hashCode());
            result = prime * result + ((tableIndex == null) ? 0 : tableIndex.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            UI other = (UI) obj;
            if (rearIndex == null) {
                if (other.rearIndex != null)
                    return false;
            } else if (!rearIndex.equals(other.rearIndex))
                return false;
            if (tableIndex == null) {
                if (other.tableIndex != null)
                    return false;
            } else if (!tableIndex.equals(other.tableIndex))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "UI [prefix=" + prefix + ", tableIndex=" + tableIndex + ", name=" + name + ", rearIndex=" + rearIndex + ", uiType=" + uiType + "]";
        }
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object paramObject) throws ValidatorException {
        if (context == null || component == null) {
            return;
        }

        UI ui = null;
        for (UIType uiType : UIType.values()) {
            ui = UIType.filter(context, component, uiType, paramObject);
            if (ui != null) {
                break;
            }
        }

        if (ui == null) {
            log.error(CONFIG_ERROR_MSG + "(0)");
            return;
        }

        EnumMap<UIType, UI> enumMap = new EnumMap<UIType, UI>(UIType.class);
        enumMap.put(ui.uiType, ui);

        UIComponent namingContainer = component.getNamingContainer();
        for (UIType uiType : UIType.values()) {
            if (!enumMap.containsKey(uiType)) {
                this.findComponent(uiType, namingContainer, enumMap, context);
            }
        }

        if (enumMap.size() != 3) {
            log.error(CONFIG_ERROR_MSG + "(1)");
            return;
        }

        UIGroup uiGroup = this.getReferenceGroup(new UIGroup(enumMap, context), context);
        uiGroup.setUIReady(ui.uiType);

        if (!uiGroup.isUIGroupReady()) {
            log.debug("# ui group is not ready!!");
            return;
        }

        boolean result = false;
        String message = null;
        try {
            String full = getComponentStringValue(enumMap.get(UIType.FULL));
            String frist = getComponentStringValue(enumMap.get(UIType.FRIST));
            String last = getComponentStringValue(enumMap.get(UIType.LAST));
            message = String.format("personName validate {0}! = [full name:%s, frist name:%s, last name:%s]", full, frist, last);
            if (full.equals(frist.concat(last))) {
                result = true;
            }
        } catch (Exception ex) {
            log.error("cast UIOutput error!", ex);
        }

        message = MessageFormat.format(message, result ? "CORRECT" : "INVALID");
        log.error(message);

        if (!result) {
            //            context.addMessage(null, INVALID_MSG);
            throw INVALID_EXCEPTION;
        }

        this.removeReferenceGroup(uiGroup, context);
    }

    @SuppressWarnings("unchecked")
    UIGroup getReferenceGroup(UIGroup group, FacesContext context) {
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Set<UIGroup> set = new HashSet<UIGroup>();
        if (request.getAttribute(REQUEST_GROUP_KEY) != null) {
            set = (Set<UIGroup>) request.getAttribute(REQUEST_GROUP_KEY);
        } else {
            request.setAttribute(REQUEST_GROUP_KEY, set);
        }
        if (set.contains(group)) {
            for (UIGroup g : set) {
                if (g.equals(group)) {
                    return g;
                }
            }
        }
        set.add(group);
        return group;
    }

    @SuppressWarnings("unchecked")
    void removeReferenceGroup(UIGroup group, FacesContext context) {
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Set<UIGroup> set = new HashSet<UIGroup>();
        if (request.getAttribute(REQUEST_GROUP_KEY) != null) {
            set = (Set<UIGroup>) request.getAttribute(REQUEST_GROUP_KEY);
        } else {
            request.setAttribute(REQUEST_GROUP_KEY, set);
        }
        if (set.contains(group)) {
            set.remove(group);
        }
    }

    static class UIGroup {
        UI frist;
        UI full;
        UI last;

        String viewId;

        boolean fristReady;
        boolean fullReady;
        boolean lastReady;

        boolean isUIGroupReady() {
            return fristReady && fullReady && lastReady;
        }

        UIGroup(EnumMap<UIType, UI> enumMap, FacesContext context) {
            viewId = context.getViewRoot().getViewId();
            frist = enumMap.get(UIType.FRIST);
            last = enumMap.get(UIType.LAST);
            full = enumMap.get(UIType.FULL);
        }

        void setUIReady(UIType ready) {
            if (ready == UIType.FRIST) {
                fristReady = true;
            }
            if (ready == UIType.LAST) {
                lastReady = true;
            }
            if (ready == UIType.FULL) {
                fullReady = true;
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((frist.clientId == null) ? 0 : frist.clientId.hashCode());
            result = prime * result + ((full.clientId == null) ? 0 : full.clientId.hashCode());
            result = prime * result + ((last.clientId == null) ? 0 : last.clientId.hashCode());
            result = prime * result + ((viewId == null) ? 0 : viewId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            UIGroup other = (UIGroup) obj;
            if (frist.clientId == null) {
                if (other.frist.clientId != null)
                    return false;
            } else if (!frist.clientId.equals(other.frist.clientId))
                return false;
            if (full.clientId == null) {
                if (other.full.clientId != null)
                    return false;
            } else if (!full.clientId.equals(other.full.clientId))
                return false;
            if (last.clientId == null) {
                if (other.last.clientId != null)
                    return false;
            } else if (!last.clientId.equals(other.last.clientId))
                return false;
            if (viewId == null) {
                if (other.viewId != null)
                    return false;
            } else if (!viewId.equals(other.viewId))
                return false;
            return true;
        }
    }

    String getComponentStringValue(UI ui) {
        return StringUtils.defaultString((String) this.getComponentValue(ui));
    }

    Object getComponentValue(UI ui) {
        Object value = ui.value;
        if (value != null) {
            return value;
        }
        if (ui.component instanceof UIOutput) {
            return ((UIOutput) ui.component).getValue();
        }
        return null;
    }

    void findComponent(UIType uiType, UIComponent baseUi, EnumMap<UIType, UI> enumMap, FacesContext context) {
        if (enumMap.size() == 3) {
            return;
        }
        UI findUi = null;
        if (baseUi == null) {
            return;
        }
        UI compare = enumMap.values().iterator().next();
        if (baseUi.getChildren() != null) {
            for (UIComponent ui : baseUi.getChildren()) {
                findUi = UIType.filter(context, ui, uiType, null);
                if (findUi != null && compare.equals(findUi)) {
                    enumMap.put(findUi.uiType, findUi);
                    return;
                }
                findComponent(uiType, ui, enumMap, context);
            }
        }
    }
}
