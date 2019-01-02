package gtu.properties;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import sun.misc.SoftCache;

public class LocaleUtil {
    private static Logger log = Logger.getLogger(LocaleUtil.class);
    private static boolean isDebug = log.isDebugEnabled();
    private static final String SCOPE_KEY = "javax.servlet.jsp.jstl.fmt.locale.session";
    private static boolean IS_HTTP_SUPPORT = false;

    static {
        if (isDebug) {
            log.debug("******LocaleUtil load properties");
        }
        try {
            Thread.currentThread().getContextClassLoader().loadClass("com.igsapp.web.ext.HttpRequestReference");
            IS_HTTP_SUPPORT = true;
        } catch (Throwable localThrowable) {
        }
    }

    public static Locale getLocale(String localeStr) {
        Locale[] supports = Locale.getAvailableLocales();
        Locale[] arrayOfLocale1;
        int j = (arrayOfLocale1 = supports).length;
        for (int i = 0; i < j; i++) {
            Locale locale = arrayOfLocale1[i];
            if (locale.toString().equals(localeStr)) {
                return locale;
            }
        }
        return Locale.getDefault();
    }

    public static Locale getCurrentLocale(HttpServletRequest req) {
        if (req == null) {
            return Locale.getDefault();
        }
        HttpSession session = req.getSession(false);
        if (session == null) {
            return req.getLocale();
        }
        Locale scopeLocale = (Locale) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale.session");
        if (scopeLocale == null) {
            scopeLocale = (Locale) session.getValue("javax.servlet.jsp.jstl.fmt.locale.session");
        }
        if (scopeLocale == null) {
            return req.getLocale();
        }
        return scopeLocale;
    }

    public static void clearCache() throws Exception {
        Field field = ResourceBundle.class.getDeclaredField("cacheList");
        field.setAccessible(true);
        Object cache = field.get(null);
        if (SoftCache.class.isAssignableFrom(cache.getClass())) {
            ((SoftCache) cache).clear();
        } else {
            ((ConcurrentHashMap) cache).clear();
        }
    }

    public static void clearCache(String basename) throws Exception {
        Field field = ResourceBundle.class.getDeclaredField("cacheList");
        field.setAccessible(true);

        Object cache = field.get(null);

        Map theCache = (cache instanceof SoftCache) ? (SoftCache) cache : (ConcurrentHashMap) cache;

        int counter = 0;
        String basename1 = basename + "@";
        String basename2 = basename + "_";
        for (Iterator iter = theCache.keySet().iterator(); iter.hasNext();) {
            Object cachekey = iter.next();
            Object resource = theCache.get(cachekey);

            String resourceStr = resource == null ? "" : resource.toString();
            if ((resourceStr.startsWith(basename1)) || (resourceStr.startsWith(basename2))) {
                iter.remove();
                counter++;
            }
        }
        if (isDebug) {
            if (counter > 0) {
                log.debug(String.format("There are %d resourceBundle for %s to be removed.%n", new Object[] { Integer.valueOf(counter), basename }));
            } else {
                log.debug(String.format("No resourceBundle for %s to be removed.%n", new Object[] { basename }));
            }
        }
    }
}
