package _temp;

import java.util.ArrayList;
import java.util.List;

public class Test40 {

    public static void main(String[] args) {
//        System.out.println(new BigDecimal(-1).compareTo(BigDecimal.ZERO));
        
//        System.out.println(StringUtils.substring("1234567", 0, -2));
        
       List<String> lst = new ArrayList<String>();
       lst.add("0");
       lst.add("1");
       lst.add("2");
       lst.add("3");
       lst.add("4");
       lst.add("5");
       
       System.out.println("v2 --- " + lst.subList(3, lst.size()));
    }

}
