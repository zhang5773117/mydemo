package com.zb.thread;

public class Demo2 {
    public static Thread thread1;
    public  static Demo2 obj;
    public static void main(String[] args)throws Exception {
        System.out.println("=====第一种状态切换 - 新建 ->运行 ->终止======");
        Thread thread1=new Thread(new Runnable() {
            public void run() {
                System.out.println("thread1当前状态:"+Thread.currentThread().getState().toString());
                System.out.println("thread1执行了");
            }
        });
        System.out.println("没有调用start方法，thread1当前状态："+thread1.getState().toString());
        thread1.start();
        Thread.sleep(2000L);
        System.out.println("等待2秒，再看thread1当前状态："+thread1.getState().toString());

        System.out.println();
        System.out.println("=====第二种状态切换 - 新建 ->运行 ->等待 ->运行 ->终止(sleep 方式)======");
        Thread thread2 =new Thread(new Runnable() {
            public void run() {
                try {//将现场2移动到等待状态，1500后自动唤醒
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread2当前状态："+Thread.currentThread().getState().toString());
                System.out.println("thread2执行了");
            }
        });
        System.out.println("没有调用start方法，thread2当前状态:"+thread2.getState().toString());
        thread2.start();
        System.out.println("调用start方法，thread2当前状态"+thread2.getState().toString());
        Thread.sleep(200L);
        System.out.println("等待200毫秒，再看thread2当前状态"+thread2.getState().toString());
        Thread.sleep(3000L);
        System.out.println("等待3000毫秒，再看thread2当前状态"+thread2.getState().toString());
        System.out.println();
        System.out.println("=====第三种状态切换 - 新建 ->运行 ->堵塞 ->运行 ->终止======");
        Thread thread3 =new Thread(new Runnable() {
            public void run() {
                synchronized (Demo2.class){
                    System.out.println("thread3当前状态:"+Thread.currentThread().getState().toString());
                    System.out.println("thread3执行了");
                }
            }
        });
        synchronized (Demo2.class){
            System.out.println("没有调用start方法，thread3当前状态："+thread3.getState().toString());
            thread3.start();
            System.out.println("调用start方法， thread3当前状态："+thread3.getState().toString());
            Thread.sleep(200L);
            System.out.println("等待200毫秒，在看thread3当前状态："+thread3.getState().toString());
        }
        Thread.sleep(3000);
        System.out.println("等待3000毫秒，让thread3抢到锁在看thread3当前状态："+thread3.getState().toString());

    }
}
