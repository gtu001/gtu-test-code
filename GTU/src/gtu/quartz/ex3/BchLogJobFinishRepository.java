package gtu.quartz.ex3;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BchLogJobFinishRepository extends JpaRepository<BchLogJobFinish, Long> {

    long countByProgramIdAndBatchDateAndSkipFlag(String programId, String batchDate, String skipFlag);

    Optional<BchLogJobFinish> findByProgramIdAndBatchDateAndSkipFlagAndReturnCode(
            String programId, String batchDate, String skipFlag, String returnCode);
}
