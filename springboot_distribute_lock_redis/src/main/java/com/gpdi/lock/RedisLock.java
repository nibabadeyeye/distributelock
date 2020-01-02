package com.gpdi.lock;

import com.gpdi.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class RedisLock {


    /**
     * key :产品Id
     * <p>
     * value:一个唯一的时间戳
     */
    public boolean requireLock(String key, String value) {


        //尝试setKey
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            System.out.println("直接获取到锁");
            return true;
        }


        //在Redis获取当前值
        Object current_value = redisTemplate.opsForValue().get(key);

        if (!StringUtils.ObjectToString(current_value).equals("") && Long.parseLong(current_value.toString()) < System.currentTimeMillis()) {//①

            System.out.println("进一步竞争锁！！！");
            String old_value = redisTemplate.opsForValue().getAndSet(key, value).toString();

            if (!StringUtils.ObjectToString(old_value).equals("") && old_value.equals(current_value)) {
                return true;
            }
        }
        return false;
    }


    //线程解锁，删除key
    public void unlock(String key, String value) {
        if (StringUtils.ObjectToString(redisTemplate.opsForValue().get(key)).equals(value)) {
            redisTemplate.opsForValue().getOperations().delete(key);
            System.out.println("释放锁成功");
        }

    }


    private RedisTemplate redisTemplate;

    public RedisLock(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
