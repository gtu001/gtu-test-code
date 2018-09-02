package gtu.db.simple_dao_gen;

import org.apache.commons.lang3.reflect.MethodUtils;

public class DBTypeTransferUtil {
    
    public Object getTimestampFromOracle(Object val){
        if(val !=null && val.getClass().getName().equals("oracle.sql.TIMESTAMP") ) {
            try {
                return (java.sql.Timestamp)MethodUtils.invokeMethod(val, "timestampValue");
            } catch (Exception e) {
                throw new RuntimeException("getTimestampFromOracle err : " + e.getMessage(), e);
            }
        }
        return val;
    }
}
