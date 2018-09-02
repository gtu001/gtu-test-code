package gtu.thread;

import java.util.Arrays;


//import org.junit.Test;

public class ThreadTest {
    
    public static void main(String[] args){
        int[] posArray = new int[]{1,3,6,3,4,2,5};
        int[] negArray = new int[]{-2,-8,-3,-9,-10};
        
        Thread t1 = new BubbleSortThread(posArray);
        t1.start();
        System.out.println("Testing with postive numbers...");
        try{
            t1.join();
            System.out.println(Arrays.toString(posArray));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        Thread t2 = new BubbleSortThread(negArray);
        t2.start();
        System.out.println("Testing with negative numbers...");
        try{
            t2.join();
            System.out.println(Arrays.toString(negArray));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    

    private static class BubbleSortThread extends Thread {
        private int[] numbers;
        BubbleSortThread(int[] numbers){
            setName("Simple Thread");
            //1.5新增例外處理 TODO
//            setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
//                @Override
//                public void uncaughtException(Thread t, Throwable e) {
//                    System.err.format("%s: %s at line %d of %s%n",//
//                            t.getName(),//
//                            e.toString(),//
//                            e.getStackTrace()[0].getLineNumber(),//
//                            e.getStackTrace()[0].getFileName()//
//                            );
//                }
//            });
//            setDefaultUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler());
            this.numbers = numbers;
        }
        
        @Override
        public void run(){
            int index = numbers.length;
            boolean finished = false;
            while(!finished){
                index --;
                finished = true;
                for(int ii = 0 ; ii < index; ii ++){
                    //create error condition
                    if(numbers[ii + 1] < 0){
                        throw new IllegalArgumentException("Cannot pass negative numbers into this thread!");
                    }
                    if(numbers[ii] > numbers[ii + 1]){
                        //swap
                        int temp = numbers[ii + 1];
                        numbers[ii] = numbers[ii + 1];
                        numbers[ii + 1] = temp;
                        finished = false;
                    }
                }
            }
        }
    }
}
