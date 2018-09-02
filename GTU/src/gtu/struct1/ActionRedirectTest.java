package gtu.struct1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.portlet.bind.annotation.ActionMapping;

public class ActionRedirectTest {
    
    /**
     * 未輸入dispatch參數
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return init(mapping, form, request, response);
    }

    //Strut1 直接倒頁面的做法
    public ActionForward initSearchForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dyForm = (DynaActionForm) form;
        return redirectForward(request, response, //
                new ActionForward("/admin/marketing/portalTopBlock.do?cmd=initSearchForm", true));
    }

    //工具方法
    private static ActionForward redirectForward(HttpServletRequest req, HttpServletResponse res, ActionForward oldFwd)
            throws Exception {
        if (oldFwd == null)
            return oldFwd;
        boolean isRedirect = oldFwd.getRedirect();
        if (!isRedirect)
            return oldFwd;

        String oldPath = oldFwd.getPath();
        String oldCtxPath = req.getContextPath();
        boolean isFullUrl = oldPath != null && (oldPath.startsWith("http://") || oldPath.startsWith("https://"));
        String redirectUrl = isFullUrl ? oldPath : String.format("%s%s", oldCtxPath, oldPath);

        // 開始輸出重導之HTML
        res.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        res.setHeader("Location", redirectUrl);
        return null;
    }
}
