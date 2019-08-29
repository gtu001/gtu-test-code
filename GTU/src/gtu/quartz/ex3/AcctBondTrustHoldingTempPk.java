package gtu.quartz.ex3;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AcctBondTrustHoldingTempPk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "BOND_ID")
    private String bondId;

    @Column(name = "TST_ACCT_NO")
    private String tstAcctNo;

    public AcctBondTrustHoldingTempPk() {

    }

    public AcctBondTrustHoldingTempPk(String bondId, String tstAcctNo) {
        super();
        this.bondId = bondId;
        this.tstAcctNo = tstAcctNo;
    }

    public String getBondId() {
        return bondId;
    }

    public void setBondId(String bondId) {
        this.bondId = bondId;
    }

    public String getTstAcctNo() {
        return tstAcctNo;
    }

    public void setTstAcctNo(String tstAcctNo) {
        this.tstAcctNo = tstAcctNo;
    }
}
