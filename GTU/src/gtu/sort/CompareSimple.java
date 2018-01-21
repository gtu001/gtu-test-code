package gtu.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Troy 2009/02/02
 * 
 */
public class CompareSimple {

    int value;

    public static void main(String[] args) {
        CompareSimple cs = new CompareSimple();

        ComObj2 c1 = new ComObj2();
        ComObj2 c2 = new ComObj2();
        ComObj2 c3 = new ComObj2();
        ComObj2 c4 = new ComObj2();
        ComObj2 c5 = new ComObj2();
        ComObj2 c6 = new ComObj2();

        c1.seq = 4;
        c2.seq = 2;
        c3.seq = 30;
        c4.seq = 1;
        c5.seq = 10;
        c6.seq = 7;

        List<ComObj2> list = new ArrayList<ComObj2>();
        list.add(c1);
        list.add(c2);
        list.add(c3);
        list.add(c4);
        list.add(c5);
        list.add(c6);
        
        List<ComObj2> list2 = new ArrayList<ComObj2>();
        list2.add(c1);
        list2.add(c2);
        list2.add(c3);
        list2.add(c4);
        list2.add(c5);
        list2.add(c6);

        Collections.sort(list);
        
        for(ComObj2 c : list){
            System.out.println("1=" + c);
        }
        
        Collections.sort(list2, new ComObjComparator());
        
        for(ComObj2 c : list2){
            System.out.println("2=" + c);
        }
    }

    private static class ComObjComparator implements Comparator<ComObj2> {
        public int compare(ComObj2 arg0, ComObj2 arg1) {
            if(arg0.seq > arg1.seq){
                return 1;
            }else if (arg0.seq < arg1.seq) {
                return -1;
            }
            return 0;
        }
    }

    private static class ComObj2 implements Comparable {
        private int seq;

        public int compareTo(Object arg0) {
            ComObj2 theirs = (ComObj2) arg0;
            if (this.seq > theirs.seq) {
                return 1;
            } else if (this.seq < theirs.seq) {
                return -1;
            }
            return 0;
        }

        @Override
        public String toString() {
            return "ComObj2 [seq=" + seq + "]";
        }
    }
}
