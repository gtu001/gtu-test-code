package gtu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class JdbcQueryTemplateTest {

    private void query1(Connection conn) {
        PreparedStatement aStmt = null;
        boolean aConnError = false;
        Date aStartTime = new Date();
        do {
            try {
                String sql = null;
                aStmt = conn.prepareStatement(sql);
                ResultSet rs = aStmt.executeQuery();
                // TODO
                rs.close();
                aConnError = false;
            } catch (Throwable e) {
                e.printStackTrace();
                aConnError = true;
                try {
                    Thread.sleep(1000);
                } catch (Throwable ex) {
                }
            } finally {
                try {
                    aStmt.close();
                } catch (Throwable e) {
                }
            }
        } while (aConnError && new Date().getTime() - aStartTime.getTime() < 60000);
    }

    private void query2(Connection conn) {
        PreparedStatement stmt = null;
        boolean executeDone = false;
        int time = 0;
        do {
            try {
                String sql = null;
                stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                executeDone = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    this.wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time++;
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } while (executeDone || time == 20);
    }

    private Object update(Statement stmt, Connection conn) {
        Object rtn = null;
        try {
            conn.setAutoCommit(false);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rtn;
    }
}