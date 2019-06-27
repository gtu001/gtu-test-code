package gtu.bean;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.reflect.FieldUtils;

public class SimpleBeanCopyUtil {
    private static final SimpleBeanCopyUtil _INST = new SimpleBeanCopyUtil();

    public static void copyProperties(Object sourceBean, Object targetBean) {
        _INST._copyProperties(sourceBean, targetBean);
    }

    private SimpleBeanCopyUtil() {
    }

    private void _copyProperties(Object sourceBean, Object targetBean) {
        Class<?> srcClz = sourceBean.getClass();
        Class<?> tarClz = targetBean.getClass();
        Set<SameProperty> srcSet = getDescriptor(srcClz);
        Set<SameProperty> tarSet = getDescriptor(tarClz);
        srcSet.retainAll(tarSet);
        copyProperties(sourceBean, targetBean, srcSet);
    }

    private void copyProperties(Object sourceBean, Object targetBean, Set<SameProperty> srcSet) {
        for (SameProperty s : srcSet) {
            Object value = null;
            try {
                value = FieldUtils.readDeclaredField(sourceBean, s.name, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (value != null) {
                try {
                    FieldUtils.writeDeclaredField(targetBean, s.name, value, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SameProperty {
        String name;
        Class<?> propertyType;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((propertyType == null) ? 0 : propertyType.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SameProperty other = (SameProperty) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (propertyType == null) {
                if (other.propertyType != null)
                    return false;
            } else if (!propertyType.equals(other.propertyType))
                return false;
            return true;
        }

        private SimpleBeanCopyUtil getOuterType() {
            return SimpleBeanCopyUtil.this;
        }
    }

    private Set<SameProperty> getDescriptor(Class<?> clz) {
        PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        PropertyDescriptor[] pds = propertyUtilsBean.getPropertyDescriptors(clz);
        Set<SameProperty> set = new HashSet<SameProperty>();
        for (PropertyDescriptor pd : pds) {
            SameProperty s = new SameProperty();
            s.name = pd.getName();
            s.propertyType = pd.getPropertyType();
        }
        return set;
    }
}