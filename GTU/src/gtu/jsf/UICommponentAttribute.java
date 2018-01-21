package gtu.jsf;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UICommponentAttribute {

    static Logger log = LoggerFactory.getLogger(UICommponentAttribute.class);

    public static void main(String[] args) {
        System.out.println("done...");
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
            log.trace("ValueExpression get attr : " + attrName + ", error!!", ex);
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
            log.trace("ValueBinding get attr : " + attrName + ", error!!", ex);
        }
        return object;
    }
}
