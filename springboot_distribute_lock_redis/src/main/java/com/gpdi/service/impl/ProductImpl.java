package com.gpdi.service.impl;

import java.util.*;
import com.gpdi.entity.Order;
import com.gpdi.entity.Product;
import com.gpdi.exception.CongestionException;
import com.gpdi.lock.RedisLock;
import com.gpdi.mapper.OrderMapper;
import com.gpdi.response.R;
import org.springframework.stereotype.Service;
import com.gpdi.mapper.ProductMapper;
import com.gpdi.service.ProductService;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

@Service
public class ProductImpl implements ProductService {


    @Override
    public R addOrder(String productId) throws CongestionException {

        //初始化Redis数据
        if (redisTemplate.opsForValue().get((productId + "key"))==null) {
            productMapper.getAllProduct().forEach((a) -> {
                System.out.println("商品id"+a.getId()+"  商品名称"+a.getName());
                redisTemplate.opsForValue().set((a.getId() + "key"), a.getNumber() + "");
            });
        };

        RedisLock redisLock = new RedisLock(redisTemplate);

        //对库存进行操作
        String value = System.currentTimeMillis() + 10000 + "";
        if (!redisLock.requireLock(productId, value)) {
            System.out.println("业务繁忙！！！");
            return new R(100,"请求失败");
          //  throw new CongestionException();
        } else {
            System.out.println(Thread.currentThread().getName()+"获取到锁");
            String redisProductKey = (productId + "key");
            int redisProductValue = Integer.parseInt(redisTemplate.opsForValue().get((productId + "key")));
            if (redisProductValue > 0) {

                //在Redis中减少库存
                redisTemplate.opsForValue().set(redisProductKey, (redisProductValue - 1) + "");

                //数据库中减少库存
                Product product = productMapper.getProductNumberById(productId);
                product.setNumber(product.getNumber() - 1);
                productMapper.updateProduct(product);

                //创建订单存入数据库
                Order order = new Order();
                order.setProductId(productId);
                order.setStatus(1);
                order.setUserId(1);
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                order.setOrderTime(s.format(date));
                orderMapper.addOrders(order);

            } else {
                return new R(200, "当前已无库存");
            }
            //释放锁
            redisLock.unlock(productId, value);

            return new R(200, "你已经成功下单");
        }

    }


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;


    @Autowired
    private StringRedisTemplate redisTemplate;





}
