package gtu.net.cookie;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveCookie {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public void invalidateSession(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        // request.getSession().invalidate();//if don't work try this
        Cookie[] cookies = request.getCookies();

        StringBuilder keyval = new StringBuilder();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                cookies[i].setValue(null);
                cookies[i].setMaxAge(0);
                response.addCookie(cookie);
                keyval.append(cookies[i].getName() + "=" + cookies[i].getValue() + "; ");
            }
        }

        Date date = new Date();
        // Locale locale = Locale.CHINA;
        // SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",
        // locale);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        StringBuilder builder = new StringBuilder();
        builder.append(keyval);
        builder.append("Path=/; HttpOnly; ");
        builder.append("Max-Age=0; ");
        builder.append("Expires=" + sdf.format(date));
        response.setHeader("Set-Cookie", builder.toString());
    }
}
