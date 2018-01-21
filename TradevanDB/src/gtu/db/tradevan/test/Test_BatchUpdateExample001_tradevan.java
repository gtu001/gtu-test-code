package gtu.db.tradevan.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gtu.db.tradevan.DBBatchUpdatePreapre_tradevan.BatchUpdatePrepareAction;
import gtu.db.tradevan.DBBatchUpdatePreapre_tradevan.BatchUpdatePrepareActionObject;
import gtu.db.tradevan.DBCommon_tradevan;
import gtu.db.tradevan.DBMain;

public class Test_BatchUpdateExample001_tradevan {

    public static void main(String[] args) throws SQLException {
        Test_BatchUpdateExample001_tradevan t1 = new Test_BatchUpdateExample001_tradevan();
        // t1.testCaseNormalInsert();
        t1.testCaseBatchInsert();
    }

    private static final int MAX_SIZE = 200000;

    /*
     * done... 37412
     */
    private void testCaseNormalInsert() {
        DBMain dbMain = new DBMain();
//        dbMain.setDataSource(DbConstant.getTestDataSource());

        dbMain.beginTransaction();

        long startTime = System.currentTimeMillis();

        for (int ii = 0; ii < MAX_SIZE; ii++) {
            ProductDO vo = new ProductDO();
            vo.setListId(new BigDecimal(ii));
            vo.setGroupId("group" + ii);
            vo.setName("name" + ii);
            vo.setPrice(new BigDecimal(ii * 10));
            vo.setTitle("title" + ii);
            dbMain.insert(vo);
        }

        dbMain.commit();

        long during = System.currentTimeMillis() - startTime;

        System.out.println("done... " + during);
    }

    /*
     * done... 21221 (1000筆commit一次) done... 24334 (10000筆commit一次) done...
     * 23543 (100000筆commit一次) done... 23590 (200000筆commit一次)
     */
    private void testCaseBatchInsert() throws SQLException {
        DBMain dbMain = new DBMain();
        // dbMain.setDataSource(DbConstant.getTestDataSource());

        long startTime = System.currentTimeMillis();

        Connection conn = dbMain.getDataSource().getConnection();
        PreparedStatement pstmt = conn.prepareStatement("select * from user_info where 1=1 ");

        final ResultSet rs = pstmt.executeQuery();

        dbMain.batchUpdate(1000, false, dbMain.getDataSource().getConnection(), new BatchUpdatePrepareAction() {

            int listId = 0;

            @Override
            public BatchUpdatePrepareActionObject[] apply() throws SQLException {
                List<BatchUpdatePrepareActionObject> rtnList = new ArrayList<BatchUpdatePrepareActionObject>();
                if (rs.next()) {
                    BatchUpdatePrepareActionObject bean = new BatchUpdatePrepareActionObject();
                    Map<String, Object> valMap = DBCommon_tradevan.getValueMapFrom(rs);
                    System.out.println("valMap == " + valMap);

                    ProductDO vo = new ProductDO();
                    vo.setListId(new BigDecimal(listId));
                    vo.setGroupId("group" + listId);
                    vo.setName((String) valMap.get("EMAIL"));
                    vo.setPrice(new BigDecimal(listId * 10));
                    vo.setTitle((String) valMap.get("USER_ID"));
                    bean.setBean(vo);
                    bean.setUpdateType("insert");
                    rtnList.add(bean);
                } else if (listId < MAX_SIZE) {
                    BatchUpdatePrepareActionObject bean = new BatchUpdatePrepareActionObject();
                    ProductDO vo = new ProductDO();
                    vo.setListId(new BigDecimal(listId));
                    vo.setGroupId("group" + listId);
                    vo.setName("name" + listId);
                    vo.setPrice(new BigDecimal(listId * 10));
                    vo.setTitle("title" + listId);
                    bean.setBean(vo);
                    bean.setUpdateType("insert");
                    rtnList.add(bean);
                }
                listId++;
                return rtnList.toArray(new BatchUpdatePrepareActionObject[0]);
            }
        });

        DBCommon_tradevan.closeConnection(rs, pstmt, conn);

        long during = System.currentTimeMillis() - startTime;

        System.out.println("done... " + during);
    }
}
