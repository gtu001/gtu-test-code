package _temp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test55 {

    public static void main(String[] args) throws IOException {
        List<String> lst1 = new ArrayList<String>();
        List<String> lst2 = new ArrayList<String>();
        
        lst1.add("111");
        lst1.add("333");
        lst2.add("111");
        lst2.add("333");
        
        boolean test = lst1.retainAll(lst2);
        System.out.println(test);
        System.out.println(lst1);
        System.out.println("done...");
    }
}
