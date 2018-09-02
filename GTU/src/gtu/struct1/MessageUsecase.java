package gtu.struct1;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class MessageUsecase {

    //複製放在action李
    private void showMeesages(HttpServletRequest request, String message, boolean isError) {
        showMeesages(request, Arrays.asList(message), isError);
    }
    private void showMeesages(HttpServletRequest request, List<String> messages, boolean isError) {
        log.debug("#. showMeesages .s");
        ActionMessages ams = new ActionMessages();
        for(String message : messages){
            ams.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(message, false));
        }
        if(ams.isEmpty()){
            log.debug("showMeesages - 無任何訊息!!");
        }else{
            if(isError){
                this.saveErrors(request, ams);
            }else{
                this.saveMessages(request, ams);
            }
        }
        log.debug("#. showMeesages .e");
    }
    
    //頁面要放
    <%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
    <%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
    <%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
    <%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
    <%@ taglib uri="http://ajaxtags.org/tags/ajax" prefix="ajax" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
    <c:set var="contextPath" scope="request" value="${pageContext.request.contextPath}" />
    <un:useConstants var="frontendConstant" className="com.sti.estore.frontend.web.FrontendConstant" />
    
    <logic:messagesPresent message="true">
        <script language="javascript">
                alert("<html:messages id='messages' message='true'><bean:write name='messages'/>\n</html:messages>");
        </script>
    </logic:messagesPresent>
}
