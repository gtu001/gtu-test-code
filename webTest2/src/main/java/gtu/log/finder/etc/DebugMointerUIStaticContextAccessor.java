package gtu.log.finder.etc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DebugMointerUIStaticContextAccessor {

    private static DebugMointerUIStaticContextAccessor instance;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerInstance() {
        if (instance == null)
            instance = this;
    }
    
    public static ApplicationContext getContext() {
        return instance.applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return instance.applicationContext.getBean(clazz);
    }
}