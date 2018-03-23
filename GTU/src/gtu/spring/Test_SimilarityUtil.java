package gtu.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import gtu.swing.util.JCommonUtil;

public class Test_SimilarityUtil {

    public static void main(String[] args) {
        String[] arry = JCommonUtil._jOptionPane_showInputDialog("欄位英文,欄位中文").split(",");
        String orignRank1 = arry[0];// 欄位英文
        String orignRank2 = arry[1];// 欄位中文

        Map<String, String> map = new HashMap<String, String>();
        
        map.put("PersonInfo_FLAG", "记录标志");
        map.put("BANK", "银行编号");
        map.put("CURR", "交易币种");
        map.put("CLOSE_BAL", "本期应还款");
        map.put("STMT_BAL", "账单金额");
        map.put("STMT_SIGN", "账单金额符号");
        map.put("MIN_DUE", "最低应还款");
        map.put("DUE_DATE", "还款截止日期");
        map.put("NAME", "账户姓名");
        map.put("ADDRESS1", "账单地址栏1");
        map.put("ADDRESS2", "账单地址栏2");
        map.put("ADDRESS3", "账单地址栏3");
        map.put("ADDRESS4", "账单地址栏4");
        map.put("ADDRESS5", "账单地址栏5");
        map.put("POST_CODE", "账单地址邮政编码");
        map.put("CARD_NBR", "卡号");
        map.put("CLOSE_DATE", "结账日期");
        map.put("OPEN_BAL", "上期账单金额");
        map.put("OPBAL_SIGN", "上期账单金额符号");
        map.put("PAYMENT", "上期还款额");
        map.put("INT", "循环利息");
        map.put("ADJUST_AMT", "本期调整金额");
        map.put("ADJST_SIGN", "本期调整金额符号");
        map.put("ADD_AMT", "本期新增账款");
        map.put("STMT_CODE", "账单代码信息");
        map.put("REPRINT_FLAG", "补寄标志");
        map.put("ADDAMT_SIGN", "本期新增账款的金额符号");
        map.put("PersonInfo_REVS1", "保留字段");
        map.put("ADDRESS21", "第二账单地址1");
        map.put("ADDRESS22", "第二账单地址2");
        map.put("ADDRESS23", "第二账单地址3");
        map.put("ADDRESS24", "第二账单地址4");
        map.put("ADDRESS25", "第二账单地址5");
        map.put("POST_CODE2", "第二账单地址邮政编码");
        map.put("CLOSE_BALX", "外币本期应还款的人民币金额");
        map.put("PersonInfo_EXCH_RATE", "账单日汇率");
        map.put("FIRSTMT_FLAG", "首张账单标志");
        map.put("BIRTHMTH_YN", "账户拥有者生日标志");
        map.put("BIRTH_YYMM", "客户生日年月");
        map.put("GENDER", "客户性别");
        map.put("CARD_PLANS", "账户所拥有卡片品牌");
        map.put("CARD_LEVELS", "账户所拥有卡片的卡片级别");
        map.put("CARD_PRODS", "账户所拥有卡片产品");
        map.put("CUSTR_ID", "证件号码");
        map.put("ACCOUNT", "账号");
        map.put("ODUE_MTHS", "逾期期数");
        map.put("PersonInfo_REVS2", "保留域");
        map.put("MICF", "条形码栏位");
        map.put("APP_SOURCE", "发卡面专案代码");
        map.put("CLOSE_STS", "账户状态");
        map.put("ODUE_FLAG", "账户逾期");
        map.put("PROD_MAXLEVEL", "最高卡种级别");
        map.put("CATEGORY", "账户类别");
        map.put("DEPT", "部门");
        map.put("FTRAMT", "下期出单金额");
        map.put("BRANCH", "分行机构");
        map.put("CRED_LMT2", "综合授信额度");
        map.put("MAILTYPE", "账单快递方式");
        map.put("COMP_NAME", "单位名称");
        map.put("PersonInfo_REVS3", "保留字段1");
        map.put("PersonInfo_REVS4", "保留字段2");

        map.put("DetailsInfo_FLAG", "记录标志");// DetailsInfo
        map.put("SEQ_NO", "明细记录的序号");// DetailsInfo
        map.put("TRAN_NO", "交易流水号");// DetailsInfo
        map.put("CARD_END", "卡号后4位");// DetailsInfo
        map.put("PUR_DATE", "交易日期");// DetailsInfo
        map.put("TRAN_DESC1", "交易描述1");// DetailsInfo
        map.put("TRAN_DESC2", "交易描述2");// DetailsInfo
        map.put("BILL_AMT", "交易金额");// DetailsInfo
        map.put("BAMT_SIGN", "交易金额符号");// DetailsInfo
        map.put("VAL_DATE", "交易记账日期");// DetailsInfo
        map.put("MCC_CODE", "商户类别代码");// DetailsInfo
        map.put("DetailsInfo_REVS1", "保留字段1");// DetailsInfo
        map.put("ICADN", "芯片/磁条交易标志");// DetailsInfo
        map.put("INP_TIME", "交易具体时间");// DetailsInfo
        map.put("HOLDER", "主附卡标识");// DetailsInfo
        map.put("DetailsInfo_EXCH_RATE", "购汇汇率");// DetailsInfo
        map.put("DetailsInfo_REVS5", "保留字段5");// DetailsInfo
        map.put("DetailsInfo_REVS6", "保留字段6");// DetailsInfo

        map.put("PointInfo_FLAG", "记录标志"); // PointInfo
        map.put("MP_CRED", "分期付款信用额度"); // PointInfo
        map.put("PointInfo_REVS1", "保留字段"); // PointInfo
        map.put("INT_RATE", "日利率"); // PointInfo
        map.put("CRED_LIMIT", "信用额度"); // PointInfo
        map.put("PointInfo_REVS2", "保留字段"); // PointInfo
        map.put("CASH_CRED", "预借现金最高额度"); // PointInfo
        map.put("CASE_AVAIL", "预借现金可用额度"); // PointInfo
        map.put("POINT_CUR", "累积总积分（本期账单积分余额）"); // PointInfo
        map.put("PTCU_SIGN", "累计总积分符号"); // PointInfo
        map.put("POINT_ADD", "本月新增积分"); // PointInfo
        map.put("PTEA_SIGN", "本月新增积分符号"); // PointInfo
        map.put("POINT_CLR", "本月已兑换积分"); // PointInfo
        map.put("POINT_ADJ", "本月调整积分"); // PointInfo
        map.put("PTADJ_SIGN", "本月调整积分符号"); // PointInfo
        map.put("POINT_STMT", "上期账单积分"); // PointInfo
        map.put("PTST_SIGN", "上期账单积分符号"); // PointInfo
        map.put("POINT_IMP", "合作单位转入积分"); // PointInfo
        map.put("POINT_EXP", "合作单位转出积分"); // PointInfo
        map.put("POINT_ENC", "奖励积分"); // PointInfo
        map.put("PTENC_SIGN", "奖励积分符号"); // PointInfo
        map.put("BANK_ACCT", "自动扣款账号"); // PointInfo
        map.put("AUTOREPAY_AMT", "自动扣款金额"); // PointInfo
        map.put("BUSI_PHONE", "单位电话"); // PointInfo
        map.put("HOME_PHONE", "家庭电话"); // PointInfo
        map.put("MOBILE_PHONE", "手机号码"); // PointInfo
        map.put("EMAIL", "EMAIL地址"); // PointInfo
        map.put("MESSAGE1", "账单消息1"); // PointInfo
        map.put("MESSAGE2", "账单消息2"); // PointInfo
        map.put("MESSAGE3", "账单消息3"); // PointInfo
        map.put("MP_TOTAL", "分期付款交易的交易总金额"); // PointInfo
        map.put("MP_BAL", "分期付款交易的未还总金额"); // PointInfo
        map.put("CASH_LIMIT", "现金贷款额度"); // PointInfo
        map.put("CASH_AVAIL", "现金贷款可用额度"); // PointInfo
        map.put("PointInfo_REVS3", "保留字段"); // PointInfo
        map.put("CP_NO", "预借积分计划"); // PointInfo
        map.put("CP_TOTAL", "预借积分总积分"); // PointInfo
        map.put("CP_CUM", "预借未还积分"); // PointInfo
        map.put("CP_DAY", "预借积分时限"); // PointInfo
        map.put("CP_BEGDAY", "预借积分计划开始日期"); // PointInfo
        map.put("CP_ENDDAY", "预借积分计划结束日期"); // PointInfo
        map.put("LMP_LMT", "大额分期付款额度"); // PointInfo
        map.put("LCLOSE_BAL", "大额分期付款交易未还总金额"); // PointInfo
        map.put("TEMP_LIMIT", "临时信用额度"); // PointInfo
        map.put("TLMT_BEG", "临时额度生效日期"); // PointInfo
        map.put("TLMT_END", "临时额度失效日期"); // PointInfo
        map.put("CLIMIT_AVAIL", "可用贷款额度"); // PointInfo
        map.put("PointInfo_REVS4", "保留字段1"); // PointInfo
        map.put("PointInfo_REVS5", "保留字段2"); // PointInfo

        Map<Double, List<String>> rank1 = new TreeMap<Double, List<String>>();
        Map<Double, List<String>> rank2 = new TreeMap<Double, List<String>>();

        for (String key : map.keySet()) {
            String val = map.get(key);
            putByList(rank1, SimilarityUtil.sim(orignRank1.toLowerCase(), key.toLowerCase()), key);
            putByList(rank2, SimilarityUtil.sim(orignRank2, val), val);
        }

        List<Double> rank1Lst = new ArrayList<Double>(rank1.keySet());
        List<Double> rank2Lst = new ArrayList<Double>(rank2.keySet());

        Collections.reverse(rank1Lst);
        Collections.reverse(rank2Lst);

        int showMaxSize = 10;

        int rank1Lst_Len = rank1Lst.size() > showMaxSize ? showMaxSize : rank1Lst.size();
        int rank2Lst_Len = rank2Lst.size() > showMaxSize ? showMaxSize : rank2Lst.size();

        System.out.println("輸入<<" + Arrays.toString(arry));

        System.out.println("------------------------------------------------------------");
        for (int ii = 0; ii < rank1Lst_Len; ii++) {
            System.out.println(rank1Lst.get(ii));
            List<String> lst2 = rank1.get(rank1Lst.get(ii));
            Collections.sort(lst2);
            for (int jj = 0; jj < lst2.size(); jj++) {
                System.out.println("\t" + lst2.get(jj) + "\t" + map.get(lst2.get(jj)));
            }
        }
        System.out.println("------------------------------------------------------------");
        for (int ii = 0; ii < rank2Lst_Len; ii++) {
            System.out.println(rank2Lst.get(ii) + "\t" + rank2.get(rank2Lst.get(ii)));
        }
    }

    private static void putByList(Map<Double, List<String>> map, Double key, String value) {
        List<String> arry = new ArrayList<String>();
        if (map.containsKey(key)) {
            arry = map.get(key);
        }
        map.put(key, arry);
        arry.add(value);
    }
}
