package gtu.jsf;

import javax.faces.context.FacesContext;

/**
 * @author Troy 2009/02/02
 * 
 */
public final class SessionContext {

    /**
     * 取得FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
     * containsKey(name)
     * 
     * @param name
     * @return 
     *         FacesContext.getCurrentInstance().getExternalContext().getSessionMap
     *         ().containsKey(name);
     */
    public static boolean contains(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(name);
    }

    /**
     * 取得FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
     * get(name);
     * 
     * @param name
     * @return 
     *         FacesContext.getCurrentInstance().getExternalContext().getSessionMap
     *         ().get(name)
     */
    public static Object getObject(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(name);
    }

    /**
     * 取得(String)SessionContext.getObject(name);
     * 
     * @param name
     * @return (String)SessionContext.getObject(name);
     */
    public static String getString(String name) {
        return (String) getObject(name);
    }

    /**
     * 移除FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
     * remove(name)
     * 
     * @param name
     */
    public static void remove(String name) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(name);
    }

    /**
     * 設定FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
     * put(name, value);
     * 
     * @param name
     * @param value
     */
    public static void setObject(String name, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(name, value);
    }

    /**
     * 設定SessionContext.setObject(name, value);
     * 
     * @param name
     * @param value
     */
    public static void setString(String name, String value) {
        setObject(name, value);
    }

    /**
     * 取得 FacesContext.getCurrentInstance().getExternalContext().
     * getRequestContextPath()
     * 
     * @return FacesContext.getCurrentInstance().getExternalContext().
     *         getRequestContextPath();
     */
    public static String getContextPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

}
