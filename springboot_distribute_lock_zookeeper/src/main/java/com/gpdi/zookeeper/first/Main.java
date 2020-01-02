package com.gpdi.zookeeper.first;


import java.util.concurrent.CountDownLatch;

/**
 * @desc: 基于临时有序节点的分布式锁
 * 这里采用的是递归的方式，而不是监听的方式
 * Created by Administrator on 2017/7/14.
 */
public class Main {


    public final static int num = 5;
    public static CountDownLatch countDownLatch = new CountDownLatch(Main.num);

    /**
     * 目前实现是有问题的, 如果是秒杀怎么操作，每一个请求创建一个线程，
     * 把所有的线程加入到一个队列,这个队列不断更新
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= num; i++) {
            new Thread(new ZookeeperLock()).start();
        }
        countDownLatch.await();
        System.out.println("所有线程已全部执行完");
    }
}



