package com.gpdi.zookeeper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @desc： 用zookeeper实现分布式锁
 *
 *   一、实现分布式锁的原理：
 *        1.1、利用zookeeper树形结点的特性， 一个结点只能下面唯一的临时结点，唯一性。
 *        1.2 、唯一结点的有序性，顺序为 从小到大。
 *        1.3、watch监控机制，当结点删除的时候会发送了一个通知
 *
 *   二、业务逻辑实现：
 *        2.1、背景说明：假设50个系统连接一个Zookeeper(当然Zookeeper可以进行集群），实现一个秒杀功能，
 *    抢20个商品， 每个系统一个线程，线程连接Zk并对Zookeeper进行操作，每一个线程都在里面创建一个结点,
 *    每次去判断有没有拿到锁（判断当前是不是最小的），没有拿到锁的话进行等待（直到有其他的线程释放锁，
 *    通知到重新进行锁分配），拿到锁的就判断有没有库存，有库存就进行下单，直到20个商品
 *    被秒杀。
 *
 */
@SpringBootApplication
public class DistributeLockApplication {

	public static void main(String[] args) throws  Exception{
		SpringApplication.run(DistributeLockApplication.class, args);
	}

}
