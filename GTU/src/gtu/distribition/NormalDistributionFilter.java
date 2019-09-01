package gtu.distribition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gtu.collection.ListUtil;

public abstract class NormalDistributionFilter<T> {
    private List<T> orignLst;
    private List<BigDecimal> keyLst;
    private Map<BigDecimal, List<T>> valMap;
    private List<T> allLst;

    public NormalDistributionFilter(List<T> orignLst) {
        this.orignLst = orignLst;
        preInit();
    }

    public abstract boolean isNeedPredict(T bean);

    public abstract BigDecimal getValue(T bean);

    public abstract Comparator<T> getCompare();

    private void preInit() {
        valMap = new HashMap<BigDecimal, List<T>>();
        for (T d : this.orignLst) {
            List<T> innLst = new ArrayList<T>();
            if (isNeedPredict(d)) {
                BigDecimal key = getValue(d);
                if (valMap.containsKey(key)) {
                    innLst = valMap.get(key);
                }
                innLst.add(d);
                valMap.put(key, innLst);
            }
        }
        keyLst = new ArrayList<BigDecimal>(valMap.keySet());
        Collections.sort(keyLst);

        allLst = new ArrayList<T>();
        for (int ii = 0; ii < keyLst.size(); ii++) {
            BigDecimal key = keyLst.get(ii);
            if (valMap.containsKey(key)) {
                allLst.addAll(valMap.get(key));
            }
        }
    }

    public List<T> getRangeLst(double startPercent, double endPercent) {
        return ListUtil.getRangePercentLst(allLst, startPercent, endPercent, getCompare());
    }

    public BigDecimal getRangeValue(double startPercent, double endPercent) {
        List<T> resultLst = getRangeLst(startPercent, endPercent);
        BigDecimal totalVal = BigDecimal.ZERO;
        for (T b : resultLst) {
            BigDecimal val = getValue(b);
            totalVal = totalVal.add(val);
        }
        return totalVal.divide(new BigDecimal(resultLst.isEmpty() ? 1 : resultLst.size()), 20);
    }
}