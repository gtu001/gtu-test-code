package gtu.quartz.ex3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogSysBatchService {

    private final String DATE_TIME_FORMAT = "yyyyMMdd HH:mm:ss";

    @Autowired
    LogSysBatchRepository logSysBatchRepository;

//    @Override
    public LogSysBatch saveData(String batchName, String batchStepName, String logMessage) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        String batchDate = dateTimeFormatter.format(LocalDateTime.now());
        LogSysBatch logSysBatch = new LogSysBatch(batchName, batchStepName, batchDate, logMessage);
        LogSysBatch saveLogSysBatch = logSysBatchRepository.save(logSysBatch);

        return saveLogSysBatch;
    }
}
