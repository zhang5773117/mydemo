package com.zb.pool;

//-server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*VolatileVisibilTest.preparedData
public class VolatileVisibilTest {
    private volatile static boolean initFlag = false;

    public static void main(String[] args) throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("waiting data....");
                while (!initFlag){

                }
                System.out.println("================success");
            }
        }).start();
        Thread.sleep(50);
        new Thread(new Runnable() {
            @Override
            public void run() {
                preparedData();
            }
        }).start();
    }

    public  static void preparedData(){
        System.out.println("prepareing data....");
        initFlag =true;
        System.out.println("prepareing data end....");
    }
}
