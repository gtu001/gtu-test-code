package gtu.array;

public class ArraysTest {

    public static void main(String[] args) {
        int[] testArray = { 1, 2, 3, 4, 5 };
        Class<?> whatWG = testArray.getClass();
        System.out.println("type name=" + whatWG.getName() + " ; ");
        if (whatWG.isArray()) {
            Class<?> elementType = whatWG.getComponentType();
            System.out.println(" is Array of " + elementType);
            System.out.println(" ; Array size: " + java.lang.reflect.Array.getLength(testArray));
            if (elementType.isPrimitive())
                System.out.println(", element is Primitive");
            else { // is Object
                System.out.println(", element NOT Primitive\n  --> its element: ");
                try {
                    Object y = java.lang.reflect.Array.get(testArray, 0); // x[0]
                } catch (Exception e) {
                    System.out.println(" x[0] NOT exist!  null ?");
                }
            }// if..else
        } else {
            System.out.println(" NOT an Array"); // if .. isArray(
        }
    }
}
