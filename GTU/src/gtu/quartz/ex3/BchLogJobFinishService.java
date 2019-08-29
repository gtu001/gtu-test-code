package gtu.quartz.ex3;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BchLogJobFinishService {

    @Autowired
    BchLogJobFinishRepository bchLogJobFinishRepository;

    public long countByProgramIdAndBatchDateAndSkipFlag(String programId, String batchDate, String skipFlag) {

        return bchLogJobFinishRepository.countByProgramIdAndBatchDateAndSkipFlag(programId, batchDate, skipFlag);
    }

    public Optional<BchLogJobFinish> findByProgramIdAndBatchDateAndSkipFlagAndReturnCode(String programId,
            String batchDate, String skipFlag, String returnCode) {

        return bchLogJobFinishRepository.findByProgramIdAndBatchDateAndSkipFlagAndReturnCode(programId, batchDate,
            skipFlag, returnCode);
    }

    public BchLogJobFinish save(BchLogJobFinish bchLogJobFinish) {
        return bchLogJobFinishRepository.save(bchLogJobFinish);
    }

    public void doSaveFinishJobRecord(String programId, String multiTrigger, LocalDateTime startDateTime,
            String jobTypeCd, String batchDate, String returnCode, String returnText) {
        this.save(
            this.generateBchLogJobFinish(batchDate, programId, multiTrigger, startDateTime, returnCode, returnText));
    }

    private BchLogJobFinish generateBchLogJobFinish(String batchDate, String programId, String multiTrigger,
            LocalDateTime startDateTime, String returnCode, String returnText) {

        BchLogJobFinish bchLogJobFinish = new BchLogJobFinish();
        bchLogJobFinish.setBatchDate(batchDate);
        bchLogJobFinish.setProgramId(programId);
        bchLogJobFinish.setReturnCode(returnCode);
        bchLogJobFinish.setStartDttm(startDateTime);
        bchLogJobFinish.setEndDttm(LocalDateTime.now());
        bchLogJobFinish.setSkipFlag(multiTrigger);
        bchLogJobFinish.setReturnDesc(returnText);
        bchLogJobFinish.setUpdateDttm(LocalDateTime.now());

        return bchLogJobFinish;
    }

}
