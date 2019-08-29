package gtu.quartz.ex3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AcctBondTrustHoldingRepository extends JpaRepository<AcctBondTrustHolding, String> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE ACCT_BOND_TRUST_HOLDING RENAME TO ACCT_BOND_TRUST_HOLDING_OLD", nativeQuery = true)
    void renameTable();

}
