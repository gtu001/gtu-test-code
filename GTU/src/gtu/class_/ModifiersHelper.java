package gtu.class_;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ModifiersHelper {

    public static String getModifiers(final Method dataType) {
        final int modifiers = dataType.getModifiers();
        StringBuilder sb = new StringBuilder();
        if (Modifier.isPrivate(modifiers)) {
            sb.append("private ");
        }
        if (Modifier.isProtected(modifiers)) {
            sb.append("protected ");
        }
        if (Modifier.isPublic(modifiers)) {
            sb.append("public ");
        }
        if (Modifier.isAbstract(modifiers)) {
            sb.append("abstract ");
        }
        if (Modifier.isFinal(modifiers)) {
            sb.append("final ");
        }
        if (Modifier.isNative(modifiers)) {
            sb.append("native ");
        }
        if (Modifier.isInterface(modifiers)) {
            sb.append("interface ");
        }
        if (Modifier.isStatic(modifiers)) {
            sb.append("static ");
        }
        if (Modifier.isStrict(modifiers)) {
            sb.append("strict ");
        }
        if (Modifier.isSynchronized(modifiers)) {
            sb.append("synchronized ");
        }
        if (Modifier.isTransient(modifiers)) {
            sb.append("transient ");
        }
        if (Modifier.isVolatile(modifiers)) {
            sb.append("volatile ");
        }
        return sb.toString();
    }
}