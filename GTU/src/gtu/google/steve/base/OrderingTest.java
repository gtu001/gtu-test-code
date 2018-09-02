package gtu.google.steve.base;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

/**
 * @author Steve
 */
public class OrderingTest {
	
	/*
	 * 建立的方式
	 * ● Ordering.natural()
	 * ● new Ordering() { ... }
	 * ● Ordering.from(existingComparator);
	 * ● Ordering.explicit("alpha", "beta", "gamma");
	 */

    private Comparator<Person> lastNameComparator;
    private Comparator<Person> firstNameComparator;
    private List<Person> persons;

    @Before
    public void before() {
        lastNameComparator = new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return p1.lastName.compareTo(p2.lastName);
            }
        };

        firstNameComparator = new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return p1.firstName.compareTo(p2.firstName);
            }
        };

        persons = Lists.newArrayList(new Person("Alfred", "Hitchcock"), null, new Person("Homer", "Simpson"),
                new Person("Peter", "Fox"), new Person("Bart", "Simpson"));
    }

    @URLToImageTest
    public void testMinMax() {
        System.out.println("# testMinMax...");
        System.out.println("min(firstName) = " + Ordering.<Person> from(firstNameComparator).nullsFirst().min(persons));
        System.out.println("max(firstName) = " + Ordering.<Person> from(firstNameComparator).nullsFirst().max(persons));
        System.out.println("min(lastName) = " + Ordering.<Person> from(lastNameComparator).nullsFirst().min(persons));
        System.out.println("max(lastName) = " + Ordering.<Person> from(lastNameComparator).nullsFirst().max(persons));
    }

    @URLToImageTest
    public void testSortedCopy() {
        System.out.println("# testSortedCopy...");
        for (Person person : Ordering.<Person> from(firstNameComparator).nullsLast().sortedCopy(persons)) {
            System.out.println(person);
        }
    }

    /**
     * @author Steve Tien
     * @version 1.0, Jun 15, 2010
     */
    private class Person {
        private String firstName;
        private String lastName;

        private Person(String firstName, String lastName) {
            this.firstName = (firstName);
            this.lastName = (lastName);
        }

        @Override
        public String toString() {
            return "Person [firstName=" + firstName + ", lastName=" + lastName + "]";
        }
    }
    
    
    public static void main(String[] args) {
    	OrderingTest test = new OrderingTest();
        Person2 p1 = test.new Person2("troy", "0720318", "100", "engineer");
        Person2 p2 = test.new Person2("mary", "0900620", "300", "dancer");
        Person2 p3 = test.new Person2("joe", "0680925", "900", "president");
        Person2 p4 = test.new Person2("steve", "0731120", "600", "manager");
        Person2 p5 = test.new Person2("kim", "0891004", "500", "teacher");
        List<Person> list = Lists.newArrayList(p1, p2, p3, p4, p5);
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
				Ordering.natural().nullsFirst().reverse()
						.onResultOf(new Function() {
							@Override
							public Comparable apply(Object arg0) {
								return null;
							}
						});
                return 0;
            }
        });
        for(Person p : list){
            System.out.println(p);
        }
    }
    
    private class Person2 {
        String name;
        String birth;
        String salary;
        String job;
        
        public Person2(String name, String birth, String salary, String job) {
            super();
            this.name = name;
            this.birth = birth;
            this.salary = salary;
            this.job = job;
        }

        @Override
        public String toString() {
            return "Person [name=" + name + ", birth=" + birth + ", salary=" + salary + ", job=" + job + "]";
        }
    }
}
