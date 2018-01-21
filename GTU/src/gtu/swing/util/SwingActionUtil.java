package gtu.swing.util;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.commons.lang.StringUtils;

public class SwingActionUtil {

    private final Class<?> registerClass;
    private final Object registerObject;
    private final Map<String, Process> registerActionMap = new HashMap<String, Process>();

    private final Process undifinedAction = new Process() {
        public void action(EventObject evt) throws Exception {
            System.err.println("!!未定義 action : " + actionName + ", from : " + getRemark(evt) + "[" + evt.getSource().getClass().getSimpleName() + "]");
            boolean findSelf = false;
            String selfName = SwingActionUtil.class.getSimpleName();
            for(StackTraceElement stack : Thread.currentThread().getStackTrace()){
                if(!findSelf && stack.toString().contains(selfName)){
                    findSelf = true;
                }
                if(findSelf && !stack.toString().contains(selfName)){
                    System.err.println(stack);
                    break;
                }
            }
        }
    };

    private SwingActionUtil(Object object) {
        this.registerClass = object.getClass();
        this.registerObject = object;
    }

    public static SwingActionUtil newInstance(Object object) {
        return new SwingActionUtil(object);
    }

    public interface Action {
        void action(EventObject evt) throws Exception;
    }

    public SwingActionUtil addAction(String actionName, final Action action) {
        if (registerActionMap.containsKey(actionName)) {
            throw new RuntimeException(actionName + "已存在!!");
        }
        System.out.println("註冊 action : " + actionName);
        registerActionMap.put(actionName, new Process() {
            public void action(EventObject evt) throws Exception {
                action.action(evt);
            }
        });
        return this;
    }

    public SwingActionUtil invokeAction(final String actionName, EventObject evt) {
        if (registerActionMap.containsKey(actionName)) {
            registerActionMap.get(actionName).execute(evt);
        } else {
            undifinedAction.actionName = actionName;
            undifinedAction.execute(evt);
        }
        return this;
    }

    public SwingActionUtil  addAction(JComponent component, Class<?> eventClass, final Action action) {
        String actionName = getActionName(component, eventClass);
        return addAction(actionName, action);
    }
    
    String getActionName(Object component, Class<?> eventClass){
        return component.getClass().getSimpleName() + "_" + component.hashCode() + "_" + eventClass.getSimpleName();
    }

    public SwingActionUtil invokeAction(EventObject evt) {
        String actionName = getActionName(evt.getSource(), evt.getClass());
        return invokeAction(actionName, evt);
    }

    abstract class Process implements Action {
        protected String actionName;

        void execute(EventObject evt) {
            System.out.format(remarkFormat, getRemark(evt), evt.getSource().getClass().getSimpleName(), evt.getClass().getSimpleName(), "start");
            try {
                action(evt);
            } catch (Throwable ex) {
                JCommonUtil.handleException(actionName, ex);
                ex.printStackTrace();
            }
            System.out.format(remarkFormat, getRemark(evt), evt.getSource().getClass().getSimpleName(), evt.getClass().getSimpleName(), "end");
        }

        final String remarkFormat = "# %s[%s]\t%s ...%s\n";
    }

    String getRemark(EventObject evt) {
        Object object = evt.getSource();
        String remark = null;
        try {
            remark = (String) object.getClass().getMethod("getText", new Class[0]).invoke(object, new Object[0]);
            if (StringUtils.isNotBlank(remark)) {
                return remark;
            }
        } catch (Exception e) {
        }
        try {
            remark = (String) object.getClass().getMethod("getName", new Class[0]).invoke(object, new Object[0]);
            if (StringUtils.isNotBlank(remark)) {
                return remark;
            }
        } catch (Exception e) {
        }
        if (StringUtils.isNotBlank(remark)) {
            return remark;
        }
        return "unknow";
    }
}
