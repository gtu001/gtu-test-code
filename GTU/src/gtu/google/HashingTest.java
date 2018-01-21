package gtu.google;

import java.nio.charset.Charset;

import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;

public class HashingTest {

	public static void main(String[] args) {
		Person person = new Person();
		Hashing.murmur3_128()
				//
				.newHasher()//
				.putString(person.name, Charset.forName("utf8"))
				.putInt(person.age)//
				.putLong(person.loginDate)
				.hash();
		
//		● A unified user-friendly API for all hash functions
//		● Seedable 32- and 128-bit implementations of murmur3
//		● md5(), sha1(), sha256(), sha512() adapters
//		○ change only one line of code to switch between these
//		and murmur etc.
//		● goodFastHash(int bits), for when you don't care
//		what algorithm you use
//		● General utilities for HashCode instances, like
//		combineOrdered / combineUnordered
	}

	static class Person {
		String name;
		int age;
		long loginDate;
	}
}
