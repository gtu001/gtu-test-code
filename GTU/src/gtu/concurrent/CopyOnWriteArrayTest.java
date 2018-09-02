package gtu.concurrent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class CopyOnWriteArrayTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<String> btsIdForUpdateList = new CopyOnWriteArrayList<String>();
        btsIdForUpdateList.add("test");

        Set<String> mailContentToMemberSet = new CopyOnWriteArraySet<String>();
        mailContentToMemberSet.add("test");
    }
}
