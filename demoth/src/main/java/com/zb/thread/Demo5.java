package com.zb.thread;

/*线程封闭实例*/
public class Demo5 {

    /* threadLocal 变量， 没有一个线程都有一个副本，互不干涉*/
    public  static ThreadLocal<String> value = new ThreadLocal<String>();

    public void threadLocalTest() throws Exception{
        value.set("这是主线程设置的123");
        String v = value.get();
        System.out.println("线程1执行之前，主线程取到的值："+v);
        new Thread(new Runnable() {
            public void run() {
                String v = value.get();
                System.out.println("线程1取到的值："+v);
                value.set("这是线程1设置的456");

                v = value.get();
                System.out.println("重新设置之后， 线程1取到的值："+v);
                System.out.println("线程1执行结束");
            }
        }).start();

        Thread.sleep(5000);//等待所有线程执行结束

        v = value.get();
        System.out.println("线程1执行之前，主线程取到的值："+v);
    }

    public static void main(String[] args) throws Exception{
        Demo5 demo5 =new Demo5();
        demo5.threadLocalTest();
    }
}
