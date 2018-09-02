package gtu.google;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;


public class MultisetTest {

    public static void main(String[] args) {
        List<String> words = Arrays.asList("one", "two", "three", "one", "three");
        Multiset<String> wordBag = HashMultiset.create(words);
        System.out.println(wordBag); // [two, one x 2, three x 2]
        for (String word : wordBag.elementSet()) {
            System.out.println(word + " -> " + wordBag.count(word));
        }

        Multiset<String> wordBag2 = HashMultiset.create();
        for (String word : words) {
            wordBag2.add(word, 10);
        }
        System.out.println(wordBag2);

        Multiset<String> wordBag3 = HashMultiset.create();
        for (String word : words) {
            wordBag3.setCount(word, 100);
        }
        System.out.println(wordBag3);
    }
}
