package gtu.collection.apache;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.ClosureUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.TransformerUtils;

public class CollectionUtilsTest {

    public static void main(String[] args) {

        // 四種主要util
        Factory factory = FactoryUtils.prototypeFactory("test");// clone

        Closure closure = ClosureUtils.nopClosure();// do nothing

        Transformer trans = TransformerUtils.stringValueTransformer();

        Predicate nullPred = PredicateUtils.nullPredicate();

        // 測試1

        List<String> list = Arrays.asList("aaa", "bbb", "ccc");
        Collection transOk = CollectionUtils.collect(list, trans);

        // 測試2
        System.out.println("nullPred = " + nullPred.evaluate(null));

        // 測試3
        CollectionUtils.forAllDo(transOk, closure);

        System.out.println("done...");
    }
}
