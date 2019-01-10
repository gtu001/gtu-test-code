<#import "/lib.ftl" as my>  

package com.cathay.xx.zx.module;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import com.cathay.common.exception.ErrorInputException;
import com.cathay.common.exception.ModuleException;
import com.cathay.common.im.util.VOTool;
import com.cathay.common.service.authenticate.UserObject;
import com.cathay.common.util.DATE;
import com.cathay.common.util.db.DBUtil;
import com.cathay.rz.n0.module.RZ_N0Z001;
import com.cathay.util.Transaction;
import com.cathay.xx.vo.DTXXTP01;
import com.igsapp.db.DataSet;

/**
 * <pre>
 * DATE	Version	Description	Author
 * ${my.sysdate()}	1.0	Create	�i�·�
 * 
 * �@�B	�{���\�෧�n�����G
 * �ҲզW��	${model_chinese}
 * �Ҳ�ID	${model_clz}
 * ���n����	${model_desc}
 * 
 * </pre>
 * 
 * @author 
 * 
 */
@SuppressWarnings("unchecked")
public class ${model_clz} {
	
	/** �R�A�� log ���� **/
    private static final Logger log = Logger.getLogger(${model_clz}.class);

    private static final String SQL_query_001 = "com.cathay.xx.zx.module.XX_ZX0100.SQL_query_001";

    private static final String SQL_insert_001 = "com.cathay.xx.zx.module.XX_ZX0100.SQL_insert_001";

    private static final String SQL_update_001 = "com.cathay.xx.zx.module.XX_ZX0100.SQL_update_001";

    private static final String SQL_delete_001 = "com.cathay.xx.zx.module.XX_ZX0100.SQL_delete_001";

    /**
     * �d�߸��
     * @param reqMap
     * @return
     * @throws ModuleException
     */
    public List<Map> query(Map reqMap) throws ModuleException {
        <#list searchColumns as col>
        String ${col} = MapUtils.getString(reqMap, "${col}"); 
        </#list>
		
		DataSet ds = Transaction.getDataSet();

        <#list searchColumns as col>
        if (StringUtils.isNotEmpty(${col})) {
            <#if ! searchColumns_like?seq_contains(col)>
            ds.setField("${col}", ${col});
            <#else>
            ds.setField("${col}", new StirngBuilder().append("%").append(${col}).append("%").toString());
            </#if>
        }
        </#list>
        ds.setField("DIV_NO", user.getOpUnit());

        DBUtil.searchAndRetrieve(ds, SQL_queryNonCashChkList_001);
		List<Map> rtnList = new ArrayList<Map>();
        while (ds.next()) {
            Map dataMap = VOTool.dataSetToMap(ds);
            rtnList.add(dataMap);
        }
		//List<Map> rtnList = VOTool.findToMaps(ds, SQL_query_001);
		
		// ���o�f�媬�A����
        new RZ_N0Z001().putOP_STATUS_NM(rtnList, "XXZX0101");
        return rtnList;
    }

    /**
     * �s�W���
     * @param reqMap
     * @param user
     * @throws ParseException 
     * @throws ModuleException 
     */
    public void insert(Map reqMap, UserObject user) throws ModuleException {
		//�w��ǤJ�Ѽƶi���ˮ�
        ErrorInputException eie = null;
        if (reqMap == null || reqMap.isEmpty()) {
            eie = getErrorInputException(eie, "�ǤJ��T���i����");
        }
        if (user == null) {
            eie = getErrorInputException(eie, "�ϥΪ̸�T���i����");
        }
        if (eie != null) {
            throw eie;
        }
		
		// �Y�ˮֵL�~
		<#list insertColumns_pk as col>
		String ${col} = MapUtils.getString(reqMap, "${col}");
        if (StringUtils.isBlank(${col})) {
            eie = getErrorInputException(eie, "${columnLabel[col]}���i����");
        }
		</#list>
		if (eie != null) {
            throw eie;
        }
		
		<#list insertColumns as col>
		<#if ! insertColumns_pk?seq_contains(col)>
		String ${col} = MapUtils.getString(reqMap, "${col}"); 
        </#if>
        </#list>

        DataSet ds = Transaction.getDataSet();
        <#list insertColumns as col>
		<#if ! insertColumns_pk?seq_contains(col)>
        if (StringUtils.isNotEmpty(${col})) {
            ds.setField("${col}", ${col});
        }
        </#if>
        </#list>
		
		<#list insertColumns as col>
		<#if insertColumns_pk?seq_contains(col)>
        ds.setField("${col}", ${col});
        </#if>
        </#list>
				
        //hard code ����������
        Timestamp UPDT_DATE = DATE.currentTime();
		String FLOW_NO = new RZ_N0Z001().startFlow("XXZX0101", "�s�W", "", UPDT_ID, user.getDivNo());
        String UPDT_ID = user.getEmpID();
        
        ds.setField("OP_STATUS", 10);
        ds.setField("UPDT_ID", UPDT_ID);
        ds.setField("UPDT_DATE", UPDT_DATE);
		ds.setField("FLOW_NO", FLOW_NO);
		
		//�N��Ʒs�W�� ����� 
        DBUtil.executeUpdate(ds, SQL_insert_001, false);
		
    }

    /**
     * ��s�H���򥻸��
     * @param reqMap
     * @param user
     * @throws ModuleException 
     */
    public void update(Map reqMap, UserObject user) throws ModuleException {
		//�w��ǤJ�Ѽƶi���ˮ�
		ErrorInputException eie = null;
        if (reqMap == null || reqMap.isEmpty()) {
            eie = getErrorInputException(eie, "�ǤJ��T���i����");
        }
        if (user == null) {
            eie = getErrorInputException(eie, "�ϥΪ̸�T���i����");
        }
        if (eie != null) {
            throw eie;
        }
				
		// �Y�ˮֵL�~
		<#list updateColumns_pk as col>
		String ${col} = MapUtils.getString(reqMap, "${col}");
        if (StringUtils.isBlank(${col})) {
            eie = getErrorInputException(eie, "${columnLabel[col]}���i����");
        }
		</#list>
		if (eie != null) {
            throw eie;
        }
		
		<#list updateColumns as col>
		<#if ! updateColumns_pk?seq_contains(col)>
		String ${col} = MapUtils.getString(reqMap, "${col}"); 
        </#if>
        </#list>

        DataSet ds = Transaction.getDataSet();
        <#list updateColumns as col>
		<#if ! updateColumns_pk?seq_contains(col)>
        if (StringUtils.isNotEmpty(${col})) {
            ds.setField("${col}", ${col});
        }
        </#if>
        </#list>
        
        <#list updateColumns_pk as col>
        ds.setField("${col}", ${col});
        </#list>
        
        //hard code ����������
        ds.setField("UPDT_ID", user.getEmpID());
        ds.setField("UPDT_DATE", UPDT_DATE);
        
		//�N��Ƨ�s�� ����� 
        DBUtil.executeUpdate(ds, SQL_update_001, false);
    }

    /**
     * �R�����u�򥻸��
     * @param EMP_ID
     * @throws ModuleException 
     */
    public void delete(String EMP_ID) throws ModuleException {
		//�w��ǤJ�Ѽƶi���ˮ�
        ErrorInputException eie = null;
        <#list deleteColumns_pk as col>
        if (StringUtils.isEmpty(${col})) {
            throw new ErrorInputException("${columnLabel[col]}���i����");
            eie = getErrorInputException(eie, "${columnLabel[col]}���i����");
        }
        </#list>
        if (eie != null) {
            throw eie;
        }
		
		DataSet ds = Transaction.getDataSet();
		<#list deleteColumns_pk as col>
        ds.setField("${col}", ${col});
        </#list>
        
		//�̶ǤJEMP_ID�R�� �H���򥻸���� DBXX.DTXXTP01 ���
        DBUtil.executeUpdate(ds, SQL_delete_001, false);
    }

    /**
     * ����
     * @param vo
     * @throws ModuleException
     */
    public void confirm(${model_vo_clz} vo) throws ModuleException {
        if (vo == null) {
            throw new ErrorInputException("${model_chinese}��Ƥ��i����");
        }
        <#list approveColumns_pk as col>
        if (StringUtils.isEmpty(vo.get${col}())) {
            throw new ErrorInputException("${columnLabel[col]}���i����");
        }
        </#list>
        
        //��s���A�ݼf�� 20
        vo.setOP_STATUS(20);
        VOTool.update(vo);
    }

    /**
     * �f��
     * @param vo
     * @throws ModuleException
     */
    public void approve(${model_vo_clz} vo) throws ModuleException {
        if (vo == null) {
            throw new ErrorInputException("${model_chinese}��Ƥ��i����");
        }
        <#list approveColumns_pk as col>
        if (StringUtils.isEmpty(vo.get${col}())) {
            throw new ErrorInputException("${columnLabel[col]}���i����");
        }
        </#list>
        
        //��s���A �ܿ�J��30
        vo.setOP_STATUS(30);
        VOTool.update(vo);
    }

    /**
     * �h�^
     * @param vo
     * @throws ModuleException
     */
    public void reject(${model_vo_clz} vo) throws ModuleException {
        if (vo == null) {
            throw new ErrorInputException("${model_chinese}��Ƥ��i����");
        }
        <#list approveColumns_pk as col>
        if (StringUtils.isEmpty(vo.get${col}())) {
            throw new ErrorInputException("${columnLabel[col]}���i����");
        }
        </#list>
        
        //��s���A �ܿ�J��10
        vo.setOP_STATUS(10);
        VOTool.update(vo);
    }

    /**
     * ���oEIE����
     * @param eie
     * @param errMsg
     * @return
     */
    private ErrorInputException getErrorInputException(ErrorInputException eie, String errMsg) {
        if (eie == null) {
            eie = new ErrorInputException();
        }
        eie.appendMessage(errMsg);
        return eie;
    }
}
