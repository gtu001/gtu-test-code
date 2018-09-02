package gtu.apache;
import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;

public class PropertyUtilsTest {

    public static class TestBean {
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

        @Override
        public String toString() {
            return "TestBean [aaa=" + aaa + ", bbb=" + bbb + "]";
        }
    }

    public static void main(String[] args) throws Exception {
        TestBean testBean = new TestBean();
        testBean.setAaa("A");
        testBean.setBbb("b");
        PropertyDescriptor[] pdescs = PropertyUtils.getPropertyDescriptors(TestBean.class);
        for (PropertyDescriptor pdesc : pdescs) {
            System.out.println("getName = " + pdesc.getName());
            System.out.println("getProperty = " + PropertyUtils.getProperty(testBean, pdesc.getName()));
        }

        TestBean testBean2 = new TestBean();
        PropertyUtils.copyProperties(testBean2, testBean);
        System.out.println("testBean2 = " + testBean2);
        System.out.println("done...");
    }
}
