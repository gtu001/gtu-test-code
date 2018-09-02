package gtu.spring.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

public class PointcutTest {

    static class NormalPointcut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return MethodMatcher.TRUE;
        }
    }

    //    org.springframework.aop.support.StaticMethodMatcherPointcut 靜態切點
    //    AbstractRegexpMethodPointcut 靜態切點的實作1
    //    NameMatchMethodPointcut 靜態切點的實作2
    //    org.springframework.aop.support.DynamicMethodMatcherPointcut 動態切點
    //    DefaultPointcutAdvisor
    //    org.springframework.aop.support.annotation.AnnotationMatchingPointcut 註解切點
    //    org.springframework.aop.support.ExpressionPointcut 表達式切點 (支援AspectJ表示法)
    //    org.springframework.aop.support.ControlFlowPointcut 流程切點
    //    org.springframework.aop.support.ComposablePointcut 複合切點

    public static void main(String[] args) {
        System.out.println("done...");
    }
}
