package gtu.ireport.ex1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaybk.invf.rest.jasper.util.INVFundJasperReportUtil;
import com.cathaybk.invf.rest.jasper.util.INVFundJasperReportUtil.PrintModel;
import com.cathaybk.invf.rest.service.util.ListUtil;
import com.cathaybk.invf.rest.service.util.NumberUtil;
import com.cathaybk.invf.rest.service.util.PaddingUtils;
import com.cathaybk.invf.rest.soap.service.brancs.BANCSServiceClient;
import com.cathaybk.invf.rest.soap.service.brancs.dto_real.CubxmlResp;
import com.cathaybk.invf.rest.soap.service.cif_get_idtype.CifGetIDTypeClient;
import com.cathaybk.invf.rest.soap.service.cif_get_idtype.dto_real.Cif6idcxml;
import com.cathaybk.invf.rest.sqlserver.CTF.repository.casual.ApplicationUnionCTFRepoQuery;
import com.cathaybk.invf.rest.sqlserver.CTF.repository.casual.ReportDoc016CTFRepoQuery;
import com.cathaybk.invf.rest.sqlserver.CTF.repository.casual.ReportDoc017CTFRepoQuery;
import com.cathaybk.invf.rest.sqlserver.dto.ApplicationUnionDTO;
import com.cathaybk.invf.rest.svc.InvSvcPlatformService;
import com.cathaybk.invf.rest.svc.dto.CifDataCustomerAmtRequestDto;
import com.cathaybk.invf.rest.svc.dto.CifDataCustomerAmtResponseDto;
import com.cathaybk.invf.rest.svc.etc.SimpleTokenUtil;

@Service
public class INV_RptDoc016_17 implements AbstractBaseRpt {

    @Autowired
    private INVFundJasperReportUtil mINVFundJasperReportUtil;
    @Autowired
    public ApplicationUnionCTFRepoQuery mApplicationUnionCTFRepoQuery;
    @Autowired
    public ReportDoc016CTFRepoQuery mReportDoc016CTFRepoQuery;
    @Autowired
    public ReportDoc017CTFRepoQuery mReportDoc017CTFRepoQuery;
    @Autowired
    public InvSvcPlatformService mInvSvcPlatformService;
    @Autowired
    public CifGetIDTypeClient mCifGetIDTypeClient;
    @Autowired
    public BANCSServiceClient mBANCSServiceClient;

    @Override
    public byte[] processRpt(Map<String, Object> parameters) throws Exception {
        String ApplicationID = MapUtils.getString(parameters, "applicationID");

        ApplicationUnionDTO applicationDTO = ListUtil
            .getFirstOne(mApplicationUnionCTFRepoQuery.findResult(ApplicationID), ApplicationUnionDTO.class);

        Map<String, Object> queryParameter = new HashMap<>();
        String docNo = "";
        if (NumberUtil.getBigDecimal(applicationDTO.getNegoRate()).doubleValue() <= 3D) {
            docNo = "DOC017";
            queryParameter = ListUtil.getFirstOne(mReportDoc017CTFRepoQuery.findResultForMap(ApplicationID));
        } else {
            docNo = "DOC016";
            queryParameter = ListUtil.getFirstOne(mReportDoc016CTFRepoQuery.findResultForMap(ApplicationID));
        }

        fixParamter(queryParameter, applicationDTO.getCustomerID(), applicationDTO.getBranchID());

        List<PrintModel> listPrint = new ArrayList<>();

        PrintModel printModel = mINVFundJasperReportUtil.generatePrintModel(docNo, queryParameter);
        listPrint.add(printModel);

        byte[] resultArry = mINVFundJasperReportUtil.generateByteArray("XXXXXX", listPrint);
        return resultArry;
    }

    private CifDataCustomerAmtResponseDto getInsuranceAmtAndContributionAmt(String custId) {
        CifDataCustomerAmtRequestDto requestDto = new CifDataCustomerAmtRequestDto();
        requestDto.setCustId(custId);
        String token = SimpleTokenUtil.getToken("", "", "", "", "", "", "");
        CifDataCustomerAmtResponseDto restDto = mInvSvcPlatformService.getCustomerAmtByRest(requestDto, token);
        return restDto;
    }

    private void fixParamter(Map<String, Object> queryParameter, String custId, String branchId) {
        String identityID = MapUtils.getString(queryParameter, "IDENTITYID");
        queryParameter.put("IDENTITYID", PaddingUtils.getIdendityIDChs(identityID));

        CifDataCustomerAmtResponseDto restDto = getInsuranceAmtAndContributionAmt(custId);
        queryParameter.put("INSURANCEAMT", restDto.getData().getInsuranceBal());
        queryParameter.put("CONTRIBUTIONAMT", restDto.getData().getNetRevenues());

        {
            Cif6idcxml cifDto = mCifGetIDTypeClient.sendSoap(custId);
            String identity = cifDto.getIdcReturn().getIdentity();

            CubxmlResp brancDto = mBANCSServiceClient.sendSoap(branchId, "03077", identity, custId);
            String txnRelind = brancDto.getTranrs().getTxnrelind();

            String txnMsg = "";
            if ("Y".equalsIgnoreCase(txnRelind)) {
                txnMsg = "是(請檢附低於上述議價手續費率之同類型商品之歷史議價資料，此筆議價資料須等同或低於上述利害關係人議價後申購手續費率，使得進行交易)";
            } else {
                txnMsg = "否";
            }
            queryParameter.put("TXNRELIND_TEXT", txnMsg);
        }
        queryParameter.put("PAGE", "1");

        queryParameter.put("CHECKCOND1", "");
        queryParameter.put("CHECKCOND2", "");
        queryParameter.put("CHECKCOND3", "");

        if (StringUtils.defaultString(MapUtils.getString(queryParameter, "NEGOWORKNO")).length() > 0) {
            queryParameter.put("CHECKCOND3", "V");
        }
        if (StringUtils.defaultString(MapUtils.getString(queryParameter, "CUSTOMER_ID")).length() > 0) {
            queryParameter.put("CHECKCOND2", "V");
        }
        if (NumberUtil.getBigDecimal(MapUtils.getString(queryParameter, "TrustCapital2")).multiply(
            NumberUtil.getBigDecimal(MapUtils.getString(queryParameter, "ExRate")))
            .compareTo(new BigDecimal(50000000)) >= 0) {
            queryParameter.put("CHECKCOND1", "V");
        }
        
        this.setterDefaultEmptyParamters(queryParameter);
    }

    private void setterDefaultEmptyParamters(Map<String, Object> parameters) {
        List<String> params = new ArrayList<>();
        params.add("APPLICATIONDATE");
        params.add("CANVASSER");
        params.add("CHECKCOND1");
        params.add("CHECKCOND2");
        params.add("CHECKCOND3");
        params.add("CONTRIBUTIONAMT");
        params.add("CUSTOMERID");
        params.add("CUSTOMERNAME");
        params.add("CUSTOMERNAME");
        params.add("CUS_BRANCHNAME");
        params.add("FEEBANKORG");
        params.add("FEEINVCORPORG");
        params.add("FEETOTAL");
        params.add("FUNDID");
        params.add("FUNDNAME");
        params.add("IDENTITYID");
        params.add("INSURANCEAMT");
        params.add("NEGOFEERATE");
        params.add("NEGOMESSAGE");
        params.add("NEGONO");
        params.add("NEGORATE");
        params.add("NEGOREASON");
        params.add("NEGOWORKNO");
        params.add("ORDERDATE");
        params.add("PURCHASEAMT");
        params.add("RAWFEERATE");
        params.add("TRUSTACCT");
        params.add("TRUSTCAPITAL");
        params.add("TXNRELIND_TEXT");

        for (String key : params) {
            if (parameters.containsKey(key)) {
                Object val = parameters.get(key);
                if (val == null) {
                    parameters.put(key, "");
                }
            }
        }
    }

}
