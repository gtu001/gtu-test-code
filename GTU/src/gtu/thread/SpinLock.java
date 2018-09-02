package gtu.thread;

import java.util.concurrent.TimeUnit;

public class SpinLock {

    private static class Robot extends Thread {
        private static byte[] commands;
        private RobotController controller;
        Robot(RobotController c){
            this.controller = c;
        }
        public static void storeCommands(byte[] b){
            commands = b;
        }
        public static void processCommand(byte b){
            //Move the robot based on the command.
            System.out.println(Thread.currentThread().getName() + " : robot move : " + b);
        }
        @Override
        public void run() {
            byte[] cmds;
            while(true){
                synchronized(controller){
//                    if(commands == null){
//                        //此處使用此判斷當執行續被喚醒,第一個可以正常執行..但第二個執行的時候commands會出現NullPointer
//                        //所以此處應該改用旋鎖Spin Lock
//                        try {
//                            controller.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    while (commands == null) {
                        //這就是旋鎖,被喚醒thread必須重新檢查commands條件是否符合..以確保執行安全
                        try {
                            System.out.println(Thread.currentThread().getName() + "--wait");
                            controller.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    cmds = new byte[commands.length];
                    for(int ii = 0 ; ii < commands.length ; ii ++){
                        cmds[ii] = commands[ii];
                    }
                    commands = null;
                }
                //Now we have commands for the robot.
                int size = cmds.length;
                for(int ii = 0 ; ii < size ; ii ++){
                    processCommand(cmds[ii]);
                }
            }
        }
    }

    private static class RobotController extends Thread {
        private Robot robot1;
        private Robot robot2;
        
        @Override
        public void run() {
            robot1 = new Robot(this);
            robot1.start();
            robot2 = new Robot(this);
            robot2.start();
            sleep2Sec();
            loadCommands(new byte[]{1,2});
            sleep2Sec();
            loadCommands(new byte[]{3,4});
            sleep2Sec();
            loadCommands(new byte[]{5,6});
            sleep2Sec();
            loadCommands(new byte[]{7,8});
        }
        
        private void sleep2Sec(){
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        public synchronized void loadCommands(byte[] b){
            Robot.storeCommands(b);//Give the commands to the Robot, Notify all threads.
//            notifyAll();
            notify();
        }
    }
    
    public static void main(String[] args){
        RobotController rc = new RobotController();
        rc.start();
    }
}
