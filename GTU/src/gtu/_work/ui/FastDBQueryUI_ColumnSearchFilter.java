package gtu._work.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

public class FastDBQueryUI_ColumnSearchFilter {
    String delimit;
    Object[] alwaysMatchColumns;
    Triple<List<String>, List<Class<?>>, List<Object[]>> queryList = null;
    Triple<List<String>, List<Class<?>>, List<Object[]>> resultList = null;
    // -----------------以下變數為rowFilter
    Triple<List<String>, List<Class<?>>, List<Object[]>> rowFilterResult = null;
    Map<Integer, List<Integer>> changeColorRowCellIdxMap = null;

    public FastDBQueryUI_ColumnSearchFilter(Triple<List<String>, List<Class<?>>, List<Object[]>> queryList, String delimit, Object[] alwaysMatchColumns) {
        this.delimit = (delimit == null || StringUtils.isBlank(delimit)) ? "," : delimit;
        this.alwaysMatchColumns = alwaysMatchColumns;
        this.queryList = queryList;
    }

    class InnerMatch {
        Pattern ptn;

        InnerMatch(String singleText) {
            singleText = singleText.replaceAll(Pattern.quote("*"), ".*");
            ptn = Pattern.compile(singleText, Pattern.CASE_INSENSITIVE);
        }

        boolean find(String value) {
            Matcher mth = ptn.matcher(value);
            return mth.find();
        }
    }

    private Pair<String, List<Pattern>> filterPattern(String filterText) {
        Pattern ptn = Pattern.compile("\\/(.*?)\\/");
        Matcher mth = ptn.matcher(filterText);
        StringBuffer sb = new StringBuffer();
        List<Pattern> lst = new ArrayList<Pattern>();
        while (mth.find()) {
            String temp = mth.group(1);
            Pattern tmpPtn = null;
            if (StringUtils.isNotBlank(temp)) {
                try {
                    tmpPtn = Pattern.compile(temp, Pattern.CASE_INSENSITIVE);
                } catch (Exception ex) {
                }
            }
            if (tmpPtn != null) {
                lst.add(tmpPtn);
                mth.appendReplacement(sb, "");
            } else {
                mth.appendReplacement(sb, mth.group(0));
            }
        }
        mth.appendTail(sb);
        return Pair.of(sb.toString(), lst);
    }

    private void __filterText(String filterText) {
        // 解析 regex ptn
        Pair<String, List<Pattern>> afterFilterProc = filterPattern(filterText);

        String[] params = StringUtils.trimToEmpty(afterFilterProc.getLeft()).toUpperCase().split(Pattern.quote(delimit), -1);
        Map<String, Integer> addColumns = new LinkedHashMap<String, Integer>();

        for (String param : params) {
            param = StringUtils.trimToEmpty(param);
            InnerMatch m = new InnerMatch(param);

            for (int ii = 0; ii < queryList.getLeft().size(); ii++) {
                Object key = queryList.getLeft().get(ii);
                String headerColumn = String.valueOf(key);
                String headerColumnUpper = headerColumn.toUpperCase();

                boolean findOk = false;

                if (this.alwaysMatchColumns != null) {
                    for (Object v : this.alwaysMatchColumns) {
                        if (StringUtils.equals(String.valueOf(headerColumn), String.valueOf(v))) {
                            findOk = true;
                            break;
                        }
                    }
                }

                if (!findOk) {
                    if (StringUtils.isNotBlank(param) && headerColumnUpper.contains(param)) {
                        System.out.println("Match------------" + headerColumn + " --> " + param);
                        findOk = true;
                    } else if (param.contains("*")) {
                        if (m.find(headerColumnUpper)) {
                            findOk = true;
                        }
                    } else if (!afterFilterProc.getRight().isEmpty()) {
                        for (Pattern p : afterFilterProc.getRight()) {
                            if (p.matcher(headerColumn).find()) {
                                findOk = true;
                                break;
                            }
                        }
                    } else if (StringUtils.isBlank(filterText)) {
                        findOk = true;
                    }
                }

                if (findOk && !addColumns.containsKey(headerColumn)) {
                    System.out.println("Add------------" + key);
                    addColumns.put(headerColumn, ii);
                }
            }
        }

        List<String> titleLst = new ArrayList<String>();
        List<Class<?>> dataClzLst = new ArrayList<Class<?>>();
        List<Object[]> dataLst = new ArrayList<Object[]>();

        for (String column : addColumns.keySet()) {
            Integer idx = addColumns.get(column);
            titleLst.add(queryList.getLeft().get(idx));
            dataClzLst.add(queryList.getMiddle().get(idx));
        }

        for (Object[] row : queryList.getRight()) {
            List<Object> dLst = new ArrayList<Object>();
            for (Integer idx : addColumns.values()) {
                dLst.add(row[idx]);
            }
            dataLst.add(dLst.toArray());
        }

        resultList = Triple.of(titleLst, dataClzLst, dataLst);
    }

    public void filterColumnText(String filterText) {
        __filterText(filterText);
    }

    public Triple<List<String>, List<Class<?>>, List<Object[]>> getResult() {
        return resultList;
    }

    // -------------------------------------------------------------------------------
    // 以下是row filter
    // -------------------------------------------------------------------------------

    public static class FindTextHandler {
        String searchText;
        String delimit;

        FindTextHandler(String searchText, String delimit) {
            this.searchText = searchText;
            this.delimit = delimit;
        }

        boolean isAllMatch() {
            return StringUtils.isBlank(searchText);
        }

        String[] getArry() {
            String[] arry = StringUtils.trimToEmpty(searchText).toLowerCase().split(Pattern.quote(delimit), -1);
            List<String> rtnLst = new ArrayList<String>();
            for (int ii = 0; ii < arry.length; ii++) {
                if (StringUtils.isNotBlank(arry[ii])) {
                    rtnLst.add(StringUtils.trimToEmpty(arry[ii]));
                }
            }
            return rtnLst.toArray(new String[0]);
        }

        String valueToString(Object value) {
            return value == null ? "" : String.valueOf(value).toLowerCase();
        }
    }

    private void addColorRowMatch(int rowIdx, List<String> cols, Map<Integer, List<Integer>> changeColorRowCellIdxMap) {
        List<Integer> lst = new ArrayList<Integer>();
        for (int ii = 0; ii < cols.size(); ii++) {
            lst.add(ii);
            ;
        }
        changeColorRowCellIdxMap.put(rowIdx, lst);
    }

    private void addColorCellMatch(int rowIdx, int cellIdx, Map<Integer, List<Integer>> changeColorRowCellIdxMap) {
        List<Integer> cellLst = new ArrayList<Integer>();
        if (changeColorRowCellIdxMap.containsKey(rowIdx)) {
            cellLst = changeColorRowCellIdxMap.get(changeColorRowCellIdxMap);
        }
        cellLst.add(cellIdx);
        changeColorRowCellIdxMap.put(rowIdx, cellLst);
    }

    /**
     * 計算欄位型態
     * 
     * @param queryLst
     * @return
     */
    private Triple<List<String>, List<Class<?>>, List<Object[]>> fixPairToTripleQueryResult(Pair<List<String>, List<Object[]>> queryLst) {
        List<Object[]> lst = queryLst.getRight();
        TreeMap<Integer, Class<?>> typeMap = new TreeMap<Integer, Class<?>>();
        A: for (int ii = 0; ii < lst.size(); ii++) {
            if (queryLst.getLeft().size() == typeMap.size()) {
                break A;
            }
            Object[] arry = lst.get(ii);
            B: for (int jj = 0; jj < arry.length; jj++) {
                if (typeMap.containsKey(jj)) {
                    continue;
                }
                if (arry[jj] != null) {
                    typeMap.put(jj, arry[jj].getClass());
                }
            }
        }
        for (int ii = 0; ii < queryLst.getLeft().size(); ii++) {
            if (!typeMap.containsKey(ii)) {
                typeMap.put(ii, Object.class);
            }
        }
        List<Class<?>> typeLst = new ArrayList<Class<?>>(typeMap.values());
        return Triple.of(queryLst.getLeft(), typeLst, queryLst.getRight());
    }

    public void filterRowText(String rowFilterText, boolean isIgnoreFirstColumn, boolean isKeepMatchOnly, Triple<List<String>, List<Class<?>>, List<Object[]>> queryList) {
        List<Object[]> qList = new ArrayList<Object[]>();

        changeColorRowCellIdxMap = new HashMap<Integer, List<Integer>>();

        FindTextHandler finder = new FindTextHandler(rowFilterText, "^");
        boolean allMatch = finder.isAllMatch();

        List<String> cols = queryList.getLeft();

        boolean hasNoColumn = isIgnoreFirstColumn;

        for (int rowIdx = 0; rowIdx < queryList.getRight().size(); rowIdx++) {
            Object[] rows = queryList.getRight().get(rowIdx);
            if (allMatch) {
                qList.add(rows);
                // addColorRowMatch(rowIdx, cols,
                // changeColorRowCellIdxMap);
                continue;
            }

            B: for (int ii = 0; ii < cols.size(); ii++) {
                String value = finder.valueToString(rows[ii]);
                for (String text : finder.getArry()) {
                    if (value.contains(text)) {
                        int realCol = ii;
                        if (hasNoColumn) {
                            realCol = realCol + 1;
                        }
                        if (isKeepMatchOnly) {
                            addColorCellMatch(qList.size(), realCol, changeColorRowCellIdxMap);
                        } else {
                            addColorCellMatch(rowIdx, realCol, changeColorRowCellIdxMap);
                        }
                        qList.add(rows);
                        break B;
                    }
                }
            }
        }

        System.out.println("qList - " + qList.size());
        System.out.println("changeColorRowCellIdxMap - " + changeColorRowCellIdxMap);

        if (isKeepMatchOnly) {
            // 過濾欄位紀錄
            rowFilterResult = fixPairToTripleQueryResult(Pair.of(cols, qList));
        } else {
            rowFilterResult = queryList;
        }
    }

    public Pair<Triple<List<String>, List<Class<?>>, List<Object[]>>, Map<Integer, List<Integer>>> getResultFinal() {
        return Pair.of(rowFilterResult, changeColorRowCellIdxMap);
    }
}