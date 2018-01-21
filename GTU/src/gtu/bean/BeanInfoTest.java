package gtu.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanInfoTest {

    static class TestBean {
        String aaa;
        String bbb;

        public String getAaa() {
            return aaa;
        }

        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        public String getBbb() {
            return bbb;
        }

        public void setBbb(String bbb) {
            this.bbb = bbb;
        }
    }

    private static Logger log = LoggerFactory.getLogger(BeanInfoTest.class);

    /**
     * @param args
     * @throws IntrospectionException
     */
    public static void main(String[] args) throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(TestBean.class);
        PropertyDescriptor[] descritors = info.getPropertyDescriptors();
        int size = descritors.length;
        for (int index = 0; index < size; index++) {
            PropertyDescriptor prop = descritors[index];
            log.debug("\tprop.getPropertyEditorClass = " + prop.getPropertyEditorClass());
            log.debug("\tprop.getPropertyType = " + prop.getPropertyType());
            log.debug("\tprop.getReadMethod = " + prop.getReadMethod());
            log.debug("\tprop.getWriteMethod = " + prop.getWriteMethod());
            log.debug("\tprop.isBound = " + prop.isBound());
            log.debug("\tprop.isConstrained = " + prop.isConstrained());
            log.debug("\tprop.hashCode = " + prop.hashCode());
            log.debug("\tprop.getName = " + prop.getName());
            log.debug("\tprop.isHidden = " + prop.isHidden());
            log.debug("\tprop.getDisplayName = " + prop.getDisplayName());
            log.debug("\tprop.attributeNames = " + prop.attributeNames());
            log.debug("\tprop.getShortDescription = " + prop.getShortDescription());
            log.debug("\tprop.isExpert = " + prop.isExpert());
            log.debug("\tprop.isPreferred = " + prop.isPreferred());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
    }

}
