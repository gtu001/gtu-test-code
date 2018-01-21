package gtu.spring.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

public class _ControllableTest {

    interface Mointorable {
        void setMonitorAtive(boolean active);
    }

    static class ControllablePerformanceMonitor extends DelegatingIntroductionInterceptor implements Mointorable {
        private static final long serialVersionUID = 453616994913209962L;

        private ThreadLocal<Boolean> monitorStatusMap = new ThreadLocal<Boolean>();

        @Override
        public Object invoke(MethodInvocation mi) throws Throwable {
            Object obj = null;
            if (monitorStatusMap.get() != null && monitorStatusMap.get()) {
                System.out.println("monitor begin!");
                obj = super.invoke(mi);
                System.out.println("monitor end!");
            } else {
                obj = super.invoke(mi);
            }
            return obj;
        }

        @Override
        protected Object doProceed(MethodInvocation mi) throws Throwable {
            return super.doProceed(mi);
        }

        @Override
        public void setMonitorAtive(boolean active) {
            monitorStatusMap.set(active);
        }
    }

    static class ForumService {
        public void removeForum(int index) {
            System.out.println("remove forum : " + index);
        }

        public void removeTopic(int index) {
            System.out.println("remove topic : " + index);
        }
    }

    public static void main(String[] args) throws Exception {
        ControllablePerformanceMonitor pmonitor = new ControllablePerformanceMonitor();
        ForumService service = new ForumService();
        ProxyFactoryBean pf = new ProxyFactoryBean();
        //引介增強要通過創毽子類來生成代理
        pf.setProxyTargetClass(true);
        pf.setInterfaces(new Class[] { Mointorable.class });
        pf.addAdvice(pmonitor);
        pf.setTarget(service);

        ForumService service2 = (ForumService) pf.getObject();
        service2.removeForum(10);
        service2.removeTopic(10);
        System.out.println("---------------");
        Mointorable mointor = (Mointorable) service2;
        mointor.setMonitorAtive(true);
        service2.removeForum(10);
        service2.removeTopic(10);
    }
}
