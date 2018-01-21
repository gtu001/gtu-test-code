package gtu.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CharacterEncodingHttpServletRequestWrapper extends
        HttpServletRequestWrapper {

    private String ecoding = null;
    private String oldEncoding = null;

    public CharacterEncodingHttpServletRequestWrapper(
            HttpServletRequest request, String encoding) {
        super(request);
        this.ecoding = encoding;
        this.oldEncoding = request.getCharacterEncoding();
    }

    public String getParameter(String value) {
        try {
            if ((oldEncoding == null || isIOS88591(oldEncoding))
                    && super.getParameter(value) != null) {
                return new String(super.getParameter(value).getBytes("iso-8859-1"), ecoding);
            } else {
                return super.getParameter(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isIOS88591(String endcoding) {
        endcoding = endcoding.toLowerCase();
        return endcoding.startsWith("iso") && (endcoding.indexOf("8859") != -1) && endcoding.endsWith("1");
    }
}

/*
<filter>
<filter-name>CharsetEncoding</filter-name>
<filter-class>javabean.com.CharsetEncodingFilter</filter-class>
<init-param>
<param-name>RequestEncoding</param-name>
<param-value>iso-8859-1</param-value>
</init-param>
<init-param>
<param-name>ResponseEncoding</param-name>
<param-value>gb2312</param-value>
</init-param>
</filter>
<filter-mapping>
<filter-name>CharsetEncoding</filter-name>
<url-pattern>/*</url-pattern>
</filter-mapping> 
*/