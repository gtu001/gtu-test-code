package gtu.jsf.tag.primeface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.primefaces.component.column.Column;
import org.primefaces.model.DualListModel;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class PropertiesPickListRenderer extends CoreRenderer implements Serializable {
    private static final long serialVersionUID = 8196434099482342877L;

    static Logger log = LoggerFactory.getLogger(PropertiesPickListRenderer.class);

    final static File operationFile = new File("C:/workspace/backup/ris3ape/ris3ape-risfaces/src/main/java/com/iisigroup/ris/ae/ui/tag/proppicklist/operationFile.properties");
    final static Properties operationProps;

    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(operationFile));
        } catch (Exception ex) {
            log.error("load operation error!!", ex);
        }
        operationProps = props;
    }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx

    private static final String CONTAINER_CLASS = "ui-picklist ui-widget";
    private static final String LIST_CLASS = "ui-widget-content ui-picklist-list";
    private static final String SOURCE_CLASS = LIST_CLASS + " ui-picklist-source";
    private static final String TARGET_CLASS = LIST_CLASS + " ui-picklist-target";
    private static final String SOURCE_CONTROLS = "ui-picklist-source-controls";
    private static final String TARGET_CONTROLS = "ui-picklist-target-controls";
    private static final String ITEM_CLASS = "ui-picklist-item ui-corner-all";
    private static final String ITEM_DISABLED_CLASS = "ui-state-disabled";
    private static final String CAPTION_CLASS = "ui-picklist-caption ui-widget-header ui-corner-tl ui-corner-tr";
    private static final String ADD_BUTTON_CLASS = "ui-picklist-button-add";
    private static final String ADD_ALL_BUTTON_CLASS = "ui-picklist-button-add-all";
    private static final String REMOVE_BUTTON_CLASS = "ui-picklist-button-remove";
    private static final String REMOVE_ALL_BUTTON_CLASS = "ui-picklist-button-remove-all";
    private static final String ADD_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-e";
    private static final String ADD_ALL_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-e";
    private static final String REMOVE_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-w";
    private static final String REMOVE_ALL_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-w";
    private static final String MOVE_UP_BUTTON_CLASS = "ui-picklist-button-move-up";
    private static final String MOVE_DOWN_BUTTON_CLASS = "ui-picklist-button-move-down";
    private static final String MOVE_TOP_BUTTON_CLASS = "ui-picklist-button-move-top";
    private static final String MOVE_BOTTOM_BUTTON_CLASS = "ui-picklist-button-move-bottom";
    private static final String MOVE_UP_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-n";
    private static final String MOVE_DOWN_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-s";
    private static final String MOVE_TOP_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-n";
    private static final String MOVE_BOTTOM_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-s";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        PropertiesPickList pickList = (PropertiesPickList) component;
        String clientId = pickList.getClientId(context);
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        String sourceParam = clientId + "_source";
        String targetParam = clientId + "_target";
        if (params.containsKey(sourceParam) && params.containsKey(targetParam)) {
            pickList.setSubmittedValue(new String[] { params.get(sourceParam), params.get(targetParam) });
        }
    }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        PropertiesPickList pickList = (PropertiesPickList) component;

        encodeMarkup(facesContext, pickList);
        encodeScript(facesContext, pickList);
    }

    private static final RisCode OTHER_CODE;
    static {
        RisCode code = new RisCode();
        code.setCode("999");
        code.setName("其他");
        OTHER_CODE = code;
    }

    protected void encodeMarkup(FacesContext context, PropertiesPickList pickList) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = pickList.getClientId(context);

        ApplicationContext context1 = this.getApplicationContext(context);
        ReferenceMappingService referenceService = context1.getBean(ReferenceMappingService.class);

        List<RisCode> codeList = new ArrayList<RisCode>();
        List<RisCode> tmp = null;
        Set<String> categorySet = referenceService.getAllReferenceCate();

        //判斷屬性 operationId 是否有設定指定的作業代碼, 未設定則抓頁面取得前面編號作為作業代碼 XXX
        String operationId = (String) getValueExpression("operationId", pickList, context);
        if (StringUtils.isEmpty(operationId)) {
            operationId = getOperationId(context);
            log.debug("auto operationId : {}", operationId);
        }

        for (Object key : operationProps.keySet()) {
            if (!operationId.equalsIgnoreCase((String) key)) {
                continue;
            }
            for (String category : Arrays.asList(StringUtils.defaultString(operationProps.getProperty((String) key)).split(","))) {
                if (categorySet.contains(category)) {
                    tmp = referenceService.getAllReferenceMapping().get(category);
                    if (tmp != null) {
                        codeList.addAll(tmp);
                    }
                }
            }
            for (int ii = 0; ii < codeList.size(); ii++) {
                String value = codeList.get(ii).getName();
                int pos1 = value.indexOf("其");
                int pos2 = value.indexOf("它");
                if (pos2 == -1) {
                    pos2 = value.indexOf("他");
                }
                if (pos1 != -1 && pos2 != -1 && pos1 < pos2) {
                    codeList.remove(ii);
                    ii--;
                }
            }
        }

        if (codeList.isEmpty()) {
            this.errorMessage(operationFile.getName() + "檔案, 無此作業設定!! = " + operationId, context);
            log.error("not found in operationProps = {}", operationProps);
        }

        pickList.getAttributes().put("codeList", codeList);

        //判斷屬性 addOther 是否要加"其他"選項, 預設為true XXX
        boolean addOther = true;
        Object obj = getValueExpression("addOther", pickList, context);
        if (obj != null && obj instanceof String) {
            addOther = Boolean.parseBoolean((String) obj);
        }
        if (obj != null && obj instanceof Boolean) {
            addOther = (Boolean) obj;
        }
        if (addOther) {
            codeList.add(OTHER_CODE);
        }

        DualListModel<RisCode> model = new DualListModel<RisCode>();
        model.setSource(codeList);

        String styleClass = pickList.getStyleClass();
        styleClass = styleClass == null ? CONTAINER_CLASS : CONTAINER_CLASS + " " + styleClass;

        writer.startElement("table", pickList);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute("style", StringUtils.defaultString((String) getValueExpression("style", pickList, context), "width:40%"), null);
        writer.startElement("tbody", null);
        writer.startElement("tr", null);

        //Target List Reorder Buttons
        if (pickList.isShowSourceControls()) {
            encodeListControls(context, pickList, SOURCE_CONTROLS);
        }

        //Source List
        encodeList(context, pickList, clientId + "_source", SOURCE_CLASS, model.getSource(), pickList.getFacet("sourceCaption"));

        //Buttons
        writer.startElement("td", null);
        encodeButton(context, pickList.getAddLabel(), ADD_BUTTON_CLASS, ADD_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getAddAllLabel(), ADD_ALL_BUTTON_CLASS, ADD_ALL_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getRemoveLabel(), REMOVE_BUTTON_CLASS, REMOVE_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getRemoveAllLabel(), REMOVE_ALL_BUTTON_CLASS, REMOVE_ALL_BUTTON_ICON_CLASS);
        writer.endElement("td");

        //Target List
        encodeList(context, pickList, clientId + "_target", TARGET_CLASS, model.getTarget(), pickList.getFacet("targetCaption"));

        //Target List Reorder Buttons
        if (pickList.isShowTargetControls()) {
            encodeListControls(context, pickList, TARGET_CONTROLS);
        }

        writer.endElement("tr");
        writer.endElement("tbody");

        writer.endElement("table");

        writer.startElement("input", pickList);
        writer.writeAttribute("id", clientId + "_depend_text", "id");
        writer.writeAttribute("name", clientId + "_depend_text", "name");
        writer.writeAttribute("type", "hidden", "type");
        writer.endElement("input");
    }

    Object getValueExpression(String attrName, UIComponent commponent, FacesContext context) {
        Object object = null;
        try {
            log.trace("# ValueExpression ..");
            ValueExpression valExp = commponent.getValueExpression(attrName);
            log.trace("expectedType = {}", valExp.getExpectedType());
            log.trace("expressionString = {}", valExp.getExpressionString());
            log.trace("type = {}", valExp.getType(context.getELContext()));
            log.trace("valueReference = {}", valExp.getValueReference(context.getELContext()));
            log.trace("value = {}", object = valExp.getValue(context.getELContext()));
            log.trace("literalText = {}", valExp.isLiteralText());
            log.trace("readOnly = {}", valExp.isReadOnly(context.getELContext()));
        } catch (Exception ex) {
            log.trace("(1)get attr : " + attrName + ", error!!");
        }
        if (object != null) {
            return object;
        }
        try {
            log.trace("# ValueBinding ..");
            ValueBinding bind = commponent.getValueBinding(attrName);
            log.trace("expressionString = {}", bind.getExpressionString());
            log.trace("type = {}", bind.getType(context));
            log.trace("value = {}", object = bind.getValue(context));
            log.trace("readOnly = {}", bind.isReadOnly(context));
        } catch (Exception ex) {
            log.trace("(2)get attr : " + attrName + ", error!!");
        }
        return object;
    }

    WebApplicationContext getApplicationContext(FacesContext context) {
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        ServletContext scontext = session.getServletContext();
        WebApplicationContext webContext = (WebApplicationContext) scontext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        return webContext;
    }

    static Pattern fetchOperationIdPattern = Pattern.compile("^(\\w+)_?\\w*\\.xhtml$");

    String getOperationId(FacesContext context) {
        String viewId = context.getViewRoot().getViewId();
        viewId = new File(viewId).getName();
        Matcher matcher = fetchOperationIdPattern.matcher(viewId);
        matcher.find();
        viewId = matcher.group(1).toUpperCase();
        return viewId;
    }

    protected void encodeScript(FacesContext context, PropertiesPickList pickList) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = pickList.getClientId(context);

        startScript(writer, clientId);

        writer.write("PrimeFaces.cw('PickList','" + pickList.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");
        writer.write(",effect:'" + pickList.getEffect() + "'");
        writer.write(",effectSpeed:'" + pickList.getEffectSpeed() + "'");

        if (pickList.isShowSourceControls())
            writer.write(",showSourceControls:true");
        if (pickList.isShowTargetControls())
            writer.write(",showTargetControls:true");
        if (pickList.isDisabled())
            writer.write(",disabled:true");
        if (pickList.getOnTransfer() != null)
            writer.write((",onTransfer:function(e) {" + pickList.getOnTransfer() + ";}"));

        writer.write("});\n");
        writer.write(String.format(jscript, clientId));
        writer.write(String.format("%s_setup({id:'%s',effect:'%s',effectSpeed:'%s'});", clientId, clientId, pickList.getEffect(), pickList.getEffectSpeed()));
        endScript(writer);
    }

    final static String jscript = getJs();

    protected void encodeListControls(FacesContext context, PropertiesPickList pickList, String styleClass) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("td", null);
        writer.writeAttribute("class", styleClass, null);
        encodeButton(context, pickList.getMoveUpLabel(), MOVE_UP_BUTTON_CLASS, MOVE_UP_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getMoveTopLabel(), MOVE_TOP_BUTTON_CLASS, MOVE_TOP_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getMoveDownLabel(), MOVE_DOWN_BUTTON_CLASS, MOVE_DOWN_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getMoveBottomLabel(), MOVE_BOTTOM_BUTTON_CLASS, MOVE_BOTTOM_BUTTON_ICON_CLASS);
        writer.endElement("td");
    }

    protected void encodeCaption(FacesContext context, UIComponent caption) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", CAPTION_CLASS, null);
        caption.encodeAll(context);
        writer.endElement("div");
    }

    protected void encodeButton(FacesContext context, String title, String styleClass, String icon) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("button", null);
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", HTML.BUTTON_ICON_ONLY_BUTTON_CLASS + " " + styleClass, null);
        writer.writeAttribute("title", title, null);

        //icon
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_LEFT_ICON_CLASS + " " + icon, null);
        writer.endElement("span");

        //text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);
        writer.write("ui-button");
        writer.endElement("span");

        writer.endElement("button");
    }

    protected void encodeList(FacesContext context, PropertiesPickList pickList, String listId, String styleClass, List model, UIComponent caption) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("td", null);

        if (caption != null) {
            encodeCaption(context, caption);
            styleClass += " ui-corner-bottom";
        } else {
            styleClass += " ui-corner-all";
        }

        writer.startElement("ul", null);
        writer.writeAttribute("class", styleClass, null);

        String values = encodeOptions(context, pickList, model);

        writer.endElement("ul");

        encodeListStateHolder(context, listId, values);

        writer.endElement("td");
    }

    @SuppressWarnings("unchecked")
    protected String encodeOptions(FacesContext context, PropertiesPickList pickList, List<RisCode> model) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String var = pickList.getVar();
        StringBuilder builder = new StringBuilder();

        for (Iterator<RisCode> it = model.iterator(); it.hasNext();) {
            RisCode item = it.next();
            context.getExternalContext().getRequestMap().put(var, item);
            String value = item == null ? "" : item.getCode();
            String label = item == null ? "" : item.getName();
            String itemClass = pickList.isItemDisabled() ? ITEM_CLASS + " " + ITEM_DISABLED_CLASS : ITEM_CLASS;

            writer.startElement("li", null);
            writer.writeAttribute("class", itemClass, null);
            writer.writeAttribute("data-item-value", value, null); // OPTION_VALUE

            if (pickList.getChildCount() > 0) {
                writer.startElement("table", null);
                writer.startElement("tbody", null);
                writer.startElement("tr", null);

                for (UIComponent kid : pickList.getChildren()) {
                    if (kid instanceof Column && kid.isRendered()) {
                        Column column = (Column) kid;

                        writer.startElement("td", null);
                        if (column.getStyle() != null)
                            writer.writeAttribute("style", column.getStyle(), null);
                        if (column.getStyleClass() != null)
                            writer.writeAttribute("class", column.getStyleClass(), null);

                        kid.encodeAll(context);
                        writer.endElement("td");
                    }
                }

                writer.endElement("tr");
                writer.endElement("tbody");
                writer.endElement("table");
            } else {
                writer.writeText(label + "(" + value + ")", null);//OPTION_LABEL
            }

            writer.endElement("li");

            builder.append("\"").append(value).append("\"");

            if (it.hasNext()) {
                builder.append(",");
            }
        }

        context.getExternalContext().getRequestMap().remove(var);

        return builder.toString();
    }

    protected void encodeListStateHolder(FacesContext context, String clientId, String values) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("value", values, null);
        writer.endElement("input");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        try {
            PropertiesPickList pickList = (PropertiesPickList) component;
            String[] value = (String[]) submittedValue;
            String[] sourceValue = value[0].split(",");
            String[] targetValue = value[1].split(",");
            DualListModel<?> model = new DualListModel();

            String otherVal = context.getExternalContext().getRequestParameterMap().get(component.getClientId(context) + "_depend_text");

            List<RisCode> codeList = (List<RisCode>) pickList.getAttributes().get("codeList");

            doConvertValue(context, pickList, sourceValue, model.getSource());
            doConvertValue(context, pickList, targetValue, model.getTarget());

            Map<String, String> rtnMap = new HashMap<String, String>();
            for (Object val : model.getTarget()) {
                if (val.equals(OTHER_CODE.getCode())) {
                    rtnMap.put(OTHER_CODE.getCode(), otherVal);
                    continue;
                }
                boolean findOk = false;
                for (RisCode code : codeList) {
                    if (code.getCode().equals(val)) {
                        rtnMap.put(code.getCode(), code.getName());
                        findOk = true;
                    }
                }
                if (!findOk) {
                    errorMessage("code : " + val + " not found!!", context);
                }
            }
            return rtnMap;
        } catch (Exception exception) {
            log.error("err", exception);
            throw new ConverterException(exception);
        }
    }

    @SuppressWarnings("unchecked")
    protected void doConvertValue(FacesContext context, PropertiesPickList pickList, String[] values, List model) {
        Converter converter = pickList.getConverter();

        for (String item : values) {
            if (isValueBlank(item))
                continue;

            //trim whitespaces and double quotes
            String val = item.trim();
            val = val.substring(1, val.length() - 1);

            Object convertedValue = converter != null ? converter.getAsObject(context, pickList, val) : val;

            if (convertedValue != null)
                model.add(convertedValue);
        }
    }

    void errorMessage(String message, FacesContext context) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "錯誤", message));
        log.error(message);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    static String getJs() {
        StringBuilder sb = new StringBuilder();
        sb.append("            function %s_setup(cfg) {                                                                                                     \n");
        sb.append("                try {                                                                                                                    \n");
        sb.append("                    var id = cfg.id;                                                                                                     \n");
        sb.append("                    var jqId = PrimeFaces.escapeClientId(id);                                                                            \n");
        sb.append("                    var jq = $(jqId);                                                                                                    \n");
        sb.append("                    var sourceList = jq.find('.ui-picklist-source');                                                                     \n");
        sb.append("                    var targetList = jq.find('.ui-picklist-target');                                                                     \n");
        sb.append("                    var sourceState = $(jqId + '_source');                                                                               \n");
        sb.append("                    var targetState = $(jqId + '_target');                                                                               \n");
        sb.append("                    var items = jq.find('.ui-picklist-item:not(.ui-state-disabled)');                                                    \n");
        sb.append("                    var other = $(PrimeFaces.escapeClientId(id + \"_depend_text\"));                                                     \n");
        sb.append("                    var otherCheck = function(select, isRemove){                                                                         \n");
        sb.append("                     if(select == undefined || select.attr(\"data-item-value\") == undefined || select.attr(\"data-item-value\") == ''){ \n");
        sb.append("                         return false;                                                                                               \n");
        sb.append("                     }                                                                                                                   \n");
        sb.append("                     if(select.attr(\"data-item-value\") == \"999\" && !isRemove){                                               \n");
        sb.append("                         var str = prompt(\"請輸入[其他]代碼\", \"\");                                                               \n");
        sb.append("                         if(str == \"\"){                                                                                            \n");
        sb.append("                             return false;                                                                                       \n");
        sb.append("                         }else{                                                                                                      \n");
        sb.append("                             other.val(str);                                                                                     \n");
        sb.append("                         }                                                                                                           \n");
        sb.append("                     }                                                                                                                   \n");
        sb.append("                     if(select.attr(\"data-item-value\") == \"999\" && isRemove){                                                \n");
        sb.append("                         other.val('');                                                                                              \n");
        sb.append("                     }                                                                                                                   \n");
        sb.append("                     if(isRemove){                                                                                                       \n");
        sb.append("                         transfer(select, targetList, sourceList, 'command');                                                        \n");
        sb.append("                     }else{                                                                                                              \n");
        sb.append("                         transfer(select, sourceList, targetList, 'command');                                                        \n");
        sb.append("                     }                                                                                                                   \n");
        sb.append("                    }                                                                                                                    \n");
        sb.append("                 var addEvent = function(){                                                                          \n");
        sb.append("                     sourceList.children('li.ui-picklist-item.ui-state-highlight')                                       \n");
        sb.append("                         .removeClass('ui-state-highlight')                                                          \n");
        sb.append("                         .hide(cfg.effect, {}, cfg.effectSpeed, function() {                                         \n");
        sb.append("                             otherCheck($(this), false);                                                         \n");
        sb.append("                         });                                                                                     \n");
        sb.append("                 }                                                                                                           \n");
        sb.append("                 var addAllEvent = function() {                                                                      \n");
        sb.append("                     sourceList.children('li.ui-picklist-item:not(.ui-state-disabled)')                              \n");
        sb.append("                         .removeClass('ui-state-highlight')                                                          \n");
        sb.append("                         .hide(cfg.effect, {}, cfg.effectSpeed, function() {                                         \n");
        sb.append("                             otherCheck($(this), false);                                                         \n");
        sb.append("                         });                                                                                         \n");
        sb.append("                 };                                                                                                  \n");
        sb.append("                 var removeEvent = function() {                                                                      \n");
        sb.append("                     targetList.children('li.ui-picklist-item.ui-state-highlight')                                       \n");
        sb.append("                         .removeClass('ui-state-highlight')                                                          \n");
        sb.append("                         .hide(cfg.effect, {}, cfg.effectSpeed, function() {                                         \n");
        sb.append("                             otherCheck($(this), true);                                                          \n");
        sb.append("                         });                                                                                     \n");
        sb.append("                 }                                                                                                   \n");
        sb.append("                 var removeAllEvent = function() {                                                                   \n");
        sb.append("                     targetList.children('li.ui-picklist-item:not(.ui-state-disabled)')                                  \n");
        sb.append("                     .removeClass('ui-state-highlight')                                                                  \n");
        sb.append("                     .hide(cfg.effect, {}, cfg.effectSpeed, function() {                                                 \n");
        sb.append("                         otherCheck($(this), true);                                                                  \n");
        sb.append("                     });                                                                                             \n");
        sb.append("                 }                                                                                                   \n");
        sb.append("                    $(jqId + ' .ui-picklist-button-add').unbind(\"click\").click(addEvent);                                              \n");
        sb.append("                 $(jqId + ' .ui-picklist-button-add-all').unbind(\"click\").click(addAllEvent);                      \n");
        sb.append("                 $(jqId + ' .ui-picklist-button-remove').unbind(\"click\").click(removeEvent);                       \n");
        sb.append("                 $(jqId + ' .ui-picklist-button-remove-all').unbind(\"click\").click(removeAllEvent);                \n");
        sb.append("                    var transfer = function(item, from, to, type) {                                                                      \n");
        sb.append("                        item.appendTo(to).removeClass('ui-state-highlight')                                                              \n");
        sb.append("                                .show('fade', {}, 'fast', function() {                                                                   \n");
        sb.append("                                    saveState();                                                                                         \n");
        sb.append("                                })                                                                                                       \n");
        sb.append("                    };                                                                                                                   \n");
        sb.append("                    var saveState = function() {                                                                                         \n");
        sb.append("                        saveListState(sourceList, sourceState, \"source\");                                                              \n");
        sb.append("                        saveListState(targetList, targetState, \"target\");                                                              \n");
        sb.append("                    };                                                                                                                   \n");
        sb.append("                    var saveListState = function(list, holder, dest) {                                                                   \n");
        sb.append("                        var values = [];                                                                                                 \n");
        sb.append("                        $(list).children('li.ui-picklist-item').each(function() {                                                        \n");
        sb.append("                         var val = $(this).data('item-value');                                                                       \n");
        sb.append("                         if(val == undefined || val == ''){                                                                          \n");
        sb.append("                             return false;                                                                                       \n");
        sb.append("                         }                                                                                                           \n");
        sb.append("                            values.push('\"' + $(this).data('item-value') + '\"');                                                       \n");
        sb.append("                        });                                                                                                              \n");
        sb.append("                        holder.val(values.join(','));                                                                                    \n");
        sb.append("                    };                                                                                                                   \n");
        sb.append("                } catch (err) {                                                                                                          \n");
        sb.append("                }                                                                                                                        \n");
        sb.append("            }                                                                                                                            \n");
        return sb.toString();
    }
}