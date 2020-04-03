package com.zb.thread;

public class StopThread extends  Thread {
    private int i = 0,j=0;

    @Override
    public void run() {
        synchronized (this){
            //添加同步锁,确保线程安全
            ++i;
            //休眠10秒，模拟耗时操作
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++j;
        }
    }
    public void print(){
        System.out.println("i="+i+",j="+j);
    }
}
