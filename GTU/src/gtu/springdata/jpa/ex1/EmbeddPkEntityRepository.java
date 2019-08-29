package gtu.springdata.jpa.ex1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmbeddPkEntityRepository extends JpaRepository<EmbeddPkEntity, EmbeddPkEntityPk> {

    @Query(value = "select max(t.embeddPkEntityPk.pkColumn1) from EmbeddPkEntity t")
    public String getMaxPkColumn1();
}
