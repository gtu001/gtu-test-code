package gtu.google;

import org.python.google.common.collect.HashBiMap;

import com.google.common.collect.BiMap;

public class HashBiMapTest {

	public static void main(String[] args) {
		//Bi map是key,value皆不可重複的map
		HashBiMap<String,String> biMap = HashBiMap.<String,String>create();
		biMap.put("aaa", "AAA");
//		biMap.put("bbb", "AAA");
		System.out.println(biMap.inverse().get("AAA"));
	}
}
