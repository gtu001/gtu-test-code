package gtu.jsf;

import javax.faces.context.FacesContext;

/**
 * @author Troy 2009/02/02
 * 
 */
public class RequestParameter {
    /**
     * 取得FacesContext.getCurrentInstance().getExternalContext().
     * getRequestParameterMap().containsKey(name)
     * 
     * @param name
     * @return FacesContext.getCurrentInstance().getExternalContext().
     *         getRequestParameterMap().containsKey(name)
     */
    public static boolean contains(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey(name);
    }

    /**
     * 取得FacesContext.getCurrentInstance().getExternalContext().
     * getRequestParameterMap().get(name)
     * 
     * @param name
     * @return FacesContext.getCurrentInstance().getExternalContext().
     *         getRequestParameterMap().get(name)
     */
    public static Object getObject(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    /**
     * 取得(String)RequestParameter.getObject(name)
     * 
     * @param name
     * @return (String)RequestParameter.getObject(name);
     */
    public static String getString(String name) {
        return (String) getObject(name);
    }

    /**
     * 移除FacesContext.getCurrentInstance().getExternalContext().
     * getRequestParameterMap().remove(name);
     * 
     * @param name
     */
    public static void remove(String name) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().remove(name);
    }
}
