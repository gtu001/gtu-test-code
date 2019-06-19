package gtu.ireport.ex1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cathay.common.exception.DataNotFoundException;
import com.cathay.common.util.DATE;
import com.cathaybk.invf.rest.attr.CubxmlTag;
import com.cathaybk.invf.rest.attr.ModuleTag;
import com.cathaybk.invf.rest.attr.ThreadLocalAttr;
import com.cathaybk.invf.rest.jasper.service.cipher.AbstractBaseRpt;
import com.cathaybk.invf.rest.module.INVF_O3Z001;
import com.cathaybk.invf.rest.util.ReturnCodeUtil;
import com.cathaybk.invf.rest.util.ThreadLocalManager;

/**
 * INVG-rest端口 http://localhost:8080/swagger-ui.html
 * 
 * @author INVG-rest小組
 *
 */
@RestController
@RequestMapping(value = "/INVF-jasper-rpt")
public class INVFJasperRptController {

    private static final Logger LOG = LogManager.getLogger(INVFJasperRptController.class);

    @Autowired
    private INVF_O3Z001 INVF_O3Z001;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    ThreadLocalManager threadLocalManager;

    @RequestMapping(value = "/report001", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> query2(@RequestBody Map<String, Object> reqMap, HttpServletRequest req) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> cubxmlMap = MapUtils.getMap(reqMap, CubxmlTag.CUBXML);
        Map<String, Object> mwHeaderMap = MapUtils.getMap(cubxmlMap, CubxmlTag.MWHEADER);
        String msgId = MapUtils.getString(mwHeaderMap, "MSGID", "").trim();
        try {
            Map<String, Object> reqTranrq = (Map<String, Object>) cubxmlMap.get(CubxmlTag.TRANRQ);
            String rptKey = MapUtils.getString(reqTranrq, "rptKey");

            RptEnum rpt = null;
            for (RptEnum e : RptEnum.values()) {
                if (StringUtils.equals(e.name(), rptKey)) {
                    rpt = e;
                    break;
                }
            }

            if (rpt != null) {
                AbstractBaseRpt bean = (AbstractBaseRpt) autowireCapableBeanFactory.getBean(rpt.rptClz);
                byte[] resultArry = bean.processRpt(reqTranrq);
                return new ResponseEntity<>(resultArry, HttpStatus.OK);
            } else {
                throw new Exception("查無此報表 : " + rptKey);
            }
        } catch (DataNotFoundException dnfe) {
            String errMsg = dnfe.getMessage();
            LOG.error(errMsg, dnfe);
            ReturnCodeUtil.setReturnMsg(mwHeaderMap, "0188", errMsg);
            setErrMsg(result, mwHeaderMap);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            LOG.error(errMsg, e);
            ReturnCodeUtil.setReturnMsg(mwHeaderMap, "9999", "交易失敗 : " + errMsg);
            setErrMsg(result, mwHeaderMap);
        } finally {
            try {
                threadLocalManager.setThreadLocal(ThreadLocalAttr.RES_DATA, result);
                writeLog();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            } finally {
                threadLocalManager.removeThreadLocal();
            }
        }
        return new ResponseEntity<>(null, HttpStatus.METHOD_FAILURE);
    }

    private void setErrMsg(Map<String, Object> result, Map<String, Object> mwHeaderMap) {
        Map<String, Object> returnCubxmlMap = new HashMap<>();
        Map<String, Object> reqTranrq = threadLocalManager.getThreadLocal(ThreadLocalAttr.TRANRQ);
        returnCubxmlMap.put(CubxmlTag.MWHEADER, mwHeaderMap);
        returnCubxmlMap.put(CubxmlTag.TRANRS, reqTranrq);
        result.put(CubxmlTag.CUBXML, returnCubxmlMap);
    }

    private void writeLog() {
        try {
            Map<String, Object> logMap = new HashMap<>();
            logMap.putAll(threadLocalManager.getThreadLocal(ThreadLocalAttr.TRANRQ));
            logMap.putAll(threadLocalManager.getThreadLocal(ThreadLocalAttr.MWHEADER));

            HttpServletRequest req = threadLocalManager.getThreadLocal(ThreadLocalAttr.HTTP_SERVLET_REQ);
            String channelIP = req.getRemoteAddr();
            String localIP;
            try {
                localIP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                localIP = null;
            }
            String txnSeq = UUID.randomUUID().toString();

            logMap.put(ModuleTag.TXN_SEQ, StringUtils.substring(txnSeq.replace("-", ""), 0, 12)); // UUID
            logMap.put(ModuleTag.FINAL, "Y");
            logMap.put(ModuleTag.REQ_DATA, threadLocalManager.getThreadLocal(ThreadLocalAttr.CUBXML));
            logMap.put(ModuleTag.RES_DATA, threadLocalManager.getThreadLocal(ThreadLocalAttr.RES_DATA));
            logMap.put(ModuleTag.REQ_DATA_TIME, threadLocalManager.getThreadLocal(ThreadLocalAttr.REQ_DATE_TIME));
            logMap.put(ModuleTag.RES_DATA_TIME, DATE.currentTime());
            logMap.put(ModuleTag.LOCAL_IP, localIP);
            logMap.put(ModuleTag.CHANNEL_IP, channelIP);

            INVF_O3Z001.insertFundServiceLog(logMap);
        } catch (Exception e) {
            LOG.error("紀錄LOG錯誤:" + e.getMessage());
        }
    }

}
