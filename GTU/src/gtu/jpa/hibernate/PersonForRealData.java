package gtu.jpa.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Joiner;

@Entity(name = "PersonForReadData")
@Table(name = "Rcdf001m")
//@NamedQuery(name = "findPerson", query = //
//" SELECT                                                                                                                                         "
//      + "     a.person_id as personid,                                                                                                                               "
//      + "     (case when b.birth_yyymmdd is null or trim(b.birth_yyymmdd) = '' then a.birth_yyymmdd else b.birth_yyymmdd end) as birth_yyymmdd as birthYyymmdd,          "
//      + "     (case when b.site_id is null or trim(b.site_id) = '' then a.site_id else b.site_id end) as siteId                                         "
//      + " FROM (select person_id, birth_yyymmdd, site_id from rcdf002e WHERE tx_yyymmdd >= '0941221' AND apply_reason = '2' AND process_status = '9') a  "
//      + " LEFT JOIN rcdf001m b ON a.person_id = b.person_id                                                                                              "
//      )
//@NamedQuery(name = "findPerson", query = //
//  "select a from Person a"//
//    )
//@NamedNativeQuery(
//      name = "findPerson",
//      query = "select a.person_id, a.birth_yyymmdd, a.site_id from rcdf002e a",
//      resultClass = Person.class
//      )
public class PersonForRealData implements Serializable{
    
    private static final long serialVersionUID = 1L;

//    @Id
//    @GeneratedValue
//    long id;
    
    @Id
    @Column(name="PERSON_ID", length=10, nullable=false)
    String personid;
    
    @Column(name="BIRTH_YYYMMDD", length=7, nullable=false)
    String birthYyymmdd;
    
    @Id
    @Column(name="SITE_ID", length=8, nullable=false)
    String siteId;
    
    @Transient
    int count;

    @Override
    public String toString() {
        return "Person [personid=" + personid + ", birthYyymmdd=" + birthYyymmdd + ", siteId=" + siteId + ", count="
                + count + "]";
    }

    public String writeOver() {
        Object[] vals = new Object[] { StringUtils.leftPad(personid, 10, '0'), //
                StringUtils.leftPad(birthYyymmdd, 7, '0'), //
                StringUtils.substring(StringUtils.leftPad(siteId, 8, '0'), 0, 5), //
                StringUtils.leftPad(String.valueOf(count), 2, '0'), //
        };
        return Joiner.on(",").join(vals);
    }

    public String writeOne() {
        Object[] vals = new Object[] { StringUtils.leftPad(siteId, 8, '0'), //
                StringUtils.leftPad(personid, 10, '0'), //
                StringUtils.leftPad(birthYyymmdd, 7, '0'), //
                StringUtils.leftPad(String.valueOf(count), 2, '0'), //
        };
        return Joiner.on(",").join(vals);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((personid == null) ? 0 : personid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersonForRealData other = (PersonForRealData) obj;
        if (personid == null) {
            if (other.personid != null)
                return false;
        } else if (!personid.equals(other.personid))
            return false;
        return true;
    }
}
/*
 * javax.persistence.Column ↓↓↓↓↓
name屬性定義了被標注欄位在資料庫表中所對應欄位的名稱；
unique屬性工作表示該欄位是否為唯一標識，預設為false。如果表中有一個欄位需要唯一標識，則既可以使用該標記，也可以使用@Table標記中的@UniqueConstraint。
nullable屬性工作表示該欄位是否可以為null值，默認為true。
insertable屬性工作表示在使用“INSERT”腳本插入資料時，是否需要插入該欄位的值。
updatable屬性工作表示在使用“UPDATE”腳本插入資料時，是否需要更新該欄位的值。insertable和updatable屬性一般多用於唯讀的屬性，例如主鍵和外鍵等。這些欄位的值通常是自動生成的。
columnDefinition屬性工作表示創建表時，該欄位創建的SQL語句，一般用於通過Entity生成表定義時使用。（也就是說，如果DB中表已經建好，該屬性沒有必要使用。）
table屬性定義了包含當前欄位的表名。
length屬性工作表示欄位的長度，當欄位的類型為varchar時，該屬性才有效，預設為255個字元。
precision屬性和scale屬性工作表示精度，當欄位類型為double時，precision表示數值的總長度，scale表示小數點所占的位數。
*/