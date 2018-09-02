package gtu.collection.apache;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Transformer;

public class CollectionUtils_3DListTo2D {

    public static void main(String[] args) {
        Collection<String> list1 = Arrays.asList("A", "B", "C");
        Collection<String> list2 = Arrays.asList("D", "E");
        Collection<String> list3 = Arrays.asList("F", "G", "H", "I");

        System.out.println(com.google.common.collect.Iterables.concat(list1, list2, list3));

        Collection<?> lists = Arrays.asList(list1, list2, list3);

        System.out.println(lists);

        Transformer collection_to_iterator = new Transformer() {
            public Object transform(Object input) {
                return ((Collection<?>) input).iterator();
            }
        };

        Iterator<?> iterator = IteratorUtils.chainedIterator(CollectionUtils.collect(lists, collection_to_iterator));

        while (iterator.hasNext()) {
            Object o = iterator.next();
            System.out.print(o);
            System.out.print(", ");
        }

        IteratorUtils.toList(iterator);
    }

}
