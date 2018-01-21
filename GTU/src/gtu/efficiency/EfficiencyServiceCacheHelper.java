package gtu.efficiency;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.log4j.Logger;

/**
 * 效能調整快取工具
 */
public class EfficiencyServiceCacheHelper {

    private class CacheTest {
        CacheHelper<String> helper1 = new CacheHelper<String>("test1", true, new Transformer<Object[], String>() {
            public String transform(Object[] arg0) {
                List<Object> lst = new ArrayList<Object>();
                for (Object v : arg0) {
                    lst.add(v);
                }
                return lst.toString();
            }
        });

        CacheHelper<String> helper2 = new CacheHelper<String>("test2", true, new Transformer<Object[], String>() {
            public String transform(Object[] arg0) {
                List<Object> lst = new ArrayList<Object>();
                for (Object v : arg0) {
                    lst.add(v);
                }
                return lst.toString();
            }
        });
    }

    public static void main(String[] args) {
        EfficiencyServiceCacheHelper t = new EfficiencyServiceCacheHelper();
        CacheTest cache = t.new CacheTest();
        cache.helper1.get("10");
        cache.helper2.get("10");
        cache.helper1.get("10");
        cache.helper2.get("10");
        System.out.println("helper size = " + cache.helper1.map.size());
        System.out.println("helper size = " + cache.helper2.map.size());
        CacheHelper.printAll();
        System.out.println("done...");
    }

    public static class CacheHelper<Val> {
        private static final Logger logger = Logger.getLogger(CacheHelper.class);

        private static Set<CacheHelper<?>> REPORT_SET = new LinkedHashSet<CacheHelper<?>>();
        Transformer<Object[], Val> transformer;
        LRUMap<String, Val> map = new LRUMap<String, Val>();
        boolean cloneIfCan;
        int totalCount = 0;
        int cacheUseCount = 0;
        List<Long> duringLst = new ArrayList<Long>();
        String tag;

        public CacheHelper(String tag, boolean cloneIfCan, Transformer<Object[], Val> transformer) {
            this.tag = tag;
            this.cloneIfCan = cloneIfCan;
            this.transformer = transformer;
            REPORT_SET.add(this);
        }

        public Collection<Val> values() {
            return map.values();
        }

        private String toKey(Object... params) {
            List<Object> lst = new ArrayList<Object>();
            for (Object v : params) {
                lst.add(String.valueOf(v));
            }
            return lst.toString();
        }

        public static void printAll() {
            for (CacheHelper<?> d : REPORT_SET) {
                logger.info(d.getUseageReport());
            }
            logger.info("report size : " + REPORT_SET.size());
        }

        public String getUseageReport() {
            long totalDuring = 0;
            for (long d : duringLst) {
                totalDuring += d;
            }
            long avgDuring = 0;
            if (!duringLst.isEmpty()) {
                avgDuring = totalDuring / duringLst.size();
            }
            double useRate = 0;
            if (totalCount != 0) {
                useRate = ((double) cacheUseCount / (double) totalCount) * 100;
            }
            BigDecimal _useRate = new BigDecimal(useRate);
            _useRate = _useRate.setScale(3, RoundingMode.HALF_UP);
            return String.format("[%s] 快取次數 : %d / 總使用次數 : %d -> 快取使用率 : %s%% , 平均耗時 : %d, 總耗時 : %d (毫秒)", tag, cacheUseCount, totalCount, _useRate, avgDuring, totalDuring);
        }

        public Val get(Object... params) {
            totalCount++;
            String key = toKey(params);
            Val val = null;
            if (map.containsKey(key)) {
                cacheUseCount++;
                val = map.get(key);
            }else {
                During d = new During();
                val = transformer.transform(params);
                duringLst.add(d.get());
                map.put(key, val);
            }
            if(cloneIfCan) {
                val = getClone(val);
            }
            return val;
        }
        
        public void clear(){
            map.clear();
            totalCount = 0;
            cacheUseCount = 0;
            duringLst.clear();
        }
        
        private Val getClone(Val t){
            if(t instanceof Cloneable){
                try {
                    Method mth = Object.class.getDeclaredMethod("clone", new Class[0]);
                    mth.setAccessible(true);
                    return (Val)mth.invoke(t, new Object[0]);
                } catch (Exception e) {
                    logger.error("clone失敗 : " + e.getMessage(), e);
                }
            }
            return t;
        }

        private class During {
            long startTime = -1;

            During() {
                startTime = System.currentTimeMillis();
            }

            private long get() {
                return System.currentTimeMillis() - startTime;
            }
        }
    }
}
