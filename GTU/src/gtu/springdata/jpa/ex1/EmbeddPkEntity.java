package gtu.springdata.jpa.ex1;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EMBEDD_PK_ENTITY")
public class EmbeddPkEntity implements Serializable {

    @EmbeddedId
    private EmbeddPkEntityPk embeddPkEntityPk;

    @Column(name = "test_column1")
    private String testColumn1;
    @Column(name = "test_column2")
    private String testColumn2;
}
