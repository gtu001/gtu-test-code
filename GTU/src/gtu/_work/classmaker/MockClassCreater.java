package gtu._work.classmaker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MockClassCreater {

    public static void main(String[] args) {
        List<String> paramList = new ArrayList<String>();
        List<String> methodList = new ArrayList<String>();
        List<String> constractList = new ArrayList<String>();
        for (Method m : Object.class.getMethods()) {
            if (m.getParameterTypes().length == 0 && m.getReturnType() != void.class) {
                String paraStr = "";
                if (m.getReturnType() == boolean.class || m.getReturnType() == Boolean.class) {
                    paraStr = m.getName().replaceFirst("is", "");
                } else {
                    paraStr = m.getName().replaceFirst("get", "");
                }
                paraStr = paraStr.substring(0,1).toLowerCase() + paraStr.substring(1);
                paramList.add("private " + m.getReturnType().getSimpleName() + " " + paraStr + ";");
                String methodStr = "";
                //lazy版本
//                methodStr = "public " + m.getReturnType().getSimpleName() + " " + m.getName() + "(){ if(this."
//                        + paraStr + " == null) { this." + paraStr + " = entity." + m.getName() + "(); } return this."
//                        + paraStr + ";}";
                //建構子版本
                methodStr = "public " + m.getReturnType().getSimpleName() + " " + m.getName() + "(){  return this."
                        + paraStr + ";}";
                methodList.add(methodStr);
                constractList.add(paraStr + " = entity." + m.getName() + "();");
            }
        }
        for (String para : paramList) {
            System.out.println(para);
        }
        for (String para : constractList) {
            System.out.println(para);
        }
        for (String para : methodList) {
            System.out.println(para);
        }
        System.out.println("done...");
    }
}
