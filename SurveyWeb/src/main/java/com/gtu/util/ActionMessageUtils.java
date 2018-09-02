package com.gtu.util;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ActionMessageUtils {

    private Logger log = Logger.getLogger(ActionMessageUtils.class);
    
    private static final ActionMessageUtils _INSTANCE = new ActionMessageUtils();

    private ActionMessageUtils() {
    }

    public static void saveErrors(HttpServletRequest request, String message) {
        _INSTANCE._processMessages(request, Arrays.asList(message), true);
    }

    public static void saveMessages(HttpServletRequest request, String message) {
        _INSTANCE._processMessages(request, Arrays.asList(message), false);
    }

    private void _processMessages(HttpServletRequest request, List<String> messages, boolean isError) {
        ActionMessages ams = new ActionMessages();
        for (String message : messages) {
            ams.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(message, false));
        }
        _processMessages(request, ams, isError);
    }

    private void _processMessages(HttpServletRequest request, ActionMessages ams, boolean isError) {
        String key = Globals.ERROR_KEY;
        if (!isError) {
            key = Globals.MESSAGE_KEY;
        }
        if (ams == null || ams.isEmpty()) {
            request.removeAttribute(key);
            return;
        }
        log.info("新增訊息  : " + ams);
        request.setAttribute(key, ams);
    }
}
