package gtu.log;

public class CurrentStackUtil_MainTest {

    public static void main(String[] args) {
        //加了此行就會忽略此類
        CurrentStackUtil.StackTraceWatcher.getInstance().addClass(CurrentStackUtil_MainTest.class);
        
        System.out.println(CurrentStackUtil.getInstance().apply().currentStack());
    }

}
