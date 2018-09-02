package gtu.stack;

import java.util.ArrayDeque;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.log4j.Logger;

public class ArrayDequeTest {

    private Logger log = Logger.getLogger(getClass());
    private ArrayDeque test = null; 


    //public static void main(String[] args){
    //}


    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    @BeforeClass                                                       
    public static void setUpBeforeClass() throws Exception {           
        //log.debug("# setUpBeforeClass ...");                
    }                                                                  
    @AfterClass                                                        
    public static void tearDownAfterClass() throws Exception {         
        //log.debug("# tearDownAfterClass ...");              
         unit = null;
    }                                                                  
    @Before                                                            
    public void setUp() throws Exception {                             
        //log.debug("# setUp ...");                           
         test = new ArrayDeque();
    }                                                                  
    @After                                                             
    public void tearDown() throws Exception {                          
        //log.debug("# tearDown ...");                        
         unit = null;
    }                                                                  
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX



        //PUBLIC

    @Test
    public void testPeekFirst() throws Exception {
        log.debug("# testPeekFirst ...");
        log.debug("peekFirst = " + test.peekFirst());
    }

    @Test
    public void testAddFirst() throws Exception {
        log.debug("# testAddFirst ...");
        log.debug("addFirst = " + test.addFirst(java.lang.Object));
    }

    @Test
    public void testAddLast() throws Exception {
        log.debug("# testAddLast ...");
        log.debug("addLast = " + test.addLast(java.lang.Object));
    }

    @Test
    public void testPollFirst() throws Exception {
        log.debug("# testPollFirst ...");
        log.debug("pollFirst = " + test.pollFirst());
    }

    @Test
    public void testRemoveLastOccurrence() throws Exception {
        log.debug("# testRemoveLastOccurrence ...");
        log.debug("removeLastOccurrence = " + test.removeLastOccurrence(java.lang.Object));
    }

    @Test
    public void testGetLast() throws Exception {
        log.debug("# testGetLast ...");
        log.debug("getLast = " + test.getLast());
    }

    @Test
    public void testRemoveLast() throws Exception {
        log.debug("# testRemoveLast ...");
        log.debug("removeLast = " + test.removeLast());
    }

    @Test
    public void testToArray() throws Exception {
        log.debug("# testToArray ...");
        log.debug("toArray = " + test.toArray());
        log.debug("toArray = " + test.toArray());
    }

    @Test
    public void testIterator() throws Exception {
        log.debug("# testIterator ...");
        log.debug("iterator = " + test.iterator());
    }

    @Test
    public void testClone() throws Exception {
        log.debug("# testClone ...");
        log.debug("clone = " + test.clone());
        log.debug("clone = " + test.clone());
    }

    @Test
    public void testElement() throws Exception {
        log.debug("# testElement ...");
        log.debug("element = " + test.element());
    }

    @Test
    public void testOfferLast() throws Exception {
        log.debug("# testOfferLast ...");
        log.debug("offerLast = " + test.offerLast(java.lang.Object));
    }

    @Test
    public void testSize() throws Exception {
        log.debug("# testSize ...");
        log.debug("size = " + test.size());
    }

    @Test
    public void testOffer() throws Exception {
        log.debug("# testOffer ...");
        log.debug("offer = " + test.offer(java.lang.Object));
    }

    @Test
    public void testPoll() throws Exception {
        log.debug("# testPoll ...");
        log.debug("poll = " + test.poll());
    }

    @Test
    public void testIsEmpty() throws Exception {
        log.debug("# testIsEmpty ...");
        log.debug("isEmpty = " + test.isEmpty());
    }

    @Test
    public void testClear() throws Exception {
        log.debug("# testClear ...");
        log.debug("clear = " + test.clear());
    }

    @Test
    public void testGetFirst() throws Exception {
        log.debug("# testGetFirst ...");
        log.debug("getFirst = " + test.getFirst());
    }

    @Test
    public void testRemoveFirst() throws Exception {
        log.debug("# testRemoveFirst ...");
        log.debug("removeFirst = " + test.removeFirst());
    }

    @Test
    public void testPollLast() throws Exception {
        log.debug("# testPollLast ...");
        log.debug("pollLast = " + test.pollLast());
    }

    @Test
    public void testPeek() throws Exception {
        log.debug("# testPeek ...");
        log.debug("peek = " + test.peek());
    }

    @Test
    public void testRemoveFirstOccurrence() throws Exception {
        log.debug("# testRemoveFirstOccurrence ...");
        log.debug("removeFirstOccurrence = " + test.removeFirstOccurrence(java.lang.Object));
    }

    @Test
    public void testRemove() throws Exception {
        log.debug("# testRemove ...");
        log.debug("remove = " + test.remove(java.lang.Object));
        log.debug("remove = " + test.remove());
    }

    @Test
    public void testOfferFirst() throws Exception {
        log.debug("# testOfferFirst ...");
        log.debug("offerFirst = " + test.offerFirst(java.lang.Object));
    }

    @Test
    public void testPush() throws Exception {
        log.debug("# testPush ...");
        log.debug("push = " + test.push(java.lang.Object));
    }

    @Test
    public void testDescendingIterator() throws Exception {
        log.debug("# testDescendingIterator ...");
        log.debug("descendingIterator = " + test.descendingIterator());
    }

    @Test
    public void testPeekLast() throws Exception {
        log.debug("# testPeekLast ...");
        log.debug("peekLast = " + test.peekLast());
    }

    @Test
    public void testAdd() throws Exception {
        log.debug("# testAdd ...");
        log.debug("add = " + test.add(java.lang.Object));
    }

    @Test
    public void testPop() throws Exception {
        log.debug("# testPop ...");
        log.debug("pop = " + test.pop());
    }

    @Test
    public void testContains() throws Exception {
        log.debug("# testContains ...");
        log.debug("contains = " + test.contains(java.lang.Object));
    }


        //OTHER

    @Test
    public void testAccess$200() throws Exception {
        log.debug("# testAccess$200 ...");
        log.debug("access$200 = " + ArrayDeque.access$200(java.util.ArrayDeque));
    }

    @Test
    public void testAccess$500() throws Exception {
        log.debug("# testAccess$500 ...");
        log.debug("access$500 = " + ArrayDeque.access$500(java.util.ArrayDeque, int));
    }

    @Test
    public void testAccess$400() throws Exception {
        log.debug("# testAccess$400 ...");
        log.debug("access$400 = " + ArrayDeque.access$400(java.util.ArrayDeque));
    }

    @Test
    public void testAccess$300() throws Exception {
        log.debug("# testAccess$300 ...");
        log.debug("access$300 = " + ArrayDeque.access$300(java.util.ArrayDeque));
    }
}
