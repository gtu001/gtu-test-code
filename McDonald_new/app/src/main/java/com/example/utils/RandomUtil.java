package com.example.utils;

import java.util.ArrayList;
import java.util.List;

public class RandomUtil {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("111");
        list.add("22");
        list.add("333");
        list.add("444");
        System.out.println(randomList(list));
        System.out.println("done...");
    }

    public static <T> List<T> randomList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<T>();
        }
        Object[] values = new Object[list.size()];
        int index = 0;
        do {
            int pos = rangeInteger(0, list.size() - 1);
            values[index++] = list.get(pos);
            list.remove(pos);
        } while (!list.isEmpty());
        List<T> lst2 = new ArrayList<T>();
        for (int ii = 0; ii < values.length; ii++) {
            lst2.add((T) values[ii]);
        }
        return lst2;
    }
    
    public static <T> T random(T... ts){
        List<T> list = new ArrayList<T>();
        for(T x : ts){
            list.add(x);
        }
        return randomList(list).get(0);
    }

    private static final Character[] randomCharArray;
    static {
        List<Character> list = new ArrayList<Character>();
        addChar(new char[] { 'a', 'z' }, list);
        addChar(new char[] { 'A', 'Z' }, list);
        addChar(new char[] { '0', '9' }, list);
        randomCharArray = list.toArray(new Character[list.size()]);
    }

    public static String randomStr(int length) {
        StringBuilder sb = new StringBuilder();
        int max = randomCharArray.length - 1;
        for (int ii = 0; ii < length; ii++) {
            sb.append(randomCharArray[rangeInteger(0, max)]);
        }
        return sb.toString();
    }

    public static char randomChar(char... ch) {
        int max = ch.length - 1;
        return ch[rangeInteger(0, max)];
    }

    public static String numberStr(int length) {
        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < length; ii++) {
            sb.append(String.valueOf(rangeInteger(0, 9)));
        }
        return sb.toString();
    }

    public static int rangeInteger(int start, int end) {
        return (int) (Math.random() * (end - start + 1)) + start;
    }

    public static String lowerCase(int length) {
        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < length; ii++) {
            sb.append(lowerCase());
        }
        return sb.toString();
    }

    public static String upperCase(int length) {
        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < length; ii++) {
            sb.append(upperCase());
        }
        return sb.toString();
    }

    private static char lowerCase() {
        return (char) rangeInteger((int) 'a', (int) 'z');
    }

    private static char upperCase() {
        return (char) rangeInteger((int) 'A', (int) 'Z');
    }

    private static void addChar(char[] pairs, List<Character> list) {
        for (int ii = pairs[0]; ii <= pairs[1]; ii++) {
            list.add((char) ii);
        }
    }
}
