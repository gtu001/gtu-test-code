// D:\workspace_gtu001_bak\WebTest\target\classes
package gtu.db.oracle;
 
import gtu.log.Logger2File;
import gtu.log.finder.DebugMointerUI;
 
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
 
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
 
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
 
import com.ebao.ls.arap.pub.CastUtils;
 
public class PageQuery_EbaoTest {
    private static Logger2File logger = DebugMointerUI.getLogger();
 
    @Resource(name = "1")
    javax.servlet.http.HttpServletRequest request;
 
    @Resource(name = "2")
    com.ebao.foundation.module.web.filter.HttpResponseWrapper response;
 
    @Resource(name = "3")
    com.ebao.ls.arap.cash.ctrl.agency.bankagency.BankAgencyLinkDetailForm myform;
 
    @Resource(name = "4")
    com.ebao.foundation.module.configframework.support.spring.LazyWebXmlApplicationContext context;
 
    public Object parser(javax.servlet.http.HttpServletRequest arg1, com.ebao.foundation.module.web.filter.HttpResponseWrapper arg2,
                    com.ebao.ls.arap.cash.ctrl.agency.bankagency.BankAgencyLinkDetailForm arg3, com.ebao.foundation.module.configframework.support.spring.LazyWebXmlApplicationContext arg4) {
//        final String textId = request.getParameter("textId");
        final String textId = "481";
        PageQueryHandler<Map<String, Object>> pqh = new PageQueryHandler<Map<String, Object>>() {
            @Override
            Integer getTotalCount() {
                return queryForLinkDetail_count(textId);
            }
 
            @Override
            List<Map<String, Object>> doQuery(Integer startItemNo, Integer endItemNo) {
                return queryForLinkDetail(textId, startItemNo, endItemNo);
            }
        };
 
        List<Map<String, Object>> queryList = pqh.process(request);
        myform.setList1(queryList);
 
        // logger.close();
        logger.showFile();
        return null;
    }
 
    private enum UnpurposeCodeEnum {
        D_1("查無保單號碼"), D_2("幣別不符");
        final String chs;
 
        final String setBackField = "UNPURPOSE_CODE_CHS";
 
        UnpurposeCodeEnum(String chs) {
            this.chs = chs;
        }
    }
 
    private enum StatusEnum {
        Y("執行成功"), N("執行失敗"), C("執行中");
        final String chs;
 
        final String setBackField = "STATUS_CHS";
 
        StatusEnum(String chs) {
            this.chs = chs;
        }
    }
 
    private enum InnerCstCodeEnum {
        STATUS("StatusEnum"),
        UNPURPOSE_CODE("UnpurposeCodeEnum"), ;
        final String enu;
 
        InnerCstCodeEnum(String enu) {
            this.enu = enu;
        }
    }
   
    /**
     * 附加中文欄位
    */
    private void appendCstCodeChinese(List<Map<String,Object>> queryList){
        for (Map<String, Object> m : queryList) {
            logger.debug(" - " + m);
 
            for (InnerCstCodeEnum c : InnerCstCodeEnum.values()) {
                if (m.containsKey(c.name())) {
                    if (m.get(c.name()) != null) {
                        String v = String.valueOf(m.get(c.name()));
 
                        Class[] cs = this.getClass().getDeclaredClasses();
                        for (Class c2 : cs) {
                            if (c2.getSimpleName().equals(c.enu)) {
                               
                                String matchV = v;
                                if(v.matches("^\\d+\\w*$")){
                                    matchV = "D_" + v;
                                }
                                Enum c2Enu = Enum.valueOf(c2, matchV);
                               
                                try {
                                    Field f = c2Enu.getDeclaringClass().getDeclaredField("chs");
                                    String v2 = (String) f.get(c2Enu);
 
                                    Field f2 = c2Enu.getDeclaringClass().getDeclaredField("setBackField");
                                    String v3 = (String) f2.get(c2Enu);
 
                                    m.put(v3, v2);
 
                                    logger.debug("setBack - " + v3 + " = " + v2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
 
    /**
     * 查分頁sql
    */
    private List<Map<String, Object>> queryForLinkDetail(String textId, int startNo, int endNo) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from (select rownum mnum,obj.* from ( ");// Pager
        queryForLinkDetail_whereConditionAppender(sql, textId);
        sql.append(" ) obj ) where mnum >= " + startNo + " and mnum <= "
                        + endNo + " ");// Pager
 
        Session session = com.ebao.foundation.module.db.hibernate.HibernateSession3
                        .currentSession();
        SQLQuery q = session.createSQLQuery(sql.toString());
        q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = q.list();
        return list;
    }
 
    /**
     * 共用sql
    */
    private void queryForLinkDetail_whereConditionAppender(StringBuffer sql, String textId) {
        sql.append("    select                                  ");
        sql.append("        t.fee_id,                           ");// FEE_ID
        sql.append("        t.text_id,                          ");// 批號
        sql.append("        t.detail_id,                        ");// 明細號
        sql.append("        t.entry_code as policy_code,        ");// 保單號
        sql.append("        k.money_id,                         ");
        sql.append("        t.PAY_SOURCE,                       ");
        sql.append("        t.PAY_MODE,                         ");
        sql.append("        t.prem_purpose,                     ");
        sql.append("        t.fee_amount,                       ");// 金額
        sql.append("        t.bill_no,                          ");
        sql.append("        t.transfer_date,                    ");
        sql.append("        t.status,                           ");// 狀態
        sql.append("        t.unpurpose_code,                   ");// 無法入懸帳
        sql.append("        t.update_time,                      ");
        sql.append("        t.updated_timestamp                 ");
        sql.append("   from T_BANK_AGENCY_DETAIL t              ");
        sql.append("   left join t_contract_master k on t.entry_code = k.policy_code ");
        sql.append("  where t.text_id = '" + textId + "'            ");
    }
 
    /**
     * 查總筆數
    */
    private int queryForLinkDetail_count(String textId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(*) as CNT from ( ");
        queryForLinkDetail_whereConditionAppender(sql, textId);
        sql.append(" ) ");
        Session session = com.ebao.foundation.module.db.hibernate.HibernateSession3
                        .currentSession();
        SQLQuery q = session.createSQLQuery(sql.toString());
        q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = q.list();
        BigDecimal cnt = (BigDecimal) list.get(0).get("CNT");
        return cnt.intValue();
    }
 
    private static abstract class PageQueryHandler<T> {
        private static int RECORD_MAX = 10; // 顯示紀錄
 
        abstract Integer getTotalCount();
 
        abstract List<T> doQuery(Integer startItemNo, Integer endItemNo);
 
        Integer starItemNO;
 
        Integer endItemNO;
 
        public List<T> process(HttpServletRequest request) {
            Integer total = getTotalCount();
            request.setAttribute("total", "" + total);
            // 取得頁數
            Integer pageNo = CastUtils
                            .toInteger(request.getParameter("pageNo"));
            if (pageNo == null) {
                pageNo = 0;
            } else {
                // pageNo -= 1;
            }
            request.setAttribute("pageNo", "" + (pageNo + 1));
            if (RECORD_MAX * (pageNo + 1) > total) {
                starItemNO = RECORD_MAX * pageNo + 1;
                endItemNO = RECORD_MAX * pageNo + (total % RECORD_MAX);
            } else {
                starItemNO = RECORD_MAX * pageNo + 1;
                endItemNO = RECORD_MAX * (pageNo + 1);
            }
            return this.doQuery(starItemNO, endItemNO);
        }
    }
}