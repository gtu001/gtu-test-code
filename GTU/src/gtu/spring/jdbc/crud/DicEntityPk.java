package gtu.spring.jdbc.crud;

import java.io.Serializable;

public class DicEntityPk implements Serializable {

    private static final long serialVersionUID = 1L;

    public String key;
    public String locale;
    public String type;

    public DicEntityPk() {
    }

    public DicEntityPk(String str) {
        fromString(str);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return key + "::" + locale + "::" + type;
    }

    @Override
    public int hashCode() {
        int rs = 17;
        rs = rs * 37 + ((key == null) ? 0 : key.hashCode());
        rs = rs * 37 + ((locale == null) ? 0 : locale.hashCode());
        rs = rs * 37 + ((type == null) ? 0 : type.hashCode());
        return rs;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        DicEntityPk other = (DicEntityPk) obj;
        return ((key == null && other.key == null) || (key != null && key.equals(other.key))) && ((locale == null && other.locale == null) || (locale != null && locale.equals(other.locale)))
                && ((type == null && other.type == null) || (type != null && type.equals(other.type)));
    }

    private void fromString(String str) {
        Tokenizer toke = new Tokenizer(str);
        str = toke.nextToken();
        if ("null".equals(str)) {
            key = null;
        } else {
            key = str;
        }
        str = toke.nextToken();
        if ("null".equals(str)) {
            locale = null;
        } else {
            locale = str;
        }
        str = toke.nextToken();
        if ("null".equals(str)) {
            type = null;
        } else {
            type = str;
        }
    }

    protected static class Tokenizer {
        private final String str;
        private int last;

        public Tokenizer(String str) {
            this.str = str;
        }

        public String nextToken() {
            int next = str.indexOf("::", last);
            String part;
            if (next == -1) {
                part = str.substring(last);
                last = str.length();
            } else {
                part = str.substring(last, next);
                last = next + 2;
            }
            return part;
        }
    }
}