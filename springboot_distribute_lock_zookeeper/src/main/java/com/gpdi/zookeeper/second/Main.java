package com.gpdi.zookeeper.second;



public class Main {
    public static void main(String[] args) throws InterruptedException {
        CustomizedZKLock zkLock = new CustomizedZKLock("172.16.104.1:2181");
        zkLock.lock();
        System.out.println("获得锁");
        Thread.sleep(10000);
        zkLock.unlock();
        System.out.println("释放锁");
    }
}
