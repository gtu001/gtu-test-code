package gtu.db.tradevan;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

public class DBExecuteCall_tradevan {

    private static final DBExecuteCall_tradevan _instance = new DBExecuteCall_tradevan();

    public static DBExecuteCall_tradevan getInstance() {
        return _instance;
    }

    public void call(String callsql, List<CallParameter> inList, List<CallParameter> outList, Connection conn) {
        CallableStatement stmt = null;
        try {
            stmt = conn.prepareCall(callsql);
            System.out.println("call sql = " + callsql);

            for (CallParameter inp : inList) {
                System.out.println("set in = " + inp);
                if (inp.value == null) {
                    stmt.setNull(inp.index, inp.type);
                } else {
                    stmt.setObject(inp.index, inp.value, inp.type);
                }
            }

            for (CallParameter outp : outList) {
                System.out.println("set out = " + outp);
                stmt.registerOutParameter(outp.index, outp.type);
            }

            boolean result = stmt.execute();
            System.out.println("execute reulst = " + result);

            for (CallParameter outp : outList) {
                Object val = stmt.getObject(outp.index);
                outp.value = val;
                System.out.println("set out register = " + outp);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            DBCommon_tradevan.closeConnection(null, stmt, conn);
        }
    }

    public static class CallParameter {
        int index;

        int type;

        Object value;

        public CallParameter(int index, Object value, int type) {
            this.index = index;
            this.value = value;
            this.type = type;
        }

        @Override
        public String toString() {
            return "CallParameter [index=" + index + ", value=" + value + ", type=" + type + "]";
        }
    }
}
