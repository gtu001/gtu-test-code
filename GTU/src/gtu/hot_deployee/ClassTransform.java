package gtu.hot_deployee;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class ClassTransform implements ClassFileTransformer {

    private Instrumentation inst;

    protected ClassTransform(Instrumentation inst) {
        this.inst = inst;
    }

    /**
     * 
     * 此方法在redefineClasses时或者初次加载时会被调用，也就是说在class被再次加载时会被调用，
     * 并且我们通过此方法可以动态修改class字节码，实现类似代理之类的功能，具体方法可使用ASM或者javasist，
     * 如果对字节码很熟悉的话可以直接修改字节码。
     */

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] transformed = null;
        HotAgent.clsnames.add(className);
        return null;
    }
}