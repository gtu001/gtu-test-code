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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.swing.JCheckBox;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;

import gtu.collection.MapUtil;
import gtu.db.JdbcDBUtil;
import gtu.file.FileUtil;
import gtu.javafx.traynotification.NotificationType;
import gtu.javafx.traynotification.TrayNotificationHelper;
import gtu.javafx.traynotification.animations.AnimationType;
import gtu.poi.hssf.ExcelUtil_Xls97;
import gtu.poi.hssf.ExcelWriter;
import gtu.poi.hssf.ExcelWriter.CellStyleHandler;
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
    String fileMiddleName;
    Transformer finalDo;
    boolean isShowStopMessage;

    List<Integer> pkIndexLst = new ArrayList<Integer>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    NewNameHandler mNewNameHandler;

    Object syncObject = new Object();
    JCheckBox recordWatcherToggleAutoChk;
    Map<String, String> columnsAndChinese;

    HSSFWorkbook wb;

    public FastDBQueryUI_RecordWatcher(Triple<List<String>, List<Class<?>>, List<Object[]>> orignQueryResult, String sql, Object[] params, int maxRowsLimit, Callable<Connection> fetchConnCallable,
            long skipTime, String fileMiddleName, SysTrayUtil sysTray, Transformer finalDo, Map<String, String> columnsAndChinese, JCheckBox recordWatcherToggleAutoChk) {
        this.orignQueryResult = orignQueryResult;
        this.sql = sql;
        this.params = params;
        this.maxRowsLimit = maxRowsLimit;
        this.fetchConnCallable = fetchConnCallable;
        this.skipTime = skipTime;
        this.sysTray = sysTray;
        this.fileMiddleName = fileMiddleName;
        this.finalDo = finalDo;
        this.recordWatcherToggleAutoChk = recordWatcherToggleAutoChk;
        this.columnsAndChinese = columnsAndChinese;
    }

    private void finalDoSomething(String message, Throwable ex) {
        if (finalDo != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("msg", message);
            map.put("ex", ex);
            finalDo.transform(map);
        }
    }

    private long compareOldAndNew(List<Object[]> oldLst, List<Object[]> newLst, List<String> titleLst, long startTime, long queryEndTime) {
        Map<String, Object[]> oArry = new LinkedHashMap<String, Object[]>();
        Map<String, Object[]> nArry = new LinkedHashMap<String, Object[]>();

        if (pkIndexLst.isEmpty()) {
            finalDoSomething("請先設定欄位比對PK!", null);
            return -1;
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
            if (mNewNameHandler == null) {
                mNewNameHandler = new NewNameHandler(fileMiddleName, queryEndTime);
            }
            File xlsFile = this.writeExcel(titleLst, delArry, newArry, updArry);
            showModificationMessage(xlsFile);
        }

        long duringTime = System.currentTimeMillis() - startTime;
        return duringTime;
    }

    private void showModificationMessage(final File xlsFile) {
        try {
            TrayNotificationHelper.newInstance()//
                    .title("FastDBQueryUI - 資料發生異動")//
                    .message(xlsFile.getName())//
                    .notificationType(NotificationType.INFORMATION)//
                    .rectangleFill(TrayNotificationHelper.RandomColorFill.getInstance().get())//
                    .animationType(AnimationType.FADE)//
                    .onPanelClickCallback(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                Desktop.getDesktop().open(xlsFile);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }).show(1500);
        } catch (Throwable ex) {
            System.err.println(ex.getMessage());
            try {
                sysTray.displayMessage("FastDBQueryUI - 資料發生異動", xlsFile.getName(), TrayIcon.MessageType.INFO);
                System.out.println("FastDBQueryUI - 資料發生異動 : " + xlsFile.getName());
            } catch (Throwable ex2) {
            }
        }
    }

    private File writeExcel(List<String> titleLst, List<Object[]> delArry, List<Object[]> newArry, List<Pair<Object[], Object[]>> updArry) {
        File xlsFile = new File(FileUtil.DESKTOP_DIR, mNewNameHandler.getName(false));

        HSSFSheet sn = null;
        HSSFSheet sd = null;
        HSSFSheet su = null;

        boolean isExist = false;

        // if (xlsFile.exists()) {
        if (wb != null) {
            isExist = true;
            // wb = ExcelUtil_Xls97.getInstance().readExcel(xlsFile);
            sn = wb.getSheet("new");
            sd = wb.getSheet("del");
            su = wb.getSheet("update");
        } else {
            wb = new HSSFWorkbook();
            sn = wb.createSheet("new");
            sd = wb.createSheet("del");
            su = wb.createSheet("update");
        }

        CellStyleHandler pkCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.LAVENDER());
        CellStyleHandler nonPkCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.AQUA());

        CellStyleHandler changeCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.YELLOW());

        CellStyleHandler splitCs = ExcelWriter.CellStyleHandler.newInstance(wb.createCellStyle())//
                .setForegroundColor(new HSSFColor.BLUE_GREY());

        Object[] titleLst2 = titleLst.toArray();
        if (!isExist) {
            addRow(0, sn, titleLst2);
            addRow(0, sd, titleLst2);
            addRow(0, su, titleLst2);
            applyStyleByIndex(0, sn, pkIndexLst, pkCs, nonPkCs);
            applyStyleByIndex(0, sd, pkIndexLst, pkCs, nonPkCs);
            applyStyleByIndex(0, su, pkIndexLst, pkCs, nonPkCs);

            if (isColumnsAndChineseReady()) {
                addRow_NChinese(1, sn, titleLst2, columnsAndChinese);
                addRow_NChinese(1, sd, titleLst2, columnsAndChinese);
                addRow_NChinese(1, su, titleLst2, columnsAndChinese);
                applyStyleByIndex(1, sn, pkIndexLst, pkCs, nonPkCs);
                applyStyleByIndex(1, sd, pkIndexLst, pkCs, nonPkCs);
                applyStyleByIndex(1, su, pkIndexLst, pkCs, nonPkCs);
            }
        } else {
            addRowSplit(sn.getLastRowNum() + 1, sn, titleLst2, splitCs);
            addRowSplit(sd.getLastRowNum() + 1, sd, titleLst2, splitCs);
            addRowSplit(su.getLastRowNum() + 1, su, titleLst2, splitCs);
        }

        for (Object[] d : delArry) {
            addRow(sd.getLastRowNum() + 1, sd, d);
        }

        for (Object[] d : newArry) {
            addRow(sn.getLastRowNum() + 1, sn, d);
        }

        int uIdx = su.getLastRowNum() + 1;
        for (Pair<Object[], Object[]> d : updArry) {
            addRowDiff(uIdx, su, d.getLeft(), d.getRight(), changeCs);
            uIdx += 2;
        }

        ExcelUtil_Xls97.getInstance().autoCellSize(sn);
        ExcelUtil_Xls97.getInstance().autoCellSize(sd);
        ExcelUtil_Xls97.getInstance().autoCellSize(su);

        try {
            ExcelUtil_Xls97.getInstance().writeExcel(xlsFile, wb);
        } catch (Exception ex) {
            xlsFile = new File(FileUtil.DESKTOP_DIR, mNewNameHandler.getName(true));
            ExcelUtil_Xls97.getInstance().writeExcel(xlsFile, wb);
        }
        return xlsFile;
    }

    private void applyStyleByIndex(int rowIdx, HSSFSheet sn, List<Integer> indexLst, CellStyleHandler pkCs, CellStyleHandler nonPkCs) {
        pkCs.setSheet(sn);
        nonPkCs.setSheet(sn);
        Row row = sn.getRow(rowIdx);
        int max = row.getLastCellNum();
        if (max >= 255) {
            max = 255;
        }
        for (int ii = 0; ii <= max; ii++) {
            if (indexLst.contains(ii)) {
                pkCs.applyStyle(rowIdx, ii);
            } else {
                nonPkCs.applyStyle(rowIdx, ii);
            }
        }
    }

    private void addRow(int rowIdx, HSSFSheet sn, Object[] data) {
        HSSFRow r1 = sn.createRow(rowIdx);
        for (int ii = 0; ii < data.length; ii++) {
            String d = String.valueOf(data[ii]);
            r1.createCell(ii).setCellValue(d);
        }
    }

    private void addRow_NChinese(int rowIdx, HSSFSheet sn, Object[] data, Map<String, String> columnsAndChinese) {
        HSSFRow r1 = sn.createRow(rowIdx);
        for (int ii = 0; ii < data.length; ii++) {
            String d = String.valueOf(data[ii]);
            String chinese = MapUtil.getIgnorecase(d, columnsAndChinese);
            r1.createCell(ii).setCellValue(chinese);
        }
    }

    private void addRowSplit(int rowIdx, HSSFSheet sn, Object[] data, CellStyleHandler split) {
        String[] arry = new String[data.length];
        Arrays.fill(arry, "");
        addRow(rowIdx, sn, arry);
        split.setSheet(sn);
        split.applyStyle(rowIdx, rowIdx, 0, data.length - 1);
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
            throw new RuntimeException("取得連線錯誤!", e);
        }
    }

    public void run() {
        synchronized (syncObject) {
            try {
                System.out.println("--------RecordWatcher start");
                while (!doStop) {

                    while (!this.recordWatcherToggleAutoChk.isSelected()) {
                        syncObject.wait();
                    }

                    if (this.orignQueryResult == null) {
                        System.out.println("--------RecordWatcher start -- [init query]");
                        try {
                            this.orignQueryResult = JdbcDBUtil.queryForList_customColumns(sql, params, getConnection(), true, maxRowsLimit);
                        } catch (Exception e) {
                            // throw new RuntimeException("查詢錯誤!", e);
                            Thread.sleep(100);
                            continue;
                        }
                    } else {
                        System.out.println("--------RecordWatcher start -- [requery]");
                        long startTime = System.currentTimeMillis();
                        Triple<List<String>, List<Class<?>>, List<Object[]>> compareResult = null;
                        try {
                            compareResult = JdbcDBUtil.queryForList_customColumns(sql, params, getConnection(), true, maxRowsLimit);
                        } catch (Exception e) {
                            // throw new RuntimeException("查詢錯誤!", e);
                            Thread.sleep(100);
                            continue;
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
                System.out.println("--------RecordWatcher start -- [end]");
                String stopMessage = "監聽結束!!";
                if (!isShowStopMessage) {
                    stopMessage = null;
                }
                this.finalDoSomething(stopMessage, null);
            } catch (Throwable ex) {
                this.finalDoSomething(ex.getMessage(), ex);
            }
        }
    }

    private boolean isColumnsAndChineseReady() {
        if (columnsAndChinese == null || columnsAndChinese.isEmpty()) {
            return false;
        }
        for (String key : columnsAndChinese.keySet()) {
            if (StringUtils.isNotBlank(columnsAndChinese.get(key))) {
                return true;
            }
        }
        return false;
    }

    private class NewNameHandler {
        String middleName;
        long queryEndTime;

        private NewNameHandler(String fileMiddleName, long queryEndTime) {
            middleName = StringUtils.trimToEmpty(fileMiddleName);
            this.queryEndTime = queryEndTime;
        }

        private String getName(boolean addOne) {
            if (addOne) {
                this.queryEndTime++;
            }
            String xlsNameTemp = "FastDBQueryUI_DiffMark_" + middleName + "_" + sdf.format(queryEndTime) + ".xls";
            return xlsNameTemp;
        }
    }

    public void doStop(boolean isShowStopMessage) {
        this.isShowStopMessage = isShowStopMessage;
        this.doStop = true;
    }

    public void setPkIndexLst(List<Integer> pkIndexLst) {
        this.pkIndexLst = pkIndexLst;
    }

    public void doNotify() {
        synchronized (syncObject) {
            syncObject.notify();
        }
    }

    public void setColumnsAndChinese(Map<String, String> columnsAndChinese) {
        this.columnsAndChinese = columnsAndChinese;
    }
}
