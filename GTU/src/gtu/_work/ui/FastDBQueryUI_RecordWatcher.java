package gtu._work.ui;

import java.awt.Desktop;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import gtu.db.JdbcDBUtil;
import gtu.file.FileUtil;
import gtu.javafx.traynotification.NotificationType;
import gtu.javafx.traynotification.TrayNotificationHelper;
import gtu.javafx.traynotification.animations.AnimationType;
import gtu.poi.hssf.ExcelUtil_Xls97;
import gtu.poi.hssf.ExcelWriter;
import gtu.poi.hssf.ExcelWriter.CellStyleHandler;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.SysTrayUtil;

public class FastDBQueryUI_RecordWatcher extends Thread {
    Triple<List<String>, List<Class<?>>, List<Object[]>> orignQueryResult;
    String sql;
    Object[] params;
    int maxRowsLimit;
    boolean doStop = false;
    Callable<Connection> fetchConnCallable;
    long skipTime;
    SysTrayUtil sysTray;

    List<Integer> pkIndexLst = new ArrayList<Integer>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public FastDBQueryUI_RecordWatcher(Triple<List<String>, List<Class<?>>, List<Object[]>> orignQueryResult, String sql, Object[] params, int maxRowsLimit, Callable<Connection> fetchConnCallable,
            long skipTime, SysTrayUtil sysTray) {
        this.orignQueryResult = orignQueryResult;
        this.sql = sql;
        this.params = params;
        this.maxRowsLimit = maxRowsLimit;
        this.fetchConnCallable = fetchConnCallable;
        this.skipTime = skipTime;
        this.sysTray = sysTray;
    }

    private long compareOldAndNew(List<Object[]> oldLst, List<Object[]> newLst, List<String> titleLst, long startTime, long queryEndTime) {
        Map<String, Object[]> oArry = new LinkedHashMap<String, Object[]>();
        Map<String, Object[]> nArry = new LinkedHashMap<String, Object[]>();

        if (pkIndexLst.isEmpty()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("請先設定欄位比對PK!");
            throw new RuntimeException("請先設定欄位比對PK!");
        }

        this.appendAllRecords(oldLst, oArry);
        this.appendAllRecords(newLst, nArry);

        List<Object[]> delArry = new ArrayList<Object[]>();
        List<Object[]> newArry = new ArrayList<Object[]>();
        List<Pair<Object[], Object[]>> updArry = new ArrayList<Pair<Object[], Object[]>>();

        for (String key : oArry.keySet()) {
            if (!nArry.containsKey(key)) {
                delArry.add(oArry.get(key));
            } else {
                Object[] oldx = oArry.get(key);
                Object[] newx = nArry.get(key);
                if (!isArrayEquals(oldx, newx)) {
                    updArry.add(Pair.of(oldx, newx));
                }
            }
        }

        for (String key : nArry.keySet()) {
            if (!oArry.containsKey(key)) {
                newArry.add(nArry.get(key));
            }
        }

        if (!delArry.isEmpty() || !newArry.isEmpty() || !updArry.isEmpty()) {
            String xlsName = "FastDBQueryUI_DiffMark_" + sdf.format(queryEndTime) + ".xls";
            this.writeExcel(titleLst, delArry, newArry, updArry, xlsName);
            showModificationMessage(xlsName);
        }

        long duringTime = System.currentTimeMillis() - startTime;
        return duringTime;
    }

    private void showModificationMessage(String xlsName) {
        try {
            TrayNotificationHelper.newInstance()//
                    .title("FastDBQueryUI - 資料發生異動")//
                    .message(xlsName)//
                    .notificationType(NotificationType.INFORMATION)//
                    .rectangleFill(TrayNotificationHelper.RandomColorFill.getInstance().get())//
                    .animationType(AnimationType.FADE)//
                    .onPanelClickCallback(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                Desktop.getDesktop().open(new File(FileUtil.DESKTOP_PATH + xlsName));
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }).show(1500);
        } catch (Throwable ex) {
            System.err.println(ex.getMessage());
            try {
                sysTray.displayMessage("FastDBQueryUI - 資料發生異動", xlsName, TrayIcon.MessageType.INFO);
                System.out.println("FastDBQueryUI - 資料發生異動 : " + xlsName);
            } catch (Throwable ex2) {
            }
        }
    }

    private void writeExcel(List<String> titleLst, List<Object[]> delArry, List<Object[]> newArry, List<Pair<Object[], Object[]>> updArry, String xlsName) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sn = wb.createSheet("new");
        HSSFSheet sd = wb.createSheet("del");
        HSSFSheet su = wb.createSheet("update");

        CellStyleHandler cs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.YELLOW());

        Object[] titleLst2 = titleLst.toArray();
        addRow(0, sn, titleLst2);
        addRow(0, sd, titleLst2);
        addRow(0, su, titleLst2);

        int dIdx = 1;
        for (Object[] d : delArry) {
            addRow(dIdx++, sd, d);
        }

        int nIdx = 1;
        for (Object[] d : newArry) {
            addRow(nIdx++, sn, d);
        }

        int uIdx = 1;
        for (Pair<Object[], Object[]> d : updArry) {
            addRowDiff(uIdx + 1, su, d.getLeft(), d.getRight(), cs);
            uIdx += 2;
        }
        ExcelUtil_Xls97.getInstance().writeExcel(FileUtil.DESKTOP_PATH + xlsName, wb);
    }

    private void addRow(int rowIdx, HSSFSheet sn, Object[] data) {
        HSSFRow r1 = sn.createRow(rowIdx);
        for (int ii = 0; ii < data.length; ii++) {
            String d = String.valueOf(data[ii]);
            r1.createCell(ii).setCellValue(d);
        }
    }

    private void addRowDiff(int startRowIdx, HSSFSheet sn, Object[] oldx, Object[] newx, CellStyleHandler cs) {
        List<Integer> diffIndexLst = new ArrayList<Integer>();
        for (int ii = 0; ii < oldx.length; ii++) {
            String ox = String.valueOf(oldx[ii]);
            String nx = String.valueOf(newx[ii]);
            if (!StringUtils.equals(ox, nx)) {
                diffIndexLst.add(ii);
            }
        }

        addRow(startRowIdx, sn, oldx);
        addRow(startRowIdx + 1, sn, newx);

        for (int cellIdx : diffIndexLst) {
            cs.setSheet(sn);
            cs.applyStyle(startRowIdx, cellIdx);
            cs.applyStyle(startRowIdx + 1, cellIdx);
        }
    }

    private boolean isArrayEquals(Object[] oldx, Object[] newx) {
        String o1 = StringUtils.join(oldx);
        String n1 = StringUtils.join(newx);
        return StringUtils.equals(o1, n1);
    }

    private void appendAllRecords(List<Object[]> oldLst, Map<String, Object[]> oArry) {
        for (int ii = 0; ii < oldLst.size(); ii++) {
            Object[] arry = oldLst.get(ii);
            List<String> pkLst = new ArrayList<String>();
            for (int idx : pkIndexLst) {
                pkLst.add(String.valueOf(arry[idx]));
            }
            String key = StringUtils.join(pkLst, "^");
            oArry.put(key, arry);
        }
    }

    private Connection getConnection() {
        try {
            return (Connection) (this.fetchConnCallable.call());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (!doStop) {
            if (this.orignQueryResult == null) {
                try {
                    this.orignQueryResult = JdbcDBUtil.queryForList_customColumns(sql, params, getConnection(), true, maxRowsLimit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                long startTime = System.currentTimeMillis();
                Triple<List<String>, List<Class<?>>, List<Object[]>> compareResult = null;
                try {
                    compareResult = JdbcDBUtil.queryForList_customColumns(sql, params, getConnection(), true, maxRowsLimit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                long endTime = System.currentTimeMillis();

                if (orignQueryResult != null && compareResult != null) {
                    List<String> titleLst = orignQueryResult.getLeft();
                    if (titleLst.size() != compareResult.getLeft().size()) {
                        throw new RuntimeException("欄位數不同(old/new) : " + titleLst.size() + "/" + compareResult.getLeft().size());
                    } else {
                        // 真實比對 XXX
                        // ==============================================================
                        long sleepTime = compareOldAndNew(orignQueryResult.getRight(), compareResult.getRight(), titleLst, startTime, endTime);
                        // 真實比對 XXX
                        // ==============================================================
                        orignQueryResult = compareResult;
                        if (sleepTime > 0) {
                            try {
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
            }
        }
    }

    public void doStop() {
        this.doStop = true;
    }

    public void setPkIndexLst(List<Integer> pkIndexLst) {
        this.pkIndexLst = pkIndexLst;
    }
}
