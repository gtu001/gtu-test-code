package gtu.xml.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("phoneNumber")
public class PhoneNumber {
    @XStreamAlias("code")
    private int code;
    @XStreamAlias("number")
    private String number;

    @Override
    public String toString() {
        return "PhoneNumber [code=" + code + ", number=" + number + "]";
    }

    public PhoneNumber() {
        super();
    }

    public PhoneNumber(int code, String number) {
        super();
        this.code = code;
        this.number = number;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}