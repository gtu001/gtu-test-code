package gtu.google;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class MultimapTest {

	public static void main(String[] args) throws IOException{
		Multimap<String, String> letterBag = HashMultimap.create();
		Properties prop = new Properties();
		prop.load(MultimapTest.class.getResourceAsStream("RSCD0118.properties"));
		for(Map.Entry<Object, Object> entry : prop.entrySet()){
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
			key = key.substring(0, 5);
			letterBag.put(key, value);
		}
		for(String key : letterBag.keySet()){
			Collection<String> values = letterBag.get(key);
			System.out.println(key + "->" + values);
		}
		
		//HashMultimap ---------------- values不重複的
		//ArrayListMultimap ---------------- values重複的
		Multimap<String, String> letterBag1 = HashMultimap.create();
		Multimap<String, String> letterBag2 = ArrayListMultimap.create();
		letterBag1.put("a", "bb");
		letterBag1.put("a", "bb");
		letterBag2.put("a", "bb");
		letterBag2.put("a", "bb");
		System.out.println(letterBag1);
		System.out.println(letterBag2);
	}
}
