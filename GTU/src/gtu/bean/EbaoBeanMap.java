package gtu.bean;

import java.beans.PropertyDescriptor;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.beans.BeanMap;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

public class EbaoBeanMap extends BeanMap implements Externalizable {
    private static final long serialVersionUID = 762647444248962217L;

    private Map<String, Class> props;

    public EbaoBeanMap() {
    }

    public EbaoBeanMap(Object bean) {
        this.bean = bean;
        props = new HashMap<String, Class>();
        PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        PropertyDescriptor[] pds = propertyUtilsBean.getPropertyDescriptors(bean);
        for (PropertyDescriptor pd : pds) {
            props.put(pd.getName(), pd.getPropertyType());
        }
    }

    public static BeanMap create(Object bean) {
        return new EbaoBeanMap(bean);
    }

    public Set keySet() {
        return props.keySet();
    }

    public BeanMap newInstance(Object bean) {
        return new EbaoBeanMap(bean);
    }

    public Class getPropertyType(String name) {
        return props.get(name);
    }

    public Object get(Object bean, Object key) {
        if (bean == null) {
            return null;
        }
        PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        try {
            return propertyUtilsBean.getProperty(bean, (String) key);
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
            // return null;
        }
    }

    public Object put(Object bean, Object key, Object value) {
        if (bean == null) {
            return null;
        }
        PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        try {
            Class c = propertyUtilsBean.getPropertyType(bean, (String) key);
            if (c.isEnum()) {
                value = Enum.valueOf(c, value.toString());
            }
            Object oldValue = propertyUtilsBean.getProperty(bean, (String) key);

            propertyUtilsBean.setProperty(bean, (String) key, value);
            return oldValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
            // return null;
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeExternal(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readExternal(in);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.bean);
        out.writeObject(this.props);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.bean = in.readObject();
        this.props = (Map<String, Class>) in.readObject();
    }

}
