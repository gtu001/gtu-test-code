package gtu.json;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.JSONException;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

public class JSONNullHandler {

    private static final Logger logger = Logger.getLogger(JSONNullHandler.class);
    
    public static void main(String[] args) throws JSONException {
        JSONObject jsonObj = JSONObject.fromObject(
                "{\"transactionID\":\"003005\",\"isRequest\":\"false\",\"isResponse\":\"true\",\"system\":{\"bocBankTxnRequestDate\":\"2018-03-21\",\"bocBankTxnResponseTime\":\"16:31:25\",\"txnProvinceCode\":null,\"transactionNumber\":\"00042017110918500700000003\",\"localBankTxnResponseDate\":\"2018-03-21\",\"txnTerminalCode\":\"6\",\"txnBankCode\":\"6471\",\"transactionSessionId\":\"fd227d5fc832409487e3b056d2e29a8f\",\"localBankTxnRequestTime\":\"163132\",\"txnUserCode\":\"990501\",\"txnTraceNumber\":\"0010905769\",\"bocBankTxnRequestTime\":\"16:31:25\",\"bocBankTxnResponseDate\":\"2018-03-21\",\"orgTxnTraceNumber\":null,\"transactionCode\":\"003005\",\"requestChannelId\":\"0004\",\"localBankTxnResponseTime\":\"16:31:25\",\"extendField1\":null,\"extendField2\":null,\"extendField3\":null,\"extendField4\":null,\"returnMessage\":\"上送密码为空\",\"txnBranchCode\":\"4\",\"localBankTxnRequestDate\":\"20180321\",\"returnCode\":\"000099\",\"txnCounterCode\":\"0501\"},\"page\":{},\"message\":\"執行成功!\",\"result\":\"000000\"}");

        System.out.println("---------------------");
        filterNullReference(jsonObj);
        System.out.println("---------------------");
        filterNullReference(jsonObj);
        System.out.println("---------------------");
        System.out.println("done...");
    }

    public static void filterNullReference(JSONObject jsonObj) {
        for (Iterator<?> it = jsonObj.keys(); it.hasNext();) {
            String key = (String) it.next();
            Object val = jsonObj.get(key);
            if (val instanceof JSONNull) {
                jsonObj.put(key, "");
                logger.info("reset null key : " + key);
            } else if (val instanceof JSONObject) {
                filterNullReference((JSONObject) val);
            } else if (val instanceof JSONArray) {
                JSONArray arry = (JSONArray) val;
                for (int ii = 0; ii < arry.size(); ii++) {
                    Object v = arry.get(ii);
                    if (v instanceof JSONNull) {
                        arry.set(ii, "");
                        logger.info("reset null key : " + key + " index : " + ii);
                    } else if (v instanceof JSONObject) {
                        filterNullReference((JSONObject) v);
                    }
                }
            }
        }
    }
}