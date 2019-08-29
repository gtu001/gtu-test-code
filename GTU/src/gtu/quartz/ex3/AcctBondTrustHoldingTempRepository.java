package gtu.quartz.ex3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AcctBondTrustHoldingTempRepository extends JpaRepository<AcctBondTrustHoldingTemp, String> {

    @Modifying
    @Transactional
    @Query(value = "truncate table ACCT_BOND_TRUST_HOLDING_TEMP", nativeQuery = true)
    void truncateTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO ACCT_BOND_TRUST_HOLDING_TEMP ("
            + " BOND_ID                       ,CUST_ID                       ,TST_ACCT_NO                   ,"
            + " CONTRACT_NOTE_NBR             ,CUSTODIAN_INSTITUTE           ,TMP_PRINCIPLE_PAYMENT         ,"
            + " REDEEM_BRANCH_CODE            ,REDEEM_AMT                    ,RESERVERS_UNIT                ,"
            + " FX_CURRENT_BAL                ,NTD_CURRENT_BAL               ,FX_TXN_DEAL_AMT               ,"
            + " NTD_TXN_DEAL_AMT              ,TRUST_TYPE_CODE               ,BON_TRUST_ACCT_NBR            ,"
            + " DISTRIBUTE_BANK_CODE          ,DISTRIBUTE_ACCT_NBR           ,BRANCH_CODE                   ,"
            + " PROJECT_ID                    ,BOND_MATURITY_IND             ,DBU_OBU_TXN_IND               ,"
            + " UPPER_DOWN_ALERT_SET_IND      ,SATIATION_POINT_PERCENT       ,STOP_LOSS_POINT_PERCENT       ,"
            + " RESERVE_FIELD                 ,MANAGEMENT_CHARAGE_FEE_FLAG   ,EMAIL                         ,"
            + " UPDATE_DTTM                   )"
            + " SELECT "
            + " BOND_ID                       ,CUST_ID                       ,TST_ACCT_NO                   ,"
            + " CONTRACT_NOTE_NBR             ,CUSTODIAN_INSTITUTE           ,TMP_PRINCIPLE_PAYMENT         ,"
            + " REDEEM_BRANCH_CODE            ,REDEEM_AMT                    ,RESERVERS_UNIT                ,"
            + " FX_CURRENT_BAL                ,NTD_CURRENT_BAL               ,FX_TXN_DEAL_AMT               ,"
            + " NTD_TXN_DEAL_AMT              ,TRUST_TYPE_CODE               ,BON_TRUST_ACCT_NBR            ,"
            + " DISTRIBUTE_BANK_CODE          ,DISTRIBUTE_ACCT_NBR           ,BRANCH_CODE                   ,"
            + " PROJECT_ID                    ,BOND_MATURITY_IND             ,DBU_OBU_TXN_IND               ,"
            + " UPPER_DOWN_ALERT_SET_IND      ,SATIATION_POINT_PERCENT       ,STOP_LOSS_POINT_PERCENT       ,"
            + " RESERVE_FIELD                 ,MANAGEMENT_CHARAGE_FEE_FLAG   ,EMAIL                         ,"
            + " UPDATE_DTTM                   "
            + " FROM ACCT_BOND_TRUST_HOLDING_SG", nativeQuery = true)
    int insertFromAcctBondTrustHoldingSg();

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE ACCT_BOND_TRUST_HOLDING_TEMP RENAME TO ACCT_BOND_TRUST_HOLDING", nativeQuery = true)
    void renameTable();

}
