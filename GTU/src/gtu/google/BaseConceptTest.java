package gtu.google;

import gtu.log.Log.Systen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;

public class BaseConceptTest {

    public static void main(String[] args) {
    	Person p = new Person();
    	p.name = "John";
    	p.nickname = "bear";
    	Systen.out.println(p);
    	
        Preconditions.checkState(true, "ok!");// 若為false則拋錯

        ConcurrentMap<String, String> recommendations = new MapMaker().weakKeys().concurrencyLevel(1).makeMap();
        Systen.out.println(recommendations);

        Systen.out.println("done..");
    }
}

class Person implements Comparable<Person>{
    String name, nickname;

    @Override
    public boolean equals(Object object) {
        if (object instanceof Person) {
            Person that = (Person) object;
            return Objects.equal(this.name, that.name) && Objects.equal(this.nickname, that.nickname);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, nickname);
    }

    public String preferredName() {
        return Objects.firstNonNull(nickname, name);
    }

    @Override
    public int compareTo(Person other) {
        return ComparisonChain.start()//
                .compare(name, other.name)//
                .compare(nickname, other.nickname)//
                .result();//
    }

    @Override
	public String toString() {
    	return Objects.toStringHelper(this).add("name", name).add("nickname", nickname).toString();
	}
}
