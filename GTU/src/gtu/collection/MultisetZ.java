package gtu.collection;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 像google的MultiSet一樣做累計的動作,可以累計各種形態數值
 */
public class MultisetZ<T> {
    Map<T, BigDecimal> addMap = new TreeMap<T, BigDecimal>();

    private MultisetZ(Collection<T> elements) {
        this.addAll(elements);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static MultisetZ create() {
        return new MultisetZ(Collections.emptyList());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static MultisetZ create(Collection elemnts) {
        return new MultisetZ(elemnts);
    }
    
    public void add(T key) {
        this.add(key, BigDecimal.valueOf(1));
    }

    public void add(T key, int count) {
        this.add(key, BigDecimal.valueOf(count));
    }

    public void add(T key, long count) {
        this.add(key, BigDecimal.valueOf(count));
    }

    public void add(T key, double count) {
        this.add(key, BigDecimal.valueOf(count));
    }

    public void add(T key, float count) {
        this.add(key, BigDecimal.valueOf(count));
    }

    private void add(T key, BigDecimal count) {
        BigDecimal value = BigDecimal.valueOf(0);
        if (addMap.containsKey(key)) {
            value = addMap.get(key);
        }
        value = value.add(count);
        addMap.put(key, value);
    }

    public void setCount(T key, int count) {
        this.setCount(key, BigDecimal.valueOf(count));
    }

    public void setCount(T key, long count) {
        this.setCount(key, BigDecimal.valueOf(count));
    }

    public void setCount(T key, double count) {
        this.setCount(key, BigDecimal.valueOf(count));
    }

    public void setCount(T key, float count) {
        this.setCount(key, BigDecimal.valueOf(count));
    }

    private void setCount(T key, BigDecimal count) {
        addMap.put(key, count);
    }

    public BigDecimal count(T key) {
        return addMap.get(key);
    }

    public Set<T> elementSet() {
        return addMap.keySet();
    }
    
    public Iterator<T> iterator() {
        return addMap.keySet().iterator();
    }
    
    public int size(){
        return addMap.size();
    }
    
    public void addAll(Collection<T> elements){
        for (T c : elements) {
            this.add(c);
        }
    }

    public boolean contains(T key){
        return addMap.containsKey(key);
    }
    
    public boolean isEmpty(){
        return addMap.isEmpty();
    }

    @Override
    public String toString() {
        return "MultisetZ " + addMap + "";
    }
}