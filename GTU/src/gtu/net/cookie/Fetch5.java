package gtu.net.cookie;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Fetch5 {
    public static void main(String args[]) throws Exception {
        CookieHandler.setDefault(new Fetch5().new ListCookieHandler());
        URL url = new URL("http://www.google.com");
        URLConnection connection = url.openConnection();
        Object obj = connection.getContent();
    }

    private class ListCookieHandler extends CookieHandler {
        private List<Cookie> cookieJar = new LinkedList<Cookie>();

        public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
            List<String> setCookieList = responseHeaders.get("Set-Cookie");
            if (setCookieList != null) {
                for (String item : setCookieList) {
                    Cookie cookie = new Cookie(uri, item);
                    for (Cookie existingCookie : cookieJar) {
                        if ((cookie.getURI().equals(existingCookie.getURI())) && (cookie.getName().equals(existingCookie.getName()))) {
                            cookieJar.remove(existingCookie);
                            break;
                        }
                    }
                    cookieJar.add(cookie);
                }
            }
        }

        public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
            StringBuilder cookies = new StringBuilder();
            for (Cookie cookie : cookieJar) {
                // Remove cookies that have expired
                if (cookie.hasExpired()) {
                    cookieJar.remove(cookie);
                } else if (cookie.matches(uri)) {
                    if (cookies.length() > 0) {
                        cookies.append(", ");
                    }
                    cookies.append(cookie.toString());
                }
            }

            Map<String, List<String>> cookieMap = new HashMap<String, List<String>>(requestHeaders);

            if (cookies.length() > 0) {
                List<String> list = Collections.singletonList(cookies.toString());
                cookieMap.put("Cookie", list);
            }
            System.out.println("CookieMap: " + cookieMap);
            return Collections.unmodifiableMap(cookieMap);
        }
    }

    private class Cookie {

        String name;

        String value;

        URI uri;

        String domain;

        Date expires;

        String path;

        private DateFormat expiresFormat1 = new SimpleDateFormat("E, dd MMM yyyy k:m:s 'GMT'", Locale.US);

        private DateFormat expiresFormat2 = new SimpleDateFormat("E, dd-MMM-yyyy k:m:s 'GMT'", Locale.US);

        public Cookie(URI uri, String header) {
            String attributes[] = header.split(";");
            String nameValue = attributes[0].trim();
            this.uri = uri;
            this.name = nameValue.substring(0, nameValue.indexOf('='));
            this.value = nameValue.substring(nameValue.indexOf('=') + 1);
            this.path = "/";
            this.domain = uri.getHost();

            for (int i = 1; i < attributes.length; i++) {
                nameValue = attributes[i].trim();
                int equals = nameValue.indexOf('=');
                if (equals == -1) {
                    continue;
                }
                String name = nameValue.substring(0, equals);
                String value = nameValue.substring(equals + 1);
                if (name.equalsIgnoreCase("domain")) {
                    String uriDomain = uri.getHost();
                    if (uriDomain.equals(value)) {
                        this.domain = value;
                    } else {
                        if (!value.startsWith(".")) {
                            value = "." + value;
                        }
                        uriDomain = uriDomain.substring(uriDomain.indexOf('.'));
                        if (!uriDomain.equals(value)) {
                            throw new IllegalArgumentException("Trying to set foreign cookie");
                        }
                        this.domain = value;
                    }
                } else if (name.equalsIgnoreCase("path")) {
                    this.path = value;
                } else if (name.equalsIgnoreCase("expires")) {
                    try {
                        this.expires = expiresFormat1.parse(value);
                    } catch (ParseException e) {
                        try {
                            this.expires = expiresFormat2.parse(value);
                        } catch (ParseException e2) {
                            throw new IllegalArgumentException("Bad date format in header: " + value);
                        }
                    }
                }
            }
        }

        public boolean hasExpired() {
            if (expires == null) {
                return false;
            }
            Date now = new Date();
            return now.after(expires);
        }

        public String getName() {
            return name;
        }

        public URI getURI() {
            return uri;
        }

        public boolean matches(URI uri) {

            if (hasExpired()) {
                return false;
            }
            String path = uri.getPath();
            if (path == null) {
                path = "/";
            }

            return path.startsWith(this.path);
        }

        public String toString() {
            StringBuilder result = new StringBuilder(name);
            result.append("=");
            result.append(value);
            return result.toString();
        }
    }
}
