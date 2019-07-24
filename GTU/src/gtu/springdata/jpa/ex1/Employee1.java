package gtu.springdata.jpa.ex1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMPLOYEE_1")
public class Employee1 {
    @Id
    @Column(name = "PK_1", nullable = false)
    private String pk1;
    @Column(name = "DATE1")
    private java.sql.Date date1;
    @Column(name = "DATE2")
    private java.sql.Timestamp date2;
    @Column(name = "NUM_1")
    private java.math.BigDecimal num1;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CLOB_BODY")
    private String clobBody;

    public String getPk1() {
        return pk1;
    }

    public void setPk1(String pk1) {
        this.pk1 = pk1;
    }

    public java.sql.Date getDate1() {
        return date1;
    }

    public void setDate1(java.sql.Date date1) {
        this.date1 = date1;
    }

    public java.sql.Timestamp getDate2() {
        return date2;
    }

    public void setDate2(java.sql.Timestamp date2) {
        this.date2 = date2;
    }

    public java.math.BigDecimal getNum1() {
        return num1;
    }

    public void setNum1(java.math.BigDecimal num1) {
        this.num1 = num1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClobBody() {
        return clobBody;
    }

    public void setClobBody(String clobBody) {
        this.clobBody = clobBody;
    }
}
