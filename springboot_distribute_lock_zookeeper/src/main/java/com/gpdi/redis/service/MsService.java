//package com.gpdi.redis.service;
//
//import com.gpdi.exception.CongestionException;
//import com.gpdi.util.MyStringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//
//@Service
//public class MsService {
//
//    @Autowired
//    private RedisLock redisLock;
//
//    //商品详情
//    //  private static HashMap<String, Integer> product = new HashMap();
//
//    //订单表
//    private volatile static HashMap<String, String> orders = new HashMap();
//
//    //库存表
//    private volatile static HashMap<String, Integer> stock = new HashMap();
//
//    //设置默认的商品详情信息和库存信息
//    static {
//        // product.put("123", 70);
//        stock.put("123", 70);
//    }
//
//    public String select_info(String product_id) {
//        return "限量抢购商品XXX共" + ",现在成功下单" + orders.size()
//                + ",剩余库存" + stock.get(product_id) + "件";
//    }
//
//    /**
//     * 高并发有问题
//     *
//     * @param product_id
//     * @return
//     */
//
//    volatile static int count = 1;
//
//    public String order1(String product_id) {
//
////        System.out.println("第"+count+"次请求,现在成功下单" + orders.size()
////                        + ",剩余库存" + stock.get(product_id) + "件");
//        count++;
//        if (stock.get(product_id) == 0) {
//            return "活动已经结束了";
//            //已近买完了
//        } else {
//            //还没有卖完
//            try {
//                //模拟操作数据库
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            orders.put(MyStringUtils.getuuid(), product_id);
//            stock.put(product_id, stock.get(product_id) - 1);
//        }
//        System.out.println(select_info(product_id));
//        return select_info(product_id);
//    }
//
//
//    /**
//     * 高并发没问题，但是效率低下
//     *
//     * @param product_id
//     * @return
//     */
//    public synchronized String order2(String product_id) {
////        System.out.println("第"+count+"次请求,现在成功下单" + orders.size()
////                + ",剩余库存" + stock.get(product_id) + "件");
//        count++;
//        if (stock.get(product_id) == 0) {
//            return "活动已经结束了";
//            //已近买完了
//        } else {
//            //还没有卖完
//            try {
//                //模拟操作数据库
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            orders.put(MyStringUtils.getuuid(), product_id);
//            stock.put(product_id, stock.get(product_id) - 1);
//        }
//        System.out.println(select_info(product_id));
//        return select_info(product_id);
//    }
//
//    /**
//     * 高并发没问题，效率还行
//     *
//     * @param product_id 产品Id
//     * @return
//     */
//    public String order3(String product_id) throws CongestionException {
//        String value = System.currentTimeMillis() + 10000 + "";
//    //    System.out.println("当前的value值为"+value);
//        //当前线程竞争锁失败，抛出系统繁忙系统,否则系统继续运行
//        /**
//         * @desc: 如果返回True,则不进当前的方法，说明获取到锁了，那么就可以进行进一步的交易，
//         *    判断有没有交易、从而决定是否创建订单
//         *
//         * */
//        if (!redisLock.lock3(product_id, value)) {
//            //当前业务繁忙，请稍后再试
//            throw new CongestionException();
//        }
//        //已近买完了
//        if (stock.get(product_id) == 0) {
//            return "活动已经结束了";
//        } else {
//            //还没有卖完
//            try {
//                //模拟操作数据库
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            orders.put(MyStringUtils.getuuid(), product_id);
//            stock.put(product_id, stock.get(product_id) - 1);
//        }
//        /**
//         * redis解锁
//         */
//        redisLock.unlock(product_id, value);
//        System.out.println("Redis 释放锁");
//        String msg = select_info(product_id);
//        System.out.println(msg);
//        return msg;
//    }
//
//}
