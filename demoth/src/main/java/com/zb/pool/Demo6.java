package com.zb.pool;

import java.util.List;
import java.util.concurrent.*;

public class Demo6 {
    /**
     * 测试：提交15个执行时间， 需要3秒的任务，看线程池的状态
     * @param threadPoolExecutor 传入不同的线程池，看不同的结果
     * @throws Exception
     */
    public void testCommon(ThreadPoolExecutor threadPoolExecutor) throws Exception{
        for (int i = 0; i < 15; i++) {
            final int n = i ;
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("开始执行："+n);
                        Thread.sleep(3000L);
                        System.out.println("执行结束："+n);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("任务提交成功："+i);
        }
        //查看线程数量，查看队列等待数量
        Thread.sleep(500);
        System.out.println("当前线程池线程数量为："+threadPoolExecutor.getPoolSize());
        System.out.println("当前线程池等待的数量为："+threadPoolExecutor.getQueue().size());
        //等待15秒，查看线程数量和对象（理论上， 会别超出核心线程数量的线程自动销毁）
        Thread.sleep(15000);
        System.out.println("当前线程池线程数量为："+threadPoolExecutor.getPoolSize());
        System.out.println("当前线程池等待的数量为："+threadPoolExecutor.getQueue().size());
    }
    /*
        1.线程池信息：核心线程数量5， 最大数量10 ，无界队列，
        超出核心线程数量的线程存活时间：5秒。
        指定拒绝策略
     */
    private void threaadPoolExecutorTest1()throws Exception{
        ThreadPoolExecutor threadPoolExecutor =new ThreadPoolExecutor(5,10,5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
        testCommon(threadPoolExecutor);
        //预计结果， 线程池线程数量为5：超出数量的任务， 其他的进入队列中等待被执行
    }

    /*
        2.线程池信息：核心线程数量5，最大数量10，队列大小3，超出核心线程数量的线程存活时间：5秒， 指定拒绝策略
     */
    private void threadPoolExecutorTest2()throws Exception{
        //创建一个 核心线程数量为5， 最大数量为10, 等待队列最大是3的线程池， 也就是最大容纳13个任务
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(3), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("有任务被拒绝执行了");
            }
        });
        testCommon(threadPoolExecutor);
        //预计结果：
        // 1. 5个任务直接分配线程开始执行
        // 2. 3个任务进入等待队列
        // 3. 队列不够用， 临时开加5个线程来执行任务（5秒没活跟被销毁）
        // 4. 队列和线程池都满了， 剩下2个任务，没资源了， 被拒接执行
        // 5. 任务执行，5秒后， 如果无任务可执行， 销毁临时创建的5个线程
    }

    /*
     1.线程池信息：核心线程数量5， 最大数量5 ，无界队列，超出核心线程数量的线程存活时间：0秒。
     */
    private void threadPoolExecutoreTest3()throws Exception{
      /*  ThreadPoolExecutor threadPoolExecutor =new ThreadPoolExecutor(5,5,0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
        testCommon(threadPoolExecutor);*/
       /*
       底层：返回ThreadPoolExecutor实例，接收参数为所设定线程数量nThread，corePoolSize为nThread，maximumPoolSize为nThread；keepAliveTime为0L(不限时)；unit为：TimeUnit.MILLISECONDS；WorkQueue为：new LinkedBlockingQueue<Runnable>() 无界阻塞队列
    通俗：创建可容纳固定数量线程的池子，每隔线程的存活时间是无限的，当池子满了就不在添加线程了；如果池中的所有线程均在繁忙状态，对于新任务会进入阻塞队列中(无界的阻塞队列)
    适用：执行长期的任务，性能好很多
     * 1.创建固定大小的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小<br>
     * 2.线程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充一个新线程<br>
     * 3.因为线程池大小为3，每个任务输出index后sleep 2秒，所以每两秒打印3个数字，和线程名称<br>
*/
       ExecutorService fixedPool = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 10; i++) {
            final int ii = i;
            try {
                Thread.sleep(ii * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fixedPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行" + ii);
                }
            });
        }
    }

    /**
     * 线程池信息
     * 核心线程信息0 ， 最大数量Max_value ,SynchronouseQueue队列， 超出核心线程数量的线程存活时间：60秒
     *
     * @throws Exception
     */
    public  void threadPoolExecutoreTest4()throws Exception{
        /*
            SynchronousQueue，实际上它不是一个真正的队列，因为他不会为队列中元素维护存储空间， 与其他队列不同的是， 他维护一组线程， 这些线程在等待着把元素加入或移出队列
            在使用SynchronousQueue作为工作队列的前提下，客户端代码向线程池提交任务时，而线程池中又没有空闲的线程能够从SynchronousQueue队列实例中取一个任务
            那么相应的offer方法调用就会失败， （任务没有被存储工作队列中）
            此时，ThreadPoolExecutor会新建一个新的工作者线程用于对这个入队列失败的任务执行处理
 */
       /* ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0,
                Integer.MAX_VALUE,60L,TimeUnit.SECONDS ,new SynchronousQueue<Runnable>());
        testCommon(threadPoolExecutor);

        //预计结果，
        //1.线程池线程数量为：15， 超出数量的任务，其他的进入队列中等待执行，
        //2.所有的任务执行结束，60秒后， 如果无任务可执行， 所有线程全部被销毁池的大小恢复为0
        Thread.sleep(60000);
        System.out.println("60秒后， 在看线程池中的数量,"+threadPoolExecutor.getPoolSize());*/

        /**
         * newCachedThreadPool：
         * 底层：返回ThreadPoolExecutor实例，corePoolSize为0；maximumPoolSize为Integer.MAX_VALUE；keepAliveTime为60L；unit为TimeUnit.SECONDS；workQueue为SynchronousQueue(同步队列)
         * 通俗：当有新任务到来，则插入到SynchronousQueue中，由于SynchronousQueue是同步队列，因此会在池中寻找可用线程来执行，若有可以线程则执行，若没有可用线程则创建一个线程来执行该任务；若池中线程空闲时间超过指定大小，则该线程会被销毁。
         * 适用：执行很多短期异步的小程序或者负载较轻的服务器
         * 1.创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，那么就会回收部分空闲（60秒不执行任务）的线程<br>
         * 2.当任务数增加时，此线程池又可以智能的添加新线程来处理任务<br>
         * 3.此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程大小<br>
         */
        ExecutorService fixedPool = Executors.newCachedThreadPool();
        for (int i = 1; i <= 10; i++) {
            final int ii = i;
            try {
                Thread.sleep(ii * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fixedPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行" + ii);
                }
            });
        }
    }

    /*
        5 定时执行线程池信息： 3秒后执行， 一次性 任务， 到点就执行
     */
    public  void threadPoolExecutoreTest5 ()throws Exception{
    /*    ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(5);
        poolExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("任务被执行，现在时间："+System.currentTimeMillis());
            }
        },3000,TimeUnit.MILLISECONDS);

        System.out.println("定时任务，提交成功， 时间是："+System.currentTimeMillis()+",当前线程池中线程数量："+poolExecutor.getPoolSize());
        *///预计结果， 任务在3秒之后被执行一次

        /*
        NewScheduledThreadPool:
        底层：创建ScheduledThreadPoolExecutor实例，corePoolSize为传递来的参数，maximumPoolSize为Integer.MAX_VALUE；keepAliveTime为0；unit为：TimeUnit.NANOSECONDS；workQueue为：new DelayedWorkQueue() 一个按超时时间升序排序的队列
        通俗：创建一个固定大小的线程池，线程池内线程存活时间无限制，线程池可以支持定时及周期性任务执行，如果所有线程均处于繁忙状态，对于新任务会进入DelayedWorkQueue队列中，这是一种按照超时时间排序的队列结构
        适用：周期性执行任务的场景
*/
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行:延迟3秒后一次");
        }
        }, 3, TimeUnit.SECONDS);
        for (int i = 0; i < 5; i++) {
            scheduledThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行:普通任务");
                }
            });
        }

    }
    /*
        定时执行线程信息， 线程固定数量5
     */
    public void threadPoolExecutoreTest6() throws  Exception{
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(5);
        //周期性 执行某一个任务， 线程池提供了2种调度方式， 这里单独演示一下， 测试场景一样.
        //测试场景 提交的任务需要3秒， 才能执行完毕， 看2种不同的调度方式的区别
        //效果1： 提交后， 2秒后开始第一次执行， 之后每隔1秒， 固定执行一次（如果发现上传执行还未完毕， 则等待完毕， 完毕后立即执行）.
        //也就是说这个代码中是， 3秒执行一次（计算方式： 没次执行三秒， 间隔时间1秒， 执行结束后马上开始下一次执行， 无需等待）
        threadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务-1 被执行， 现在时间："+System.currentTimeMillis());
            }
        },2000,1000,TimeUnit.MILLISECONDS);

        //效果1： 提交后， 2秒后开始第一次执行， 之后每隔1秒， 固定执行一次（如果发现上传执行还未完毕， 则等待完毕，等上一次执行完毕后再开始计时， 等待1秒）.
        //也就是说这个代码中是， 3秒执行一次（计算方式： 没次执行三秒， 间隔时间1秒， 执行结束后在等待1秒， 所以是3+1）
        threadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务-2 被执行， 现在时间："+System.currentTimeMillis());
            }
        },2000,1000,TimeUnit.MILLISECONDS);
    }

    /*
        终止线程: .线程池信息：核心线程数量5，最大数量10，队列大小3，超出核心线程数量的线程存活时间：5秒， 指定拒绝策略
     */
    public void threadPoolExecutoreTest7() throws  Exception{
        //创建一个 核心线程数量为5， 最大数量为10, 等待队列最大是3的线程池， 也就是最大容纳13个任务
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5,
                TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(3), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("有任务被拒绝执行了");
            }
        });
        //测试：提交15 个线程时间需要3秒的任务， 看超过大小前2个， 响应的处理情况
        for (int i = 0; i < 15; i++) {
            final int n = i;
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("开始执行：" + n);
                        Thread.sleep(3000L);
                        System.out.println("执行结束：" + n);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("异常:"+e.getMessage());
                    }
                }
            });
            System.out.println("任务提交成功:"+i);
        }
        //1秒后终止线程池
        Thread.sleep(1000);
        threadPoolExecutor.shutdown();
        //再次提交显示失败
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("追加一个任务");
            }
        });
        //分析结果
        //1. 10个被任务执行， 3个任务进入等待队列,2个任务被拒绝执行
        //2.调用shutdown后， 不接受新的任务， 等待13任务执行完成结束
        //3. 追加的任务在线程关闭够， 无法在提交， 会被拒绝执行
    }

    /*
       终止线程: .线程池信息：核心线程数量5，最大数量10，队列大小3，超出核心线程数量的线程存活时间：5秒， 指定拒绝策略
    */
    public void threadPoolExecutoreTest8() throws  Exception{
        //创建一个 核心线程数量为5， 最大数量为10, 等待队列最大是3的线程池， 也就是最大容纳13个任务
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(3), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("有任务被拒绝执行了");
            }
        });
        //测试：提交15 个线程时间需要3秒的任务， 看超过大小前2个， 响应的处理情况
        for (int i = 0; i < 15; i++) {
            final int n = i;
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("开始执行：" + n);
                        Thread.sleep(3000L);
                        System.out.println("执行结束：" + n);
                    } catch (InterruptedException e) {
                        System.out.println("异常:"+e.getMessage());
                    }
                }
            });
            System.out.println("任务提交成功:"+i);
        }
        //1秒后终止线程池
        Thread.sleep(1000);
        List<Runnable> runnables = threadPoolExecutor.shutdownNow();
        //再次提交显示失败
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("追加一个任务");
            }
        });
        System.out.println("为结束的任务有:"+runnables.size());
        //分析结果
        //1. 10个任务呗执行， 3个任务进入等待队列,2个任务呗拒绝执行
        //2.调用shutdown后， 不接受新的任务， 等待13任务执行完成结束
        //3. 追加的任务在线程关闭够， 无法在提交， 会被拒绝执行
    }

    public static void main(String[] args) throws Exception{
        Demo6 demo6 =new Demo6();
        demo6.threadPoolExecutoreTest8();
    }
}
