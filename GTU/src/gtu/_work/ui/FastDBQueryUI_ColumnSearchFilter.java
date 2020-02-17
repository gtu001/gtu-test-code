package gtu._work.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    public void filterText(String filterText) {
        __filterText(filterText);
    }
    
    public Triple<List<String>, List<Class<?>>, List<Object[]>> getResult() {
        return resultList;
    }
}