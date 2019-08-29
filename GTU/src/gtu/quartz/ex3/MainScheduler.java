package gtu.quartz.ex3;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import cub.robo.fee.batch.entity.BchCfgJobDep;
import cub.robo.fee.batch.entity.BchCfgJobList;
import cub.robo.fee.batch.entity.BchJobBatchDtStatus;
import cub.robo.fee.batch.entity.BchLogJobFinish;
import cub.robo.fee.batch.entity.query.AcctBondTrustHoldingStepWording;
import cub.robo.fee.batch.entity.query.AcctDataboxTableStepWording;
import cub.robo.fee.batch.entity.query.AcctDrvMfStepWording;
import cub.robo.fee.batch.entity.query.AcctFundTrustHoldingCtfStepWording;
import cub.robo.fee.batch.entity.query.AcctFundTrustHoldingCtflStepWording;
import cub.robo.fee.batch.entity.query.BbgRdEtfCodeStepWording;
import cub.robo.fee.batch.entity.query.CtfAgreepromoteStepWording;
import cub.robo.fee.batch.entity.query.CtfFundStepWording;
import cub.robo.fee.batch.entity.query.CtflFundStepWording;
import cub.robo.fee.batch.entity.query.EmpDataboxTableStepWording;
import cub.robo.fee.batch.entity.query.FundListDetailStepWording;
import cub.robo.fee.batch.entity.query.FundListMainStepWording;
import cub.robo.fee.batch.entity.query.IVpStepWording;
import cub.robo.fee.batch.entity.query.IpMfbDeductionResultDailyStepWording;
import cub.robo.fee.batch.entity.query.IpRbDeductionResultDailyStepWording;
import cub.robo.fee.batch.entity.query.MgAcctLonStepWording;
import cub.robo.fee.batch.entity.query.MgFeeFundRdmptStepWording;
import cub.robo.fee.batch.entity.query.MstarCathaybkOdsrptStepWording;
import cub.robo.fee.batch.entity.query.MstarDailyRtnStepWording;
import cub.robo.fee.batch.entity.query.MstarDayChangeStepWording;
import cub.robo.fee.batch.entity.query.MstarFundListAfstwManagerStepWording;
import cub.robo.fee.batch.entity.query.MstarFundListAfstwStepWording;
import cub.robo.fee.batch.entity.query.MstarFundListStepWording;
import cub.robo.fee.batch.entity.query.MstarMonReturn1mBaseStepWording;
import cub.robo.fee.batch.entity.query.MstarMonReturn1mTwdStepWording;
import cub.robo.fee.batch.entity.query.MstarMonReturn1mUsdStepWording;
import cub.robo.fee.batch.entity.query.MstarMonReturn6mBaseStepWording;
import cub.robo.fee.batch.entity.query.MstarMonReturn6mTwdStepWording;
import cub.robo.fee.batch.entity.query.MstarMonReturn6mUsdStepWording;
import cub.robo.fee.batch.entity.query.MstarPortfolioStepWording;
import cub.robo.fee.batch.entity.query.MstarTwPrimaryListStepWording;
import cub.robo.fee.batch.entity.query.PartyAcctDrvDpStepWording;
import cub.robo.fee.batch.entity.query.PartyAcctDrvLbStepWording;
import cub.robo.fee.batch.entity.query.PartyCusDrvStepWording;
import cub.robo.fee.batch.entity.query.PartyCusGroupStepWording;
import cub.robo.fee.batch.entity.query.PartyCustomerStepWording;
import cub.robo.fee.batch.entity.query.PartyDataboxTableStepWording;
import cub.robo.fee.batch.entity.query.PartyDmRejectDetStepWording;
import cub.robo.fee.batch.entity.query.PartyDrvVipStepWording;
import cub.robo.fee.batch.entity.query.PartyDrvWsFeeDetailStepWording;
import cub.robo.fee.batch.entity.query.PartyEmployeeStepWording;
import cub.robo.fee.batch.entity.query.PartyFundInvCorpCtflStepWording;
import cub.robo.fee.batch.entity.query.PartyFundInvCorpStepWording;
import cub.robo.fee.batch.entity.query.PartyInfoDpStepWording;
import cub.robo.fee.batch.entity.query.PartyPbDefinitedStepWording;
import cub.robo.fee.batch.entity.query.PartyPledgeStepWording;
import cub.robo.fee.batch.entity.query.PartyVipDefiniteStepWording;
import cub.robo.fee.batch.entity.query.Pbf000StepWording;
import cub.robo.fee.batch.entity.query.PfmAssetClassLayerStepWording;
import cub.robo.fee.batch.entity.query.PfmProdAssetMapStepWording;
import cub.robo.fee.batch.entity.query.PromCustMaListStepWording;
import cub.robo.fee.batch.entity.query.RdBondCategoryCodesStepWording;
import cub.robo.fee.batch.entity.query.RdBondCodeStepWording;
import cub.robo.fee.batch.entity.query.RdBranchLonStepWording;
import cub.robo.fee.batch.entity.query.RdBranchStepWording;
import cub.robo.fee.batch.entity.query.RdFundCategoryCodesCtflStepWording;
import cub.robo.fee.batch.entity.query.RdFundCategoryCodesStepWording;
import cub.robo.fee.batch.entity.query.ReutersRtsEodStepWording;
import cub.robo.fee.batch.entity.query.ReutersRtsIntradayStepWording;
import cub.robo.fee.batch.entity.query.RoboDeductionAccountStepWording;
import cub.robo.fee.batch.entity.query.RoboPromotionsStepWording;
import cub.robo.fee.batch.entity.query.RptCommonFammStepWording;
import cub.robo.fee.batch.entity.query.StepWording;
import cub.robo.fee.batch.entity.query.TrustAcctValueStepWording;
import cub.robo.fee.batch.entity.query.UserAuditLogStepWording;
import cub.robo.fee.batch.enums.BusdayFlagEnum;
import cub.robo.fee.batch.enums.JobTypeEnum;
import cub.robo.fee.batch.enums.MonthlyDeductionEnum;
import cub.robo.fee.batch.enums.ProgramMappingEnum;
import cub.robo.fee.batch.enums.ReturnCodeEnum;
import cub.robo.fee.batch.enums.ScheTypeEnum;
import cub.robo.fee.batch.exception.InterruptJobException;
import cub.robo.fee.batch.service.BchCfgJobDepService;
import cub.robo.fee.batch.service.BchCfgJobListService;
import cub.robo.fee.batch.service.BchJobBatchDtStatusService;
import cub.robo.fee.batch.service.BchLogJobFinishService;
import cub.robo.fee.batch.service.RdCalendarService;
import cub.robo.fee.batch.util.FileOperator;

@Service
public class MainScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MainScheduler.class);

    private StepWording stepWording;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Autowired
    private FileOperator fileOperator;

    @Autowired
    private BchCfgJobListService bchCfgJobListService;

    @Autowired
    private BchLogJobFinishService bchLogJobFinishService;

    @Autowired
    private BchCfgJobDepService bchCfgJobDepService;

    @Autowired
    private BchJobBatchDtStatusService bchJobBatchDtStatusService;


    @Autowired
    private RdCalendarService rdCalendarService;
    
    private DateTimeFormatter dtfYyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

    private SimpleDateFormat sdfYyyyMM = new SimpleDateFormat("yyyyMM");
    
    //private SimpleDateFormat sdfYyyyMMdd = new SimpleDateFormat("yyyyMMdd");

    @Scheduled(cron = "${app.scheduler.corn.mainSchedule}")
    public void mainSchedule() {
        logger.info("Start scheduler mainSchedule.....");
        try {
            // Get all enable jobs from table
            List<BchCfgJobList> listBchCfgJobList = bchCfgJobListService.findByEnableFlagOrderBySeqId("Y");
            this.doJobProcess(listBchCfgJobList);
        } catch (Exception e) {
            logger.error("Scheduler mainSchedule failed!", e);
        }
    }

    private void doJobProcess(List<BchCfgJobList> listBchCfgJobList) {

        for (BchCfgJobList bchCfgJobList : listBchCfgJobList) {
            try {
                logger.info("===== Ready to do jobName ( {} {} ) =====", bchCfgJobList.getProgramName(),
                        bchCfgJobList.getProgramId());

                // 依jobTypeCd取得batchDate
                String batchDate = this.getBatchDateByJobTypeCd(bchCfgJobList.getProgramId(),
                    bchCfgJobList.getJobTypeCd(), bchCfgJobList.getTriggerFilePath(),
                    bchCfgJobList.getTriggerFileName(), bchCfgJobList.getProgramName(), bchCfgJobList.getBchDtType(),
                    bchCfgJobList.getScheTypeCd(), bchCfgJobList.getChkDate());

                if (!this.checkJobExecuteConditions(bchCfgJobList, batchDate)) {
                    logger.info("JobName ( {} {} ) won't execute, because it doesn't match conditions ..... ",
                            bchCfgJobList.getProgramName(), bchCfgJobList.getProgramId());
                    continue;
                }

                Job job = (Job) applicationContext.getBean(bchCfgJobList.getProgramName());
                jobLauncher.run(job, this.addJobParameters(bchCfgJobList, batchDate));

            } catch (Exception e) {
                logger.info("***** JobName( {} {} ) can't run, because {} *****", bchCfgJobList.getProgramName(),
                        bchCfgJobList.getProgramId(), e.getMessage());
                // 若不是自訂errMessage則印訊息
                if (!this.checkJobCustomException(e.getMessage())) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private JobParameters addJobParameters(BchCfgJobList bchCfgJobList, String batchDate) {
        this.setStepWording(bchCfgJobList.getProgramId());
        // Add job param to keep job can restartable
        return new JobParametersBuilder()
                .addString("triggerTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .addString("targetFlagFile",
                        bchCfgJobList.getTriggerFileName() != null ? bchCfgJobList.getTriggerFileName() : "")
                .addString("programId", bchCfgJobList.getProgramId())
                .addString("multiTrigger", bchCfgJobList.getMultiTrigger())
                .addString("jobLogName", bchCfgJobList.getJobName())
                .addString("fileBackupDays",
                        bchCfgJobList.getFileBackupDays() != null ? bchCfgJobList.getFileBackupDays() : "")
                .addString("jobTypeCd", bchCfgJobList.getJobTypeCd())
                .addString("scheTypeCd", bchCfgJobList.getScheTypeCd())
                .addString("triggerFilePath",
                        bchCfgJobList.getTriggerFilePath() != null ? bchCfgJobList.getTriggerFilePath() : "")
                .addString("bchDtType", bchCfgJobList.getBchDtType() != null ? bchCfgJobList.getBchDtType() : "")
                .addString("batchDate", batchDate)
                .addString("stepFileMoveValue", stepWording != null ? stepWording.getStepFileMoveValue() : "")
                .addString("stepFileMoveText", stepWording != null ? stepWording.getStepFileMoveText() : "")
                .addString("stepFileHousekeepingValue",
                        stepWording != null ? stepWording.getStepFileHousekeepingValue() : "")
                .addString("stepFileHousekeepingText",
                        stepWording != null ? stepWording.getStepFileHousekeepingText() : "")
                .addString("stepCheckFlgFileValue", stepWording != null ? stepWording.getStepCheckFlgFileValue() : "")
                .addString("stepCheckFlgFileText", stepWording != null ? stepWording.getStepCheckFlgFileText() : "")
                .addString("stepCheckRealFileValue", stepWording != null ? stepWording.getStepCheckRealFileValue() : "")
                .addString("stepCheckRealFileText", stepWording != null ? stepWording.getStepCheckRealFileText() : "")
                .addString("stepTruncateSgTableValue",
                        stepWording != null ? stepWording.getStepTruncateSgTableValue() : "")
                .addString("stepTruncateSgTableText",
                        stepWording != null ? stepWording.getStepTruncateSgTableText() : "")
                .addString("stepReadRealFileDataValue",
                        stepWording != null ? stepWording.getStepReadRealFileDataValue() : "")
                .addString("stepReadRealFileDataText",
                        stepWording != null ? stepWording.getStepReadRealFileDataText() : "")
                .addString("stepInsertSgTableValue", stepWording != null ? stepWording.getStepInsertSgTableValue() : "")
                .addString("stepInsertSgTableText", stepWording != null ? stepWording.getStepInsertSgTableText() : "")
                .toJobParameters();
    }

    private boolean checkJobExecuteConditions(BchCfgJobList bchCfgJobList, String batchDate) {
        boolean matchExecuteCondition = true;

        // Check jobs & denpendency jobs finished
        if (this.checkJobFinished(bchCfgJobList.getProgramId(), batchDate)
                || !this.checkDependencyJobsFinished(bchCfgJobList.getProgramId(), batchDate)
                || !this.checkJobConformExecDate(bchCfgJobList.getChkDate())
                || !this.checkJobConformExecTime(bchCfgJobList.getChkTime())) {
            matchExecuteCondition = false;
        }

        return matchExecuteCondition;
    }

    /**
     * 檢查job在batchDate是否已執行過, true:已執行過, false:未執行過
     * 
     * @param programId
     * @param batchDate
     * @return
     * @throws Exception
     */
    private boolean checkJobFinished(String programId, String batchDate) {
        boolean isFinished = false;

        long count = bchLogJobFinishService.countByProgramIdAndBatchDateAndSkipFlag(programId, batchDate, "N");

        // Check does the job finish
        if (count > 0) {
            isFinished = true;
        }

        logger.info("ProgramId {} checkJobFinished, isFinished = {}, batchDate = {}, skipFlag = N ", programId,
                isFinished, batchDate);
        return isFinished;
    }
    
    private boolean doDateCompare(String checkDate) {
        boolean isOverDate = true;
        LocalDate localBatchDate = LocalDate.parse(checkDate, dtfYyyyMMdd);
        if (!(localBatchDate.isBefore(LocalDate.now()))) {
            isOverDate = false;
        }
        return isOverDate;
    }
    
    private boolean isEqualToday(String checkDate) {
        boolean isEqual = true;
        LocalDate localBatchDate = LocalDate.parse(checkDate, dtfYyyyMMdd);
        if (!(localBatchDate.isEqual(LocalDate.now()))) {
            isEqual = false;
        }
        return isEqual;
    }
    
    private String[] getSortArray(String chkDate) {
        
        String[] strArray = chkDate.split(",");
        
        Arrays.sort(strArray);
        
        return strArray;     
    }
    
    // 找下個月的今天
    private Date getNextMonthDay(String batchDate, int number) throws ParseException {
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd");
        sdFormatter.setLenient(false);
        Date date = sdFormatter.parse(batchDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, number); // Calendar.MONTH 加上1個月(用add Method)
        return calendar.getTime();
    }
    
    // 找上個月
    private Date getNextMonth(String theMonth, int number) throws ParseException {
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMM");
        sdFormatter.setLenient(false);
        Date date = sdFormatter.parse(theMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, number); // Calendar.MONTH 加上1個月(用add Method)
        return calendar.getTime();
    }
    
    private String getThisMonth() {
        Date today = new Date();
        return sdfYyyyMM.format(today);
    }
    
    private Map<String, String> getBatchDate(String[] strArray) throws ParseException {
        Map<String, String> result = new HashMap<String, String>();
        String theDate = "";
        String nextDate = "";
        String batchDate = "";
        String lastDate = "";
        String theMonth = getThisMonth();
        String lastMonth = sdfYyyyMM.format(getNextMonth(theMonth, -1));
        Date thisBatchDate;
        Date nextBatchDate;
        Date lastBatchDate;

        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd");
        sdFormatter.setLenient(false);
        try {
            if (strArray.length == 1) {
                theDate = theMonth + strArray[0];
                if (doDateCompare(theDate)) {
                    thisBatchDate = getNextMonthDay(theDate, 1);
                    batchDate = sdFormatter.format(thisBatchDate);
                    nextBatchDate = getNextMonthDay(theDate, 2);
                    nextDate = sdFormatter.format(nextBatchDate);
                    lastDate = theMonth + strArray[0];
                } else {
                    batchDate = theDate;
                    lastBatchDate = getNextMonthDay(theDate, -1);
                    lastDate = sdFormatter.format(lastBatchDate);
                    nextBatchDate = getNextMonthDay(theDate, 1);
                    nextDate = sdFormatter.format(nextBatchDate);
                }
            } else {
                for (int i = 0; i < strArray.length; i++) {
                    // 10, 20, 30
                    theDate = theMonth + strArray[i];
                    if (doDateCompare(theDate)) {
                        // 如果是31號
                        if (i == strArray.length - 1) {
                            lastDate = theDate;
                            batchDate = theMonth + strArray[0];
                            thisBatchDate = getNextMonthDay(batchDate, 1);
                            batchDate = sdFormatter.format(thisBatchDate);
                            nextBatchDate = getNextMonthDay(theMonth + strArray[1], 1);
                            nextDate = sdFormatter.format(nextBatchDate);
                            break;
                        }
                        continue;
                    } else {
                        if(i==0) {
                            try {
                                lastBatchDate = getNextMonthDay(theMonth + strArray[strArray.length-1], -1);
                                lastDate = sdFormatter.format(lastBatchDate);
                            }catch(ParseException e) {
                                lastDate = lastMonth + strArray[strArray.length-1];
                            }
                            batchDate = theMonth + strArray[i];
                            nextDate = theMonth + strArray[i+1];
                        }else if(i!=0){
                            lastDate = theMonth + strArray[i-1];
                            batchDate = theMonth + strArray[i];
                            if(i != strArray.length - 1) {
                                nextDate = theMonth + strArray[i+1];
                                sdFormatter.parse(nextDate);
                            }else {
                                nextBatchDate = getNextMonthDay(theMonth + strArray[0], 1);
                                nextDate = sdFormatter.format(nextBatchDate);
                            }
                        }
                        break;
                    }
                }
            }
        } catch (ParseException e) {
            logger.info("lastDate = {} batchDate = {}, nextDate = {}", lastDate, batchDate, nextDate);
            throw new ParseException(ReturnCodeEnum.DATE_FORMAT_PARSED_ERROR.getText() + e.getMessage(), 0);
        }
        result.put("lastDate", lastDate);
        result.put("batchDate", batchDate);
        result.put("nextDate", nextDate);

        return result;
    }
    
    //找下個月的今天
    @SuppressWarnings("unused")
    private Date getNextMonthDay(Calendar calendar) {
        calendar.add(Calendar.MONTH,1); //Calendar.MONTH 加上1個月(用add Method)
        return calendar.getTime();
    }
    
    //每個月的最後一天日期
    @SuppressWarnings("unused")
    private Date getLastMonthDay(Calendar calendar) {
           calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
           return calendar.getTime();
    }
    
    //找今天的日期
    private String getSysDate() {
        Date today = new Date();
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd");
        return sdFormatter.format(today);
    }
    
    private String getTheBatchDate(String[] strArray, String scheTypeCd) throws ParseException {
        String theBatchDate= "";
        Map<String, String> mapBatchDate = this.getBatchDate(strArray);
        String lastDate = mapBatchDate.get("lastDate");
        String batchDate = mapBatchDate.get("batchDate");
        String nextDate = mapBatchDate.get("nextDate");
        String theDate = "";
        logger.info("lastDate = {} batchDate = {}, nextDate = {}", lastDate, batchDate, nextDate);
        
        if(StringUtils.equals(ScheTypeEnum.ASSIGN_DAY.getValue(), scheTypeCd)){
            theBatchDate = batchDate;
        }else if(StringUtils.equals(ScheTypeEnum.WORKING_DAY.getValue(), scheTypeCd)){
            theBatchDate = batchDate;
        }else if(StringUtils.equals(ScheTypeEnum.AFTER_WORKING_DAY.getValue(), scheTypeCd)){
            if(isEqualToday(batchDate)) {
                theBatchDate = batchDate;
            }else {
                theDate = rdCalendarService.getNextWorkDate(lastDate);
                if(isEqualToday(theDate)) {
                    theBatchDate = theDate;
                }else {
                    theBatchDate = rdCalendarService.getNextWorkDate(batchDate);
                }
            }
        }else if(StringUtils.equals(ScheTypeEnum.BEFORE_WORKING_DAY.getValue(), scheTypeCd)){
            if(isEqualToday(batchDate)) {
                theBatchDate = batchDate;
            }else {
                theDate = rdCalendarService.getBeforeWorkDate(nextDate);
                if(isEqualToday(theDate)) {
                    theBatchDate = theDate;
                }else {
                    theBatchDate = rdCalendarService.getBeforeWorkDate(batchDate);
                }
            }
        }
        return theBatchDate;
    }
    
    /**
     * 依jobTypeCd取得batchDate
     * 
     * @param programId
     * @param jobTypeCd
     * @param triggerFilePath
     * @param triggerFileName
     * @param programName
     * @return
     * @throws ParseException 
     * @throws Exception
     */
    private String getBatchDateByJobTypeCd(String programId, String jobTypeCd, String triggerFilePath,
            String triggerFileName, String programName, String bchDtType, String scheTypeCd, String chkDate)
            throws FileNotFoundException, InterruptJobException, ParseException {
        String theBatchDate = "";
        // JobTypeCd is File
        if (JobTypeEnum.FILE.getValue().equals(jobTypeCd)) {
            Path flagFile = Paths.get(triggerFilePath, triggerFileName);
            // 找不到檔案則不執行
            if (!flagFile.toFile().exists()) {
                throw new FileNotFoundException(programName + " " + ReturnCodeEnum.CANT_READ_FILE.getText());
            }
            String targetFileName = fileOperator.getTargetFileName(flagFile.toFile());
            theBatchDate = fileOperator.getTargetFileDateTimeByFileName(targetFileName, bchDtType);
            
            //* JobTypeCd is "File" unable to check batchDate is after sysDate
            
        // JobTypeCd is ExpFile
        } else if (JobTypeEnum.EXPFILE.getValue().equals(jobTypeCd)) {
            //scheTypeCd == 'A'
            if(StringUtils.equals(ScheTypeEnum.ASSIGN_DAY.getValue(), scheTypeCd)) {
                //且 chkDate是空值則不執行
                if(ObjectUtils.isEmpty(chkDate)) {
                    throw new InterruptJobException(ReturnCodeEnum.CHECK_DATE_IS_EMPTY.getText());
                }
                //對 chkDate 做排序 由小到大
                String[] strArray = getSortArray(chkDate);
                theBatchDate = getTheBatchDate(strArray, scheTypeCd);
                
            //scheTypeCd == 'E'
            }else if(StringUtils.equals(ScheTypeEnum.EVERY_DAY.getValue(), scheTypeCd)) {
                theBatchDate = getSysDate();
                
            //scheTypeCd == 'W'   
            }else {
                if(StringUtils.equals(ScheTypeEnum.WORKING_DAY.getValue(), scheTypeCd)){
                    if(ObjectUtils.isEmpty(chkDate)) {
                        theBatchDate = getSysDate();
                    }else {
                        String[] strArray = getSortArray(chkDate);
                        theBatchDate = getTheBatchDate(strArray, scheTypeCd);
                    }
                //如果scheTypeCd == 'WA||WB'    
                }else {
                    //且 chkDate是空值則不執行
                    if(ObjectUtils.isEmpty(chkDate)) {
                        throw new InterruptJobException(ReturnCodeEnum.CHECK_DATE_IS_EMPTY.getText());
                    }
                    //對 chkDate 做排序 由小到大
                    String[] strArray = getSortArray(chkDate);
                    theBatchDate = getTheBatchDate(strArray, scheTypeCd);
                }
                // 檢查今天是否為營業日
                String busdayFlag = rdCalendarService.getBusdayFlagForCalendarDate(theBatchDate);
                logger.info("busdayFlag = {} theBatchDate = {}", busdayFlag, theBatchDate);
                // busdayFlag != 'Y'
                if (!StringUtils.equals(BusdayFlagEnum.WORKING_DAY.getValue(), busdayFlag)) {
                    //今天不是營業日則不執行
                    throw new InterruptJobException(ReturnCodeEnum.TODAY_IS_NOT_WORKINGDAY.getText());
                }
            }
            LocalDate localBatchDate = LocalDate.parse(theBatchDate, dtfYyyyMMdd);
            logger.info("theBatchDate = {} ", theBatchDate);
            // batchDate > sysDate則不執行
            if (localBatchDate.isAfter(LocalDate.now())) {
                throw new InterruptJobException(ReturnCodeEnum.BATCH_DATE_GREATER_TO_SYS_DATE.getText());
            }
            
        // JobTypeCd is Batch
        } else {
            //scheTypeCd == 'A'
            if(StringUtils.equals(ScheTypeEnum.ASSIGN_DAY.getValue(), scheTypeCd)) {
                //且 chkDate是空值則不執行
                if(ObjectUtils.isEmpty(chkDate)) {
                    throw new InterruptJobException(ReturnCodeEnum.CHECK_DATE_IS_EMPTY.getText());
                }
                //對 chkDate 做排序 由小到大
                String[] strArray = getSortArray(chkDate);
                theBatchDate = getTheBatchDate(strArray, scheTypeCd);
                
            //scheTypeCd == 'E'
            }else if(StringUtils.equals(ScheTypeEnum.EVERY_DAY.getValue(), scheTypeCd)) {
                //batchDate = BCH_JOB_BATCH_DT_STATUS.NEXT_BATCH_DATE by programId 
                Optional<BchJobBatchDtStatus> optionalBchJobBatchDtStatus = bchJobBatchDtStatusService
                    .findByProgramId(programId);
                if (optionalBchJobBatchDtStatus.isPresent()) {
                    theBatchDate = optionalBchJobBatchDtStatus.get().getNextBatchDate();
                } else {// 找不到資料則不執行
                    throw new InterruptJobException(ReturnCodeEnum.CANT_FIND_NEXT_BATCH_DATE.getText());
                }
                
            //scheTypeCd == 'W'   
            }else {
                if(StringUtils.equals(ScheTypeEnum.WORKING_DAY.getValue(), scheTypeCd)){
                    if(ObjectUtils.isEmpty(chkDate)) {
                        theBatchDate = getSysDate();
                    }else {
                        //對 chkDate 做排序 由小到大
                        String[] strArray = getSortArray(chkDate);
                        theBatchDate = getTheBatchDate(strArray, scheTypeCd);
                    }
                //如果scheTypeCd == 'WA||WB'    
                }else {
                    //且 chkDate是空值則不執行
                    if(ObjectUtils.isEmpty(chkDate)) {
                        throw new InterruptJobException(ReturnCodeEnum.CHECK_DATE_IS_EMPTY.getText());
                    }
                    //對 chkDate 做排序 由小到大
                    String[] strArray = getSortArray(chkDate);
                    theBatchDate = getTheBatchDate(strArray, scheTypeCd);
                }
                // 檢查今天是否為營業日
                String busdayFlag = rdCalendarService.getBusdayFlagForCalendarDate(theBatchDate);
                logger.info("busdayFlag = {} theBatchDate = {}", busdayFlag, theBatchDate);
                // busdayFlag != 'Y'
                if (!StringUtils.equals(BusdayFlagEnum.WORKING_DAY.getValue(), busdayFlag)) {
                    //今天不是營業日則不執行
                    throw new InterruptJobException(ReturnCodeEnum.TODAY_IS_NOT_WORKINGDAY.getText());
                }
            }
            LocalDate localBatchDate = LocalDate.parse(theBatchDate, dtfYyyyMMdd);
            logger.info("theBatchDate = {} ", theBatchDate);
            // batchDate > sysDate則不執行
            if (localBatchDate.isAfter(LocalDate.now())) {
                throw new InterruptJobException(ReturnCodeEnum.BATCH_DATE_GREATER_TO_SYS_DATE.getText());
            }
        }
        
        logger.info("ProgramId {} , jobTypeCd = {}, batchDate = {}, scheTypeCd={}", programId, jobTypeCd, theBatchDate, scheTypeCd);
        
        return theBatchDate;
    }

    /**
     * 檢查job是否已完成, true:已完成, false:未完成
     * 
     * @param programId
     * @param batchDate
     * @return
     */
    private boolean checkJobFinishedByBatchDate(String programId, String batchDate) {
        boolean isFinished = false;
        Optional<BchLogJobFinish> optionalBchLogJobFinish = bchLogJobFinishService
                .findByProgramIdAndBatchDateAndSkipFlagAndReturnCode(programId, batchDate, "N",
                        ReturnCodeEnum.SUCCESS.getValue());
        // Check does the job finish
        if (optionalBchLogJobFinish.isPresent()) {
            isFinished = true;
        }
        logger.info("ProgramId {} checkJobFinishedByBatchDate, isFinished = {}, batchDate = {}", programId, isFinished,
                batchDate);
        return isFinished;
    }

    /**
     * 檢查denpendency jobs是否都已完成, true:都已完成, false:有其一未完成
     * 
     * @param programId
     * @param batchDate
     * @return
     * @throws Exception
     */
    private boolean checkDependencyJobsFinished(String programId, String batchDate) {
        boolean isFinished = true;

        List<BchCfgJobDep> listBchCfgJobDep = bchCfgJobDepService.findByEnableFlagAndProgramId("Y", programId);

        // Check these denpendency jobs finish or not
        for (BchCfgJobDep bchCfgJobDep : listBchCfgJobDep) {
            // Get the denpendency job
            Optional<BchCfgJobList> optionalBchCfgJobList = bchCfgJobListService.findByEnableFlagAndProgramId("Y",
                    bchCfgJobDep.getBchCfgJobDepPk().getDepProgramId());

            if (optionalBchCfgJobList.isPresent()
                    && !this.checkJobFinishedByBatchDate(optionalBchCfgJobList.get().getProgramId(), batchDate)) {

                logger.info("DependencyJob programId = {} doesn't finish!", bchCfgJobDep.getBchCfgJobDepPk().getDepProgramId());
                isFinished = false;
                break;
            }
        }

        logger.info("ProgramId {} checkDependencyJobsFinished, isFinished = {}", programId, isFinished);
        return isFinished;
    }

    /**
     * 檢查job是否符合執行日期, true:符合, false:不符合
     * 
     * @param checkDate
     * @return
     */
    private boolean checkJobConformExecDate(String checkDate) {
        boolean isConformed = false;

        if (checkDate == null) {
            isConformed = true;
        } else {
            String[] dateArray = checkDate.split(",");

            // check execute date of job
            for (String theDate : dateArray) {

                if (LocalDate.now().getDayOfMonth() == Integer.parseInt(theDate)) {
                    isConformed = true;
                    break;
                }
            }

            logger.info("CheckDate {} checkJobConformExecDate, isConformed = {}", checkDate, isConformed);
        }

        return isConformed;
    }

    /**
     * 檢查job是否符合執行時間, true:符合, false:不符合
     * 
     * @param checkTime
     * @return
     */
    private boolean checkJobConformExecTime(String checkTime) {
        boolean isConformed = true;

        // check execute time of job
        if (checkTime != null && checkTime.length() >= 4) {
            int hour = Integer.parseInt(checkTime.substring(0, 2));
            int minute = Integer.parseInt(checkTime.substring(2, 4));

            if (LocalTime.now().isBefore(LocalTime.of(hour, minute))) {
                isConformed = false;
            }
            logger.info("CheckTime {} checkJobConformExecTime, isConformed = {}", checkTime, isConformed);
        }

        return isConformed;
    }

    /**
     * 檢查是否為自訂的Exception, true:是, false:不是
     * 
     * @param errMessage
     * @return
     */
    private boolean checkJobCustomException(String errMessage) {
        boolean isConformed = false;

        if (errMessage.indexOf(ReturnCodeEnum.CANT_READ_FILE.getText()) != -1
                || errMessage.indexOf(MonthlyDeductionEnum.DEDUCTION_NOW_NOT_FINISH.getText()) != -1
                || errMessage.indexOf(MonthlyDeductionEnum.STEP0.getText()) != -1) {
            isConformed = true;
        }

        return isConformed;
    }

    /**
     * 設定StepWording
     * 
     * @param programId
     */
    private void setStepWording(String programId) {

        if (ProgramMappingEnum.J0001.getValue().equals(programId)) {
            stepWording = new TrustAcctValueStepWording();
		} else if (ProgramMappingEnum.J0002.getValue().equals(programId)) {
			stepWording = new RoboPromotionsStepWording();
		} else if (ProgramMappingEnum.J0004.getValue().equals(programId)) {
			stepWording = new PartyEmployeeStepWording();
		} else if (ProgramMappingEnum.J0005.getValue().equals(programId)) {
			stepWording = new RdBranchStepWording();
		} else if (ProgramMappingEnum.J0006.getValue().equals(programId)) {
			stepWording = new RdBranchLonStepWording();
		} else if (ProgramMappingEnum.J0007.getValue().equals(programId)) {
			stepWording = new AcctFundTrustHoldingCtfStepWording();
		} else if (ProgramMappingEnum.J0008.getValue().equals(programId)) {
			stepWording = new AcctFundTrustHoldingCtflStepWording();
		} else if (ProgramMappingEnum.J0009.getValue().equals(programId)) {
			stepWording = new PartyCusGroupStepWording();
		} else if (ProgramMappingEnum.J0010.getValue().equals(programId)) {
			stepWording = new PartyFundInvCorpStepWording();
		} else if (ProgramMappingEnum.J0011.getValue().equals(programId)) {
			stepWording = new PartyVipDefiniteStepWording();
		} else if (ProgramMappingEnum.J0012.getValue().equals(programId)) {
			stepWording = new CtfFundStepWording();
		} else if (ProgramMappingEnum.J0013.getValue().equals(programId)) {
			stepWording = new CtflFundStepWording();
		} else if (ProgramMappingEnum.J0015.getValue().equals(programId)) {
			stepWording = new PartyCustomerStepWording();
		} else if (ProgramMappingEnum.J0016.getValue().equals(programId)) {
			stepWording = new PartyPbDefinitedStepWording();
		} else if (ProgramMappingEnum.J0018.getValue().equals(programId)) {
			stepWording = new RdFundCategoryCodesCtflStepWording();
		} else if (ProgramMappingEnum.J0019.getValue().equals(programId)) {
			stepWording = new RdFundCategoryCodesStepWording();
		} else if (ProgramMappingEnum.J0020.getValue().equals(programId)) {
			stepWording = new MgFeeFundRdmptStepWording();
		} else if (ProgramMappingEnum.J0024.getValue().equals(programId)) {
			stepWording = new RoboDeductionAccountStepWording();
		} else if (ProgramMappingEnum.J0025.getValue().equals(programId)) {
			stepWording = new IVpStepWording();
		} else if (ProgramMappingEnum.J0026.getValue().equals(programId)) {
			stepWording = new PfmAssetClassLayerStepWording();
		} else if (ProgramMappingEnum.J0027.getValue().equals(programId)) {
			stepWording = new PfmProdAssetMapStepWording();
		} else if (ProgramMappingEnum.J0028.getValue().equals(programId)) {
			stepWording = new RdBondCategoryCodesStepWording();
		} else if (ProgramMappingEnum.J0029.getValue().equals(programId)) {
			stepWording = new FundListMainStepWording();
		} else if (ProgramMappingEnum.J0030.getValue().equals(programId)) {
			stepWording = new FundListDetailStepWording();
		} else if (ProgramMappingEnum.J0031.getValue().equals(programId)) {
			stepWording = new RdBondCodeStepWording();
		} else if (ProgramMappingEnum.J0032.getValue().equals(programId)) {
			stepWording = new MstarFundListStepWording();
		} else if (ProgramMappingEnum.J0033.getValue().equals(programId)) {
			stepWording = new IpRbDeductionResultDailyStepWording();
		} else if (ProgramMappingEnum.J0034.getValue().equals(programId)) {
			stepWording = new IpMfbDeductionResultDailyStepWording();
		} else if (ProgramMappingEnum.J0035.getValue().equals(programId)) {
			stepWording = new MstarFundListAfstwStepWording();
		} else if (ProgramMappingEnum.J0036.getValue().equals(programId)) {
			stepWording = new MstarFundListAfstwManagerStepWording();
		} else if (ProgramMappingEnum.J0037.getValue().equals(programId)) {
			stepWording = new MstarTwPrimaryListStepWording();
		} else if (ProgramMappingEnum.J0038.getValue().equals(programId)) {
			stepWording = new MstarDailyRtnStepWording();
		} else if (ProgramMappingEnum.J0039.getValue().equals(programId)) {
			stepWording = new MstarDayChangeStepWording();
		} else if (ProgramMappingEnum.J0040.getValue().equals(programId)) {
			stepWording = new MstarPortfolioStepWording();
		} else if (ProgramMappingEnum.J0041.getValue().equals(programId)) {
			stepWording = new MstarCathaybkOdsrptStepWording();
		} else if (ProgramMappingEnum.J0042.getValue().equals(programId)) {
			stepWording = new MstarMonReturn1mBaseStepWording();
		} else if (ProgramMappingEnum.J0043.getValue().equals(programId)) {
			stepWording = new MstarMonReturn1mTwdStepWording();
		} else if (ProgramMappingEnum.J0044.getValue().equals(programId)) {
			stepWording = new MstarMonReturn1mUsdStepWording();
		} else if (ProgramMappingEnum.J0045.getValue().equals(programId)) {
			stepWording = new MstarMonReturn1mBaseStepWording();
		} else if (ProgramMappingEnum.J0046.getValue().equals(programId)) {
			stepWording = new MstarMonReturn1mBaseStepWording();
		} else if (ProgramMappingEnum.J0047.getValue().equals(programId)) {
			stepWording = new MstarMonReturn1mBaseStepWording();
		} else if (ProgramMappingEnum.J0048.getValue().equals(programId)) {
			stepWording = new MstarMonReturn6mBaseStepWording();
		} else if (ProgramMappingEnum.J0049.getValue().equals(programId)) {
			stepWording = new MstarMonReturn6mTwdStepWording();
		} else if (ProgramMappingEnum.J0050.getValue().equals(programId)) {
			stepWording = new MstarMonReturn6mUsdStepWording();
		} else if (ProgramMappingEnum.J0051.getValue().equals(programId)) {
			stepWording = new ReutersRtsEodStepWording();
		} else if (ProgramMappingEnum.J0052.getValue().equals(programId)) {
			stepWording = new ReutersRtsIntradayStepWording();
		} else if (ProgramMappingEnum.J0053.getValue().equals(programId)) {
			stepWording = new PartyCusDrvStepWording();
		} else if (ProgramMappingEnum.J0054.getValue().equals(programId)) {
			stepWording = new PartyDmRejectDetStepWording();
		} else if (ProgramMappingEnum.J0055.getValue().equals(programId)) {
			stepWording = new PartyFundInvCorpCtflStepWording();
		} else if (ProgramMappingEnum.J0056.getValue().equals(programId)) {
			stepWording = new AcctBondTrustHoldingStepWording();
		} else if (ProgramMappingEnum.J0057.getValue().equals(programId)) {
			stepWording = new PartyAcctDrvDpStepWording();
		} else if (ProgramMappingEnum.J0058.getValue().equals(programId)) {
			stepWording = new BbgRdEtfCodeStepWording();
		} else if (ProgramMappingEnum.J0059.getValue().equals(programId)) {
			stepWording = new PartyDrvVipStepWording();
		} else if (ProgramMappingEnum.J0060.getValue().equals(programId)) {
			stepWording = new PromCustMaListStepWording();
		} else if (ProgramMappingEnum.J0061.getValue().equals(programId)) {
			stepWording = new PartyAcctDrvLbStepWording();
		} else if (ProgramMappingEnum.J0062.getValue().equals(programId)) {
			stepWording = new RptCommonFammStepWording();
		} else if (ProgramMappingEnum.J0063.getValue().equals(programId)) {
			stepWording = new PartyAcctDrvLbStepWording();
		} else if (ProgramMappingEnum.J0070.getValue().equals(programId)) {
			stepWording = new Pbf000StepWording();
		}else if (ProgramMappingEnum.J0071.getValue().equals(programId)) {
			stepWording = new PartyInfoDpStepWording();
		}else if (ProgramMappingEnum.J0072.getValue().equals(programId)) {
			stepWording = new MgAcctLonStepWording();
		}else if (ProgramMappingEnum.J0073.getValue().equals(programId)) {
			stepWording = new CtfAgreepromoteStepWording();
		}else if (ProgramMappingEnum.J0074.getValue().equals(programId)) {
			stepWording = new PartyDrvWsFeeDetailStepWording();
		}else if (ProgramMappingEnum.J0075.getValue().equals(programId)) {
			stepWording = new AcctDrvMfStepWording();
		}else if (ProgramMappingEnum.J0077.getValue().equals(programId)) {
            stepWording = new PartyPledgeStepWording();
        }else if (ProgramMappingEnum.J0078.getValue().equals(programId)) {
            stepWording = new AcctDrvMfStepWording();
        }else if (ProgramMappingEnum.J0079.getValue().equals(programId)) {
            stepWording = new AcctDataboxTableStepWording();
        }else if (ProgramMappingEnum.J0080.getValue().equals(programId)) {
            stepWording = new EmpDataboxTableStepWording();
        }else if (ProgramMappingEnum.J0083.getValue().equals(programId)) {
            stepWording = new UserAuditLogStepWording();
        }
	}
}
