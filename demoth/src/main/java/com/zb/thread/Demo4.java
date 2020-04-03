package com.zb.thread;

import java.util.concurrent.locks.LockSupport;

public class Demo4 {
    public static Object baozhidian;
    public Object obj = new Object();

    public void suspendResumeTest() throws Exception {
        Thread consumerThread = new Thread(new Runnable() {
            public void run() {
                if (baozhidian == null) {
                    System.out.println("1.没包子，进入等待");
                    Thread.currentThread().suspend();
                }
                System.out.println("2.买到包子，回家");
            }
        });
        consumerThread.start();
        Thread.sleep(3000);
        baozhidian = new Object();
        consumerThread.resume();
        System.out.println("3.通知消费者");
    }

    public void suspendResumeDeadLockTest() throws Exception {
        System.out.println("死锁方法");
        Thread consumerThread = new Thread(new Runnable() {
            public void run() {
                if (baozhidian == null) {
                    System.out.println("1.没包子，进入等待");
                    //当前线程拿到锁， 然后挂起
                    synchronized (obj) {
                        System.out.println("线程内锁住");
                        Thread.currentThread().suspend();
                        System.out.println("线程内出锁");
                    }
                }
                System.out.println("2.买到包子，回家");
            }
        });
        consumerThread.start();
        Thread.sleep(3000);
        baozhidian = new Object();
        //争取到锁以后，在恢复consumerThread
        synchronized (obj) {
            System.out.println("线程外锁住");
            consumerThread.resume();
            System.out.println("线程外出锁");
        }
        System.out.println("3.通知消费者");
    }

    public void suspendResumeDeadLockTest2() throws Exception {
        System.out.println("死锁方法");
        Thread consumerThread = new Thread(new Runnable() {
            public void run() {
                if (baozhidian == null) {
                    System.out.println("1.没包子，进入等待");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //当前线程拿到锁， 然后挂起
                    Thread.currentThread().suspend();
                }
                System.out.println("2.买到包子，回家");
            }
        });
        consumerThread.start();
        Thread.sleep(3000);
        baozhidian = new Object();
        //争取到锁以后，在恢复consumerThread
        consumerThread.resume();
        System.out.println("3.通知消费者");
    }

    public void watNotfiyTest() throws Exception {
        System.out.println("watNotfiy方法");
        Thread consumerThread = new Thread(new Runnable() {
            public void run() {
                if (baozhidian == null) {
                    System.out.println("1.没包子，进入等待");
                    //当前线程拿到锁， 然后挂起
                    synchronized (obj) {
                        System.out.println("线程内锁住");
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("线程内出锁");
                    }
                }
                System.out.println("2.买到包子，回家");
            }
        });
        consumerThread.start();
        Thread.sleep(3000);
        baozhidian = new Object();
        //争取到锁以后，在恢复consumerThread
        synchronized (obj) {
            System.out.println("线程外锁住");
            obj.notifyAll();
            System.out.println("线程外出锁");
        }
        System.out.println("3.通知消费者");
    }


    public void watNotfiyDeadTest() throws Exception {
        System.out.println("watNotfiy死锁方法");
        Thread consumerThread = new Thread(new Runnable() {
            public void run() {
                if (baozhidian == null) {
                    System.out.println("1.没包子，进入等待");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //当前线程拿到锁， 然后挂起
                    synchronized (obj) {
                        System.out.println("线程内锁住");
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("线程内出锁");
                    }
                }
                System.out.println("2.买到包子，回家");
            }
        });
        consumerThread.start();
        Thread.sleep(3000);
        baozhidian = new Object();
        //争取到锁以后，在恢复consumerThread
        synchronized (obj) {
            System.out.println("线程外锁住");
            obj.notifyAll();
            System.out.println("线程外出锁");
        }
        System.out.println("3.通知消费者");
    }


    public void parkUnparkTest() throws Exception {
        Thread consumerThread = new Thread(new Runnable() {
            public void run() {
                if (baozhidian == null) {
                    System.out.println("1.没包子，进入等待");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LockSupport.park();
                }
                System.out.println("2.买到包子，回家");
            }
        });
        consumerThread.start();
        Thread.sleep(3000);
        baozhidian = new Object();
        LockSupport.unpark(consumerThread);
        System.out.println("3.通知消费者");
    }


    public void parkUnparkDeadTest() throws Exception {
        Thread consumerThread = new Thread(new Runnable() {
            public void run() {
                if (baozhidian == null) {
                    System.out.println("1.没包子，进入等待");
                    synchronized (obj) {
                        LockSupport.park();
                    }
                }
                System.out.println("2.买到包子，回家");
            }
        });
        consumerThread.start();
        Thread.sleep(3000);
        baozhidian = new Object();
        synchronized (obj) {
            LockSupport.unpark(consumerThread);
        }
        System.out.println("3.通知消费者");
    }
    public static void main(String[] args) throws Exception {
        Demo4 demo4 = new Demo4();
        demo4.parkUnparkDeadTest();
//        demo4.parkUnparkDeadTest();
    }

}
