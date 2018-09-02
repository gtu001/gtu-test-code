package gtu.json.jackson;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
 
@JsonFilter("com.sample.pojo.Customer")
public class Customer {
    private String firstName;
    private String middleName;
    private String lastName;
    private int age;
    private List<String> contacts;
 
    @JsonIgnore
    private String country;
 
    @JsonIgnore
    private String city;
 
    public String getCountry() {
        return country;
    }
 
    public void setCountry(String country) {
        this.country = country;
    }
 
    public String getCity() {
        return city;
    }
 
    public void setCity(String city) {
        this.city = city;
    }
 
    public List<String> getContacts() {
        return contacts;
    }
 
    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }
 
    public String getFirstName() {
        return firstName;
    }
 
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
 
    public String getMiddleName() {
        return middleName;
    }
 
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
 
    public String getLastName() {
        return lastName;
    }
 
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
 
    public int getAge() {
        return age;
    }
 
    public void setAge(int age) {
        this.age = age;
    }
 
}