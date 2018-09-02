package gtu.jsf;

import javax.faces.context.FacesContext;

/**
 * 
 * @author Administrator 2009/02/02
 * 
 */
public class ApplicationInfo {

    /**
     * 取得FacesContext.getCurrentInstance().getExternalContext().
     * getApplicationMap().containsKey(name)
     * 
     * @param name
     * @return 
     *         FacesContext.getCurrentInstance().getExternalContext().getApplicationMap
     *         ().containsKey(name);
     */
    public static boolean contains(String name) {
        // logger.debug("ApplicationInfo.contains.name:" + name);
        //
        // logger.debug("FacesContext.getCurrentInstance():" +
        // FacesContext.getCurrentInstance());
        //
        // logger.debug("FacesContext.getCurrentInstance().getExternalContext():"
        // +
        // FacesContext.getCurrentInstance().getExternalContext());
        // logger.debug("FacesContext.getCurrentInstance().getExternalContext().getApplicationMap():"
        // +
        // FacesContext.getCurrentInstance().getExternalContext().getApplicationMap());
        //
        // logger.debug("FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().size():"
        // +
        // FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().size());
        return FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().containsKey(name);
    }

    /**
     * 取得FacesContext.getCurrentInstance().getExternalContext().
     * getApplicationMap().get(name);
     * 
     * @param name
     * @return 
     *         FacesContext.getCurrentInstance().getExternalContext().getApplicationMap
     *         ().get(name)
     */
    public static Object getObject(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(name);
    }

    /**
     * 取得(String)ApplicationInfo.getObject(name);
     * 
     * @param name
     * @return (String)ApplicationInfo.getObject(name);
     */
    public static String getString(String name) {
        return (String) getObject(name);
    }

    /**
     * 移除FacesContext.getCurrentInstance().getExternalContext().
     * getApplicationMap().remove(name)
     * 
     * @param name
     */
    public static void remove(String name) {
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().remove(name);
    }

    /**
     * 設定FacesContext.getCurrentInstance().getExternalContext().
     * getApplicationMap().put(name, value);
     * 
     * @param name
     * @param value
     */
    public static void setObject(String name, Object value) {
        // logger.debug("############################ brfore size():" +
        // FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().size());
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put(name, value);
        // logger.debug("############################ after size():" +
        // FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().size());
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
     * 取得FacesContext.getCurrentInstance().getApplication().getDefaultLocale()
     * 
     * @return 
     *         FacesContext.getCurrentInstance().getApplication().getDefaultLocale
     *         ()
     */
    public static String getDefaultLocale() {
        return FacesContext.getCurrentInstance().getApplication().getDefaultLocale().toString();
    }
}
