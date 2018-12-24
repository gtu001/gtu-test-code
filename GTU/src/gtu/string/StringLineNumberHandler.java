package gtu.string;

import java.io.IOException;
import java.io.StringReader;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

public class StringLineNumberHandler {

    public static TreeMap<Integer, Pair<Integer, Integer>> getLinePosMap(String textStr) {
        TreeMap<Integer, Pair<Integer, Integer>> treeMap = new TreeMap<Integer, Pair<Integer, Integer>>();
        StringReader reader = null;
        try {
            int lineStartPos = -1;
            int pos = 0;
            int linePos = 1;
            Integer val = null;
            reader = new StringReader(textStr);
            while ((val = reader.read()) != -1) {
                if (lineStartPos == -1) {
                    lineStartPos = pos;
                }
                if (val == 10) {
                    Pair<Integer, Integer> newLinePos = Pair.of(lineStartPos, pos);
                    treeMap.put(linePos, newLinePos);
                    linePos++;
                    lineStartPos = -1;
                }
                pos++;
            }
            if (lineStartPos < pos) {
                Pair<Integer, Integer> newLinePos = Pair.of(lineStartPos, pos);
                treeMap.put(linePos, newLinePos);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            reader.close();
        }
        return treeMap;
    }

    public static int getLineNumber(int pos, TreeMap<Integer, Pair<Integer, Integer>> linePosMap) {
        for (Integer lineNumber : linePosMap.keySet()) {
            Pair<Integer, Integer> pair = linePosMap.get(lineNumber);
            if (pair.getLeft() <= pos && pair.getRight() >= pos) {
                return lineNumber;
            }
        }
        return -1;
    }
}
