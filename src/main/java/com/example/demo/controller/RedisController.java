package com.example.demo.controller;

import com.example.demo.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;


@RestController
public class RedisController {

    public static final Logger log = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private RedisUtils redisUtils;
    //充当数据库
    private static ConcurrentHashMap<String, String> testMap = new ConcurrentHashMap<>();


    @RequestMapping(value = "/hello/{id}")
    public String hello(@PathVariable(value = "id") String id) {

        for (int i = 0; i < 10; i++) {
            testMap.put(i + "", i + "");
        }
        //查询缓存中是否存在
        boolean hasKey = redisUtils.hasKey(id);
        String str = "";
        if (hasKey) {
            //获取缓存
            Object object = redisUtils.get(id);
            log.info("从缓存获取的数据" + object);
            str = object.toString();
        } else {
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            str = testMap.get(id);
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtils.set(id, str, -1);
            log.info("数据插入缓存" + str);
        }
        return str;
    }
}
