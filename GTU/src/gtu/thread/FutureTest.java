package gtu.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {

    public static void main(String[] args) {
        Callable<Integer> func = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("inside callable start");
                Thread.sleep(1000);
                System.out.println("inside callable sleep 1sec ok");
                return new Integer(8);
            }
        };
        
        FutureTask<Integer> futureTask = new FutureTask<Integer>(func);
        Thread newTread = new Thread(futureTask);
        newTread.start();
        
        try{
            System.out.println("blocking here");
            Integer result = futureTask.get();
            System.out.println("get result : " + result);
        }catch(InterruptedException ingored){
        }catch(ExecutionException ingored){
        }
    }
}
