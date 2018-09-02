package gtu.google;

import com.google.common.collect.HashBasedTable;


public class HashBasedTableTest {

	public static void main(String[] args) {
		//		A		B		C		D
		//1		a1		b1		c1		d1
		//2		a2		b2		c2		d2
		//3		a3		b3		c3		d3
		//4		a4		b4		c4		d4
		HashBasedTable<String,String,String> hashTable = HashBasedTable.create();
		hashTable.put("1", "A", "a1");
		hashTable.put("2", "A", "a2");
		hashTable.put("3", "A", "a3");
		hashTable.put("4", "A", "a4");
		hashTable.put("1", "B", "b1");
		hashTable.put("2", "B", "b2");
		hashTable.put("3", "B", "b3");
		hashTable.put("4", "B", "b4");
		hashTable.put("1", "C", "c1");
		hashTable.put("2", "C", "c2");
		hashTable.put("3", "C", "c3");
		hashTable.put("4", "C", "c4");
		hashTable.put("1", "D", "d1");
		hashTable.put("2", "D", "d2");
		hashTable.put("3", "D", "d3");
		hashTable.put("4", "D", "d4");
		System.out.println(hashTable.get("3", "B"));
		
//		A "two-tier" map, or a map with two keys (called the "row key" and "column key").
//		● can be sparse or dense
//		○ HashBasedTable: uses hash maps (sparse)
//		○ TreeBasedTable: uses tree maps (sparse)
//		○ ArrayTable: uses V[][] (dense)
//		● many views on the underlying data are possible
//		○ row or column map (of maps)
//		○ row or column key set
//		○ set of all cells (as <R, C, V> entries)
//		● Use instead of Map<R, Map<C, V>>
	}
}
