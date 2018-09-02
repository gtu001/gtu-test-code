package gtu.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 并行计算数组的和
 * 
 * ExecutoreService提供了submit()方法，传递一个Callable，或Runnable，
 * 返回Future。如果Executor后台线程池还没有完成Callable的计算，
 * 这调用返回Future对象的get()方法，会阻塞直到计算完成。
 * 
 * 在刚在的例子中，getResult()方法的实现过程中，迭代了FutureTask的数组，
 * 如果任务还没有完成则当前线程会阻塞，如果我们希望任意字任务完成后就把其结果加到result中，
 * 而不用依次等待每个任务完成，可以使CompletionService。生产者submit()执行的任务。
 * 使用者take()已完成的任务，并按照完成这些任务的顺序处理它们的结果 。
 * 也就是调用CompletionService的take方法是，会返回按完成顺序放回任务的结果，
 * CompletionService内部维护了一个阻塞队列BlockingQueue，如果没有任务完成，
 * take()方法也会阻塞。修改刚才的例子使用CompletionService：
 */
public class ConcurrentCalculator {
    
    ExecutorService exec;
    int cpuCoreNumber;
    List<Future<Long>> tasks = new ArrayList<Future<Long>>();
    
    CompletionService<Long> completionService;
    
    static class SumCalculator implements Callable<Long>{
        int[] numbers;
        int start;
        int end;
        SumCalculator(final int[] numbers, int start, int end){
            this.numbers = numbers;
            this.start = start;
            this.end = end;
            System.out.println("numbers = " + Arrays.toString(numbers) + ", start = " + start + ", end = " + end);
        }
        public Long call() {
            Long sum = 0L;
            for(int i = start ; i < end ; i++){
                sum += numbers[i];
            }
            return sum;
        }
    }
    
    ConcurrentCalculator(){
        cpuCoreNumber = Runtime.getRuntime().availableProcessors();
        exec = Executors.newFixedThreadPool(cpuCoreNumber);
        
        completionService = new ExecutorCompletionService<Long>(exec); 
    }
    
    Long sum(final int[] numbers){
        //根據CPU核心個數拆分任務，創建FutureTask並提交道Executor
        for(int i = 0 ; i < cpuCoreNumber ; i ++){
            int increment = numbers.length / cpuCoreNumber + 1;
            int start = increment * i;
            int end = increment * i + increment;
            if(end > numbers.length){
                end = numbers.length;
            }
            SumCalculator subCalc = new SumCalculator(numbers, start, end);
            FutureTask<Long> task = new FutureTask<Long>(subCalc);
            tasks.add(task);
            if(!exec.isShutdown()){
//                exec.submit(task);//(1)
                completionService.submit(subCalc);//(2)
            }
        }
        return getResult();
    }
    
    /**
     * 迭代每個任務，獲得部分和，相加返回
     */
    Long getResult(){
        Long result = 0L;
        for(Future<Long> task : tasks){
            try{
                //如果計算未完成則阻塞
//                Long subSum = task.get();//(1)
                Long subSum = completionService.take().get();//(2)
                result += subSum;
            }catch(InterruptedException e){
                e.printStackTrace();
            }catch(ExecutionException e){
                e.printStackTrace();
            }
        }
        return result;
    }
    
    void close(){
        exec.shutdown();
    }
    
    public static void main(String[] args){
        int[] numbers = new int[]{1,2,3,4,5,6,7,8,9,10,11};
        ConcurrentCalculator calc = new ConcurrentCalculator();
        Long sum = calc.sum(numbers);
        System.out.println(sum);
        calc.close();
    }
}
