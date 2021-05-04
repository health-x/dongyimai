package com.health;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/applicationContext-redis.xml")
public class testAutowaird {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1(){
        System.out.println(redisTemplate);
        redisTemplate.boundHashOps("num").put("1","1111");
    }
}