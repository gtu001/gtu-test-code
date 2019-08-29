package gtu.springdata.jpa.ex1;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EmbeddPkEntityPk implements Serializable {

    @Column(name = "PK_COLUMN1")
    private String pkColumn1;
}
