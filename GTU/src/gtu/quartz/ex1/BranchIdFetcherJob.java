package cub.inv.query.ui.job.branchid;

import java.util.List;

import javax.annotation.Resource;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import cub.inv.query.ui.dto.rest.fund.FundQryBranchRespRequestDto;
import cub.inv.query.ui.dto.rest.fund.FundQryBranchRespResponseDto;
import cub.inv.query.ui.dto.web.fund.FundQryBranchWebRequestDto;
import cub.inv.query.ui.dto.web.fund.FundQryBranchWebResponseDto;
import cub.inv.query.ui.dto.web.fund.FundQryBranchWebResponseDto.FundQryBranchWebResponseBranchDto;
import cub.inv.query.ui.processor.fund.FundQryBranchProcessor;
import cub.inv.query.ui.service.InvfQueryService;

@Component
public class BranchIdFetcherJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(BranchIdFetcherJob.class);

    @Autowired
    private FundQryBranchProcessor fundQryBranchProcessor;

    @Autowired
    private InvfQueryService invfQueryService;
    
    @Resource
    private BranchIdFetcherJob self;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("#[BranchIdFetcherJob] start!, Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");

        // ------------------------------------------------------------------business
        // ↓↓↓↓↓↓
        self.getBranchLstUpdate();
        // ------------------------------------------------------------------business
        // ↑↑↑↑↑↑
        logger.info("#[BranchIdFetcherJob] end!", jobExecutionContext.getJobDetail().getKey());
    }

    @Cacheable("getBranchLstKeeper")
    public List<FundQryBranchWebResponseBranchDto> getBranchLstKeeper() {
        return getBranchLst();
    }

    @CachePut("getBranchLstKeeper")
    public List<FundQryBranchWebResponseBranchDto> getBranchLstUpdate() {
        return getBranchLst();
    }

    private List<FundQryBranchWebResponseBranchDto> getBranchLst() {
        logger.info("#[BranchIdFetcherJob] getBranchLst! start!");
        // 處理webRequestDto
        FundQryBranchRespRequestDto restRequestDto = fundQryBranchProcessor
            .generateRestRequestDto(new FundQryBranchWebRequestDto());

        // 發送rest request
        FundQryBranchRespResponseDto restResponseDto = (FundQryBranchRespResponseDto) invfQueryService
            .queryFundDataByRest(restRequestDto, FundQryBranchRespResponseDto.class);

        FundQryBranchWebResponseDto webResponseDto = fundQryBranchProcessor
            .generateWebResponseDto(restResponseDto);

        logger.info("#[BranchIdFetcherJob] getBranchLst! end!");
        return webResponseDto.getBranchLst();
    }
}
