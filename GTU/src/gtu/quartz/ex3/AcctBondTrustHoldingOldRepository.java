package gtu.quartz.ex3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AcctBondTrustHoldingOldRepository extends JpaRepository<AcctBondTrustHoldingOld, String> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE ACCT_BOND_TRUST_HOLDING_OLD RENAME TO ACCT_BOND_TRUST_HOLDING_TEMP", nativeQuery = true)
    void renameTable();

}
