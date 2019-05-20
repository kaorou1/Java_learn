package com.kaorou.demo;

class MyData{

    volatile int number = 0;

    public void addNumTo(){
        this.number = 50;
    }

    //volatile不保证原子性
    public synchronized void addNum(){
        this.number++;
    }
}

public class TestVolatile {


    //volatile不保证原子性
    public static void main(String[] args) {
        //testVolatileSee();

        MyData myData = new MyData();

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myData.addNum();
                }

            }, i+"" ).start();
        }

        //20个线程结束后，再取最终结果
        while (Thread.activeCount() >2 ){
            Thread.yield(); //礼让，去执行其他的线程
        }


        System.out.println(Thread.currentThread().getName() + "最终的结果是"+myData.number);

    }

    //volatile可以保证可见性
    public static void testVolatileSee(){
        //共享资源
        MyData data = new MyData();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "开始执行");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            data.addNumTo();
            System.out.println(Thread.currentThread().getName() + "结束执行 number值为"+data.number);

        }, "线程1").start();


        while (data.number == 0) {
            //主线程，如果不能读取到最新的变量值，将会一直死循环
        }

        System.out.println(Thread.currentThread().getName() + "结束执行 number值为"+data.number);
    }
}
