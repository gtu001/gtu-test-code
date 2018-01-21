package gtu.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanMap {
    private Object bean;
    private String beanClassName;
    private Map<String, Method> readMethods;
    private Map<String, Method> writeMethods;
    private Map<String, Type> types;
    private List<String> names;

    public BeanMap() {
    }

    public BeanMap(Object bean) {
        this.bean = bean;
        beanClassName = bean.getClass().getName();
        initialize();
    }

    /**
     * set bean property value
     *
     * @param name
     * @param value
     * @throws Exception
     */
    public void set(String name, Object value) throws Exception {
        Type type = types.get(name);
        Method method = writeMethods.get(name);
        if (type == null || method == null) {
            return;
        } else {
            method.invoke(bean, value);
        }
    }

    /**
     * get bean property value
     *
     * @param name
     * @return
     * @throws Exception
     */
    public Object get(String name) throws Exception {
        Type type = types.get(name);
        Method method = readMethods.get(name);
        if (type == null || method == null) {
            throw new java.lang.NoSuchFieldException(beanClassName + "." + name);
        }
        return method.invoke(bean);
    }

    public List<String> getNames() {
        return this.names;
    }

    public Type getPropertyType(String name) throws Exception {
        return types.get(name);
    }

    /**
     * initialize bean map
     */
    private void initialize() {
        readMethods = new HashMap<String, Method>();
        writeMethods = new HashMap<String, Method>();
        types = new HashMap<String, Type>();
        names = new ArrayList<String>();
        if (bean == null)
            return;
        Class<?> beanClass = bean.getClass();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(beanClass, Introspector.USE_ALL_BEANINFO);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        if (propertyDescriptors != null) {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor != null) {
                    String name = propertyDescriptor.getName();
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    Class<?> aType = propertyDescriptor.getPropertyType();
                    if (readMethod != null) {
                        readMethods.put(name, readMethod);
                    }
                    if (writeMethod != null) {
                        writeMethods.put(name, writeMethod);
                    }
                    names.add(name);
                    if (Collection.class.isAssignableFrom(aType)) {
                        if (readMethod != null) {
                            Type gType = readMethod.getGenericReturnType();
                            types.put(name, gType);
                        }
                        if (writeMethod == null) {
                            try {
                                writeMethod = beanClass.getMethod("reset" + name.substring(0, 1).toUpperCase() + name.substring(1), aType);
                                writeMethods.put(name, writeMethod);
                            } catch (java.lang.NoSuchMethodException e1) {
                                // do nothing
                            }
                        }
                        if (readMethod == null && writeMethod != null) {
                            Type gType = writeMethod.getGenericParameterTypes()[0];
                            types.put(name, gType);
                        }
                    } else {
                        types.put(name, aType);
                    }
                }
            }
        }
    }
}