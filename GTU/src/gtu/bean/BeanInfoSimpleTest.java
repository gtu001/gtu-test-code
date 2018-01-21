package gtu.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;


public class BeanInfoSimpleTest {

    public static void main(String[] args) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(BeanInfoSimpleTest.class, Introspector.USE_ALL_BEANINFO);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        if (propertyDescriptors != null) {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor != null) {
                    String name = propertyDescriptor.getName();
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    Class<?> aType = propertyDescriptor.getPropertyType();
                    System.out.println("name = " + name);
                    System.out.println("read = " + readMethod != null ? readMethod.getName() : "NA");
                    System.out.println("write = " + writeMethod != null ? writeMethod.getName() : "NA");
                    System.out.println("type = " + aType);
                }
            }
        }
    }

}
