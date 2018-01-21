package gtu.apache;

import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

public class SerializationUtilsTest {

    static class TestBean implements Serializable {
        private static final long serialVersionUID = 1L;
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

        TestBean testBean2 = (TestBean) SerializationUtils.clone(testBean);
        System.out.println(testBean2);
        System.out.println("done...");
    }
}
