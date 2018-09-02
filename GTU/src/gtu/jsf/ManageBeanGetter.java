package gtu.jsf;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

public class ManageBeanGetter {
    
    public static void main(String[] args){
        final HouseholdContextBean householdContextBean = getJsfBean("householdContextBean", HouseholdContextBean.class);
    }

    private static <T> T getJsfBean(final String beanName, final Class<T> clazz) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ELContext elContext = context.getELContext();
        final ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        final ValueExpression ve = expressionFactory.createValueExpression(elContext, "#{" + beanName + "}", Object.class);
        T bean = null;
        if (ve != null) {
            bean = (T) ve.getValue(elContext);
        }
        return bean;
    }
}
