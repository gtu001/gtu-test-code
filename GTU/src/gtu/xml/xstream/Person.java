package gtu.xml.xstream;

import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("person")
public class Person {

    @XStreamAsAttribute
    //轉換成屬性
    @XStreamAlias("firstname")
    //別名註解
    private String firstname;

    @XStreamAlias("lastname")
    private String lastname;

    //    @XStreamConverter(DateConverter.class)
    //注入轉換器
    private Date birthDate;

    @XStreamAlias("phone")
    private PhoneNumber phone;

    @XStreamAlias("fax")
    private PhoneNumber fax;

    @XStreamImplicit
    //隱式集合
    private List<Person> family;

    @XStreamOmitField
    //忽略
    private String remark;

    @Override
    public String toString() {
        return "Person [firstname=" + firstname + ", lastname=" + lastname + ", birthDate=" + birthDate + ", phone=" + phone + ", fax=" + fax + ", family=" + family + ", remark=" + remark + "]";
    }

    public Person() {
        super();
    }

    public Person(String firstname, String lastname) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public PhoneNumber getPhone() {
        return phone;
    }

    public void setPhone(PhoneNumber phone) {
        this.phone = phone;
    }

    public PhoneNumber getFax() {
        return fax;
    }

    public void setFax(PhoneNumber fax) {
        this.fax = fax;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<Person> getFamily() {
        return family;
    }

    public void setFamily(List<Person> family) {
        this.family = family;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}