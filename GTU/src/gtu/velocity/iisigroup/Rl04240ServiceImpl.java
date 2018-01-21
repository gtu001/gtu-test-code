package com.iisigroup.ris.${DOMAIN}.${PACKAGE}.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ${chineseShortName}服務
 * 
 * @author ${yourName}
 */
@Service("${LOWER_ID}Service")
public class ${UPPER_ID}ServiceImpl implements ${UPPER_ID}Service {

    private static Logger log = LoggerFactory.getLogger(${UPPER_ID}ServiceImpl.class);
    
    @Autowired
    private ReportingComponent reportingComponent;

    @Autowired
    private transient RisCommon risCommon;

    #set ($full_upper_case_id = ${LOWER_ID})
    private static final String REPORT_NAME = "$full_upper_case_id.toUpperCase()";

    #foreach( $key in $SERVICE_METHODS.keySet() )
    @Override
    public $SERVICE_METHODS.get($key) $key(${UPPER_ID}DTO ${LOWER_ID}DTO) throws RisBusinessException {
        log.debug("# service - $key...");
        return null;
    }
    #end

    @Override
    public String doPrint(${UPPER_ID}DTO ${LOWER_ID}dto, ExecutantType executantType) throws RisBusinessException {
        log.info("# doPrint ..");

        final String transactionId = risCommon.getTransactionId(executantType);

        final HashMap<String, String> reportParameters = new HashMap<String, String>();
        reportParameters.put("TRANSACTION_ID", transactionId);
        reportParameters.put("fileFormat", ${LOWER_ID}dto.getReportType());
        reportParameters.put("dataYear", ${LOWER_ID}dto.getDataYear());
        reportParameters.put("dataMonth", ${LOWER_ID}dto.getDataMonth());
        reportParameters.put("printArea", ${LOWER_ID}dto.getPrintArea());
        reportParameters.put("printArea", ${LOWER_ID}dto.getIsSendMessage());

        final String reportUrl = reportingComponent.printData(REPORT_NAME, reportParameters, executantType);

        return reportUrl;
    }
}
