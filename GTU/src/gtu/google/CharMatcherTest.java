package gtu.google;

import gtu.log.Log.Systen;

import com.google.common.base.CharMatcher;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class CharMatcherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String seriesId = CharMatcher.DIGIT.or(CharMatcher.is('-')).retainFrom("2jlsdfsdf8932kljd;jsd-dfsdf");// 只保留數字與"-"
        Systen.out.println(seriesId);
        
        String[] strings = new String[]{"aaa", "aEV", "FDS"};
		for (String string : strings) {
			if (CharMatcher.JAVA_UPPER_CASE.matchesAllOf(string)) {
				Systen.out.println("-->" + string);
			}
		}
	}

}
