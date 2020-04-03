package com.zb.thread;

public class Demo3 {
    public static void main(String[] args) throws Exception{
        StopThread stopThread = new StopThread();
        stopThread.start();
        //休眠1秒， 确保变量自增成功
        Thread.sleep(1000L);
//        stopThread.stop();
        stopThread.interrupt();
        while(stopThread.isAlive()){

        }
        stopThread.print();
    }

}
