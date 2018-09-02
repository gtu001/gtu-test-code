package gtu.net.cookie;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebClient {

    public static void main(String[] args) throws Exception {
        CookieStore store = new MyCookieStore();
        CookiePolicy policy = new MyCookiePolicy();
        CookieManager handler = new CookieManager(store, policy);
        CookieHandler.setDefault(handler);
        URL url = new URL("http://localhost:8080/cookieTest.jsp");
        URLConnection conn = url.openConnection();

        InputStream in = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String input;
        while ((input = reader.readLine()) != null) {
            System.out.println(input);
        }
        reader.close();

    }
}

class MyCookiePolicy implements CookiePolicy {
    public boolean shouldAccept(URI uri, HttpCookie cookie) {
        // String host = uri.getHost();
        // return host.equals("localhost");
        return true;
    }
}

class MyCookieStore implements CookieStore {
    private Map<URI, List<HttpCookie>> map = new HashMap<URI, List<HttpCookie>>();

    public void add(URI uri, HttpCookie cookie) {
        List<HttpCookie> cookies = map.get(uri);
        if (cookies == null) {
            cookies = new ArrayList<HttpCookie>();
            map.put(uri, cookies);
        }
        cookies.add(cookie);
    }

    public List<HttpCookie> get(URI uri) {
        List<HttpCookie> cookies = map.get(uri);
        if (cookies == null) {
            cookies = new ArrayList<HttpCookie>();
            map.put(uri, cookies);
        }
        return cookies;
    }

    public List<HttpCookie> getCookies() {
        Collection<List<HttpCookie>> values = map.values();
        List<HttpCookie> result = new ArrayList<HttpCookie>();
        for (List<HttpCookie> value : values) {
            result.addAll(value);
        }
        return result;
    }

    public List<URI> getURIs() {
        Set<URI> keys = map.keySet();
        return new ArrayList<URI>(keys);

    }

    public boolean remove(URI uri, HttpCookie cookie) {
        List<HttpCookie> cookies = map.get(uri);
        if (cookies == null) {
            return false;
        }
        return cookies.remove(cookie);
    }

    public boolean removeAll() {
        map.clear();
        return true;
    }
}