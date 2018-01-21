package gtu.spring.aop.classconfig;

import org.apache.commons.lang3.time.DateFormatUtils;

public class MyPrototypeBean {

    public String getDateTime() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss");
    }

}
