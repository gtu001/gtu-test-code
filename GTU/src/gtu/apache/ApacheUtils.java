package gtu.apache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.ArrayUtils;

public class ApacheUtils {

	public static void main(String[] args) {
		ApacheUtils x = new ApacheUtils();
		x.forAllDo();
	}

	public void forAllDo() {
		Closure closure = new Closure() {
			public void execute(Object input) {
				StringBuffer sb = (StringBuffer) input;
				sb.append("append by closure!");
			}
		};
		Set<StringBuffer> bufferSet = new HashSet<StringBuffer>();
		bufferSet.add(new StringBuffer());
		bufferSet.add(new StringBuffer());
		CollectionUtils.forAllDo(bufferSet, closure);
		System.out.println(ArrayUtils.toString(bufferSet));
	}

	public void easyIteratorForMap() {
		Map bidiMap = new HashMap();
		MapIterator it = ((org.apache.commons.collections.BidiMap) bidiMap)
				.mapIterator();
		while (it.hasNext()) {
			Object key = it.next();
			Object value = it.getValue();
			System.out.println(key + "*****" + value);
		}
	}

	public void orderedMap() {
		OrderedMap map = new LinkedMap();
		map.put("FIVE", "5");
		map.put("SIX", "6");
		map.put("SEVEN", "7");
		System.out.println(map.firstKey()); // returns "FIVE"
		System.out.println(map.nextKey("FIVE")); // returns "SIX"
		System.out.println(map.nextKey("SIX")); // returns "SEVEN"
	}
}
