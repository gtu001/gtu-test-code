package gtu.google;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class ImmutableTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> nameList = ImmutableList.of("Duke", "Java", "Oracle");
		Set<String> nameSet = ImmutableSet.of("Duke", "Java", "Oracle");
		Map<String, Integer> userDB = ImmutableMap.of("Duke", 123, "Java", 456);

		List<String> list1 = Lists.newArrayList("aa", "bb", "cc");
		List<String> list2 = ImmutableList.copyOf(list1);
	}

}
