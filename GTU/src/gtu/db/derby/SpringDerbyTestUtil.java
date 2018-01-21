package gtu.db.derby;

import gtu.class_.ClassPathUtil;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class SpringDerbyTestUtil {

    /**
     * 取得bean-derby.xml 的 beanfactory
     * 
     * @return
     */
    public static BeanFactory getDerbyBeanFactory() {
        String beanDerby = ClassPathUtil.getJavaFilePath(SpringDerbyTestUtil.class) + "bean-derby.xml";
        Resource resource = new FileSystemResource(beanDerby);
        BeanFactory beanFactory = new XmlBeanFactory(resource);
        return beanFactory;
    }

    public static void main(String[] args) {
        System.out.println("done...");
    }
}
