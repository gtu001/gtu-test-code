package gtu.apache;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.google.common.collect.Lists;

public class CompareToBuilderTest {

    public static void main(String[] args) {
        CompareToBuilderTest test = new CompareToBuilderTest();
        Person p1 = test.new Person("troy", "0720318", "100", "engineer");
        Person p2 = test.new Person("mary", "0900620", "300", "dancer");
        Person p3 = test.new Person("joe", "0680925", "900", "president");
        Person p4 = test.new Person("steve", "0731120", "600", "manager");
        Person p5 = test.new Person("kim", "0891004", "500", "teacher");
        List<Person> list = Lists.newArrayList(p1, p2, p3, p4, p5);
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return new CompareToBuilder()//
                        .append(o1.name, o2.name)//
                        .append(o1.birth, o2.birth)//
                        .append(o1.salary, o2.salary)//
                        .append(o1.job, o2.job)//
                        .toComparison();
            }
        });
        for(Person p : list){
            System.out.println(p);
        }
    }
    
    private class Person {
        String name;
        String birth;
        String salary;
        String job;
        
        public Person(String name, String birth, String salary, String job) {
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
