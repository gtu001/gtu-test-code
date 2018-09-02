package gtu.reflect.sun;

public class ReflectionTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        A.print();
    }

    static class A {
        static void print() {
            B.print();
        }
    }

    static class B {
        static void print() {
            C.print();
        }
    }

    static class C {
        static void print() {
            D.print();
        }
    }

    static class D {
        static void print() {
            System.out.println("0 = " + sun.reflect.Reflection.getCallerClass(0));
            System.out.println("1 = " + sun.reflect.Reflection.getCallerClass(1));
            System.out.println("2 = " + sun.reflect.Reflection.getCallerClass(2));
            System.out.println("3 = " + sun.reflect.Reflection.getCallerClass(3));
            System.out.println("4 = " + sun.reflect.Reflection.getCallerClass(4));
            System.out.println("5 = " + sun.reflect.Reflection.getCallerClass(5));
            System.out.println("5 = " + sun.reflect.Reflection.getCallerClass(6));
        }
    }

}
