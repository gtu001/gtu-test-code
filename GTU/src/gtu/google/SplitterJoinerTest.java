package gtu.google;

import gtu.log.Log.Systen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.collect.Iterables;

public class SplitterJoinerTest {

	public static void main(String[] args) {
		//splitter ----------------------------------------------
		Iterable<String> list = new ArrayList<String>();
		list = Splitter.on(',')//
				.trimResults()//
				.omitEmptyStrings()//
				.split(" foo, ,bar, quux,");//
		Systen.out.println(list);
		
        Iterable<String> pieces = Splitter.on(',').split("trivial,example");
        Systen.out.println(Iterables.toString(pieces));

        Iterable<String> pieces2 = Splitter.onPattern("\\s*,")//
                .trimResults().omitEmptyStrings()//
//                .splitToList("    aaa bbb, ccc   , ddddd, vvvsvsv eee, fff 33f ,, ");
                .split("    aaa bbb, ccc   , ddddd, vvvsvsv eee, fff 33f ,, ");
        Systen.out.println(Iterables.toString(pieces2));
        Systen.out.println(Splitter.fixedLength(5).split("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"));

		//joiner ----------------------------------------------
		String values = Joiner.on(", ")//
				// .skipNulls()//跳過null
				.useForNull("XXX")// 若有null則為此預設值
				.join("Kurt", "Kevin", null, "Chris");
		Systen.out.println(values);

        String s = Joiner.on(", ").join(Arrays.asList("aaa", "bbb"));
        Systen.out.println(s);

        StringBuilder sb = new StringBuilder("xxxxxx");
        Joiner.on("|").skipNulls().appendTo(sb, Arrays.asList("aaa", "bbb"));
        Systen.out.println(sb);
        
		// MapJoiner----------------------------------------------------
		// 使用map作join
		MapJoiner mapJoiner = Joiner.on("; ").useForNull("NODATA").withKeyValueSeparator(":");
		Map<String, String> map = new HashMap<String, String>();
		map.put("aaa", null);
		map.put("bbb", "11");
		map.put(null, "cccc");
		Systen.out.println(map);
		StringBuilder sb2 = new StringBuilder();
		// mapJoiner.appendTo(sb2, map.entrySet());
		mapJoiner.appendTo(sb2, map);
		Systen.out.println(sb2);
	}
}
