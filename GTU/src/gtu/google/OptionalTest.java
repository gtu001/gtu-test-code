package gtu.google;

import gtu.log.Log.Systen;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Optional;



public class OptionalTest {

    public static void main(String[] args) {
//    	● Creating an Optional<T>
    	Optional<String> val1 = Optional.of("哈哈");//notNull
    	Optional<String> val2 = Optional.absent();
    	Optional<String> val3 = Optional.fromNullable(null);//maybeNull
//    	● Unwrapping an Optional<T>
    	String str1 = val1.get(); // maybe ISE
    	String str2 = val2.or("defaultValue1");//若為null則回預設值
    	String str3 = val3.or("defaultValue2");//若為null則回預設值
    	String str4 = val3.orNull();
//    	● Other useful methods:
    	Set<String> sets = val1.asSet(); // 0 or 1
		Optional<Integer> intVal = val1.transform(new Function<String, Integer>() {
			@Override
			public Integer apply(String arg0) {
				return -1;
			}
		});
		
		Systen.out.println(val1);
		Systen.out.println(val2);
		Systen.out.println(val3);
		Systen.out.println(str1);
		Systen.out.println(str2);
		Systen.out.println(str3);
		Systen.out.println(str4);
		Systen.out.println(sets + ".." + sets.size());
		Systen.out.println(intVal);
    }

}
