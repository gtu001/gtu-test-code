package gtu.google;

import org.junit.Test;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;

public class RangeTest {
	/*
	 * 　在Guava中新增了一個新的類型Range，從名字就可以瞭解到，這個是和區間有關的資料結構。從Google官方文檔可以得到定
	 * 義：Range定義了連續跨度的範圍邊界，這個連續跨度是一個可以比較的類型(Comparable type)。比如1到100之間的整型資料。
	 * 　　在數學裡面的範圍是有邊界和無邊界之分的
	 * ；同樣，在Guava中也有這個說法。如果這個範圍是有邊界的，那麼這個範圍又可以分為包括開集（不包括端點）和閉集
	 * （包括端點）；如果是無解的可以用+∞表示。如果枚舉的話，一共有九種範圍表示： Guava Range 概念，範圍和方法 概念 表示範圍
	 * guava對應功能方法 (a..b) {x | a < x < b} open(C, C) [a..b] {x | a <= x <= b}
	 * closed(C, C) [a..b) {x | a <= x < b} closedOpen(C, C) (a..b] {x | a < x
	 * <= b} openClosed(C, C) (a..+∞) {x | x > a} greaterThan(C) [a..+∞) {x | x
	 * >= a} atLeast(C) (-∞..b) {x | x < b} lessThan(C) (-∞..b] {x | x <= b}
	 * atMost(C) (-∞..+∞) all values all()
	 * 　　上表中的guava對應功能方法那一欄表示Range類提供的方法，分別來表示九種可能出現的範圍區間。如果區間兩邊都存在範圍，在這種情況
	 * 下，區間右邊的數不可能比區間左邊的數小。在極端情況下，區間兩邊的數是相等的，但前提條件是最少有一個邊界是閉集的，否則是不成立的。比如：
	 * 　　[a..a] : 裡面只有一個數a； 　　[a..a); (a..a] : 空的區間範圍，但是是有效的； 　　(a..a) :
	 * 這種情況是無效的，構造這樣的Range將會拋出異常。
	 * 　　在使用Range時需要注意：在構造區間時，儘量使用不可改變的類型。如果你需要使用可變的類型
	 * ，在區間類型構造完成的情況下，請不要改變區間兩邊的數。
	 */

	@Test
	public void testRange1() {
		System.out.println("open:" + Range.open(1, 10));
		System.out.println("closed:" + Range.closed(1, 10));
		System.out.println("closedOpen:" + Range.closedOpen(1, 10));
		System.out.println("openClosed:" + Range.openClosed(1, 10));
		System.out.println("greaterThan:" + Range.greaterThan(10));
		System.out.println("atLeast:" + Range.atLeast(10));
		System.out.println("lessThan:" + Range.lessThan(10));
		System.out.println("atMost:" + Range.atMost(10));
		System.out.println("all:" + Range.all());
		System.out.println("closed:" + Range.closed(10, 10));
		System.out.println("closedOpen:" + Range.closedOpen(10, 10));
		// 會拋出異常
		System.out.println("open:" + Range.open(10, 10));
	}

	// 　　此外,範圍可以構造實例通過綁定類型顯式,例如：
	@Test
	public void testRange2() {
		System.out.println("downTo:" + Range.downTo(4, BoundType.OPEN));
		System.out.println("upTo:" + Range.upTo(4, BoundType.CLOSED));
		System.out.println("range:"
				+ Range.range(1, BoundType.CLOSED, 4, BoundType.OPEN));
	}

	// 　　1.contains：判斷值是否在當前Range內
	@Test
	public void testContains() {
		System.out.println(Range.closed(1, 3).contains(2));
		System.out.println(Range.closed(1, 3).contains(4));
		System.out.println(Range.lessThan(5).contains(5));
		System.out
				.println(Range.closed(1, 4).containsAll(Ints.asList(1, 2, 3)));
	}

	// 　　2.Endpoint相關查詢方法：
	@Test
	public void testQuery() {
		System.out.println("hasLowerBound:"
				+ Range.closedOpen(4, 4).hasLowerBound());
		System.out.println("hasUpperBound:"
				+ Range.closedOpen(4, 4).hasUpperBound());
		System.out.println(Range.closedOpen(4, 4).isEmpty());
		System.out.println(Range.openClosed(4, 4).isEmpty());
		System.out.println(Range.closed(4, 4).isEmpty());
		// Range.open throws IllegalArgumentException
		// System.out.println(Range.open(4, 4).isEmpty());

		System.out.println(Range.closed(3, 10).lowerEndpoint());
		System.out.println(Range.open(3, 10).lowerEndpoint());
		System.out.println(Range.closed(3, 10).upperEndpoint());
		System.out.println(Range.open(3, 10).upperEndpoint());
		System.out.println(Range.closed(3, 10).lowerBoundType());
		System.out.println(Range.open(3, 10).upperBoundType());
	}

	// 　　3.encloses方法：encloses(Range range)中的range是否包含在需要比較的range中
	@Test
	public void testEncloses() {
		Range<Integer> rangeBase = Range.open(1, 4);
		Range<Integer> rangeClose = Range.closed(2, 3);
		Range<Integer> rangeCloseOpen = Range.closedOpen(2, 4);
		Range<Integer> rangeCloseOther = Range.closedOpen(2, 5);
		System.out.println("rangeBase: " + rangeBase + " Enclose:"
				+ rangeBase.encloses(rangeClose) + " rangeClose:" + rangeClose);
		System.out.println("rangeBase: " + rangeBase + " Enclose:"
				+ rangeBase.encloses(rangeCloseOpen) + " rangeClose:"
				+ rangeCloseOpen);
		System.out.println("rangeBase: " + rangeBase + " Enclose:"
				+ rangeBase.encloses(rangeCloseOther) + " rangeClose:"
				+ rangeCloseOther);
	}

	// 　　4.isConnected：range是否可連接上
	@Test
	public void testConnected() {
		System.out.println(Range.closed(3, 5).isConnected(Range.open(5, 10)));
		System.out.println(Range.closed(0, 9).isConnected(Range.closed(3, 4)));
		System.out.println(Range.closed(0, 5).isConnected(Range.closed(3, 9)));
		System.out.println(Range.open(3, 5).isConnected(Range.open(5, 10)));
		System.out.println(Range.closed(1, 5).isConnected(Range.closed(6, 10)));
	}

	// 　　4.intersection：如果兩個range相連時，返回最大交集，如果不相連時，直接拋出異常
	@Test
	public void testIntersection() {
		System.out.println(Range.closed(3, 5).intersection(Range.open(5, 10)));
		System.out.println(Range.closed(0, 9).intersection(Range.closed(3, 4)));
		System.out.println(Range.closed(0, 5).intersection(Range.closed(3, 9)));
		System.out.println(Range.open(3, 5).intersection(Range.open(5, 10)));
		System.out
				.println(Range.closed(1, 5).intersection(Range.closed(6, 10)));
	}

	// 　　注意：第四和第五行代碼，當集合不相連時，會直接報錯
	// 　　5.span：獲取兩個range的並集，如果兩個range是兩連的，則是其最小range
	@Test
	public void testSpan() {
		System.out.println(Range.closed(3, 5).span(Range.open(5, 10)));
		System.out.println(Range.closed(0, 9).span(Range.closed(3, 4)));
		System.out.println(Range.closed(0, 5).span(Range.closed(3, 9)));
		System.out.println(Range.open(3, 5).span(Range.open(5, 10)));
		System.out.println(Range.closed(1, 5).span(Range.closed(6, 10)));
		System.out.println(Range.closed(1, 5).span(Range.closed(7, 10)));
	}
}
