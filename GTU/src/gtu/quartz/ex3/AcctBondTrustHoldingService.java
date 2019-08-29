package gtu.quartz.ex3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcctBondTrustHoldingService {
    
    @Autowired
    AcctBondTrustHoldingRepository acctBondTrustHoldingRepository;

    @Autowired
    AcctBondTrustHoldingTempRepository acctBondTrustHoldingTempRepository;

    @Autowired
    AcctBondTrustHoldingOldRepository acctBondTrustHoldingOldRepository;

    public void truncateAcctBondTrustHoldingTemp() {
        acctBondTrustHoldingTempRepository.truncateTable();
    }

    public int insertAcctBondTrustHoldingTempFromAcctBondTrustHoldingSg() {
        return acctBondTrustHoldingTempRepository.insertFromAcctBondTrustHoldingSg();
    }

    public void renameTableAcctBondTrustHoldingToAcctBondTrustHoldingOld() {
        acctBondTrustHoldingRepository.renameTable();
    }

    public void renameTableAcctBondTrustHoldingTempToAcctBondTrustHolding() {
        acctBondTrustHoldingTempRepository.renameTable();
    }

    public void renameTableAcctBondTrustHoldingOldToAcctBondTrustHoldingTemp() {
        acctBondTrustHoldingOldRepository.renameTable();
    }
}
