package gtu.quartz.ex3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cub.robo.fee.batch.enums.PartyCusDrvEnum;
import cub.robo.fee.batch.enums.PartyDmRejectDetEnum;
import cub.robo.fee.batch.service.AcctBondTrustHoldingService;
import cub.robo.fee.batch.service.LogSysBatchService;

@Component
public class AcctBondTrustHoldingInsertRenameTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(AcctBondTrustHoldingInsertRenameTasklet.class);

    private String jobLogName;

    @Autowired
    AcctBondTrustHoldingService acctBondTrustHoldingService;

    @Autowired
    LogSysBatchService logSysBatchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        this.jobLogName = (String) chunkContext.getStepContext().getJobExecutionContext().get("jobLogName");
        this.doTruncateTable();
        this.doInsertTable();
        this.doRenameTable();
        return RepeatStatus.FINISHED;
    }

    private void doTruncateTable() {
        logger.info("(7)truncate ACCT_BOND_TRUST_HOLDING_TEMP table.....");
        logSysBatchService.saveData(jobLogName, PartyDmRejectDetEnum.STEP7.getValue(), PartyDmRejectDetEnum.STEP7.getText());
        logger.info("delete ACCT_BOND_TRUST_HOLDING_TEMP all data");
        acctBondTrustHoldingService.truncateAcctBondTrustHoldingTemp();
    }

    private void doInsertTable() {
        logger.info("(8)insert ACCT_BOND_TRUST_HOLDING_TEMP table.....");
        logSysBatchService.saveData(jobLogName, PartyDmRejectDetEnum.STEP8.getValue(), PartyDmRejectDetEnum.STEP8.getText());

        int insertCount = acctBondTrustHoldingService.insertAcctBondTrustHoldingTempFromAcctBondTrustHoldingSg();
        logger.info("insert ACCT_BOND_TRUST_HOLDING_TEMP data From ACCT_BOND_TRUST_HOLDING_SG, insertCount = {}", insertCount);
    }

    private void doRenameTable() {
        logger.info("(9)rename ACCT_BOND_TRUST_HOLDING table.....");
        logSysBatchService.saveData(jobLogName, PartyCusDrvEnum.STEP9.getValue(), PartyCusDrvEnum.STEP9.getText());
        acctBondTrustHoldingService.renameTableAcctBondTrustHoldingToAcctBondTrustHoldingOld();

        logger.info("(10)rename ACCT_BOND_TRUST_HOLDING_TEMP table.....");
        logSysBatchService.saveData(jobLogName, PartyCusDrvEnum.STEP10.getValue(), PartyCusDrvEnum.STEP10.getText());
        acctBondTrustHoldingService.renameTableAcctBondTrustHoldingTempToAcctBondTrustHolding();

        logger.info("(11)rename ACCT_BOND_TRUST_HOLDING_OLD table.....");
        logSysBatchService.saveData(jobLogName, PartyCusDrvEnum.STEP11.getValue(), PartyCusDrvEnum.STEP11.getText());
        acctBondTrustHoldingService.renameTableAcctBondTrustHoldingOldToAcctBondTrustHoldingTemp();

    }
}
