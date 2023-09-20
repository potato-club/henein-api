package com.example.demo.service;

import com.example.demo.enumCustom.RedisWork;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisService {
    private final RedisCacheManager cacheManager;
    @Value("${cacheName.getCache}")
    private String redisName;

    public String setWorkStatus(String userCharacter) {
        cacheManager.getCache(redisName).put(userCharacter,RedisWork.WORK.getTitle());
        return "good, wait few second for update ";
    }

    public String updateWork(String userCharacter) {
        if (cacheManager.getCache(redisName).get(userCharacter,String.class) != null) {
            cacheManager.getCache(redisName).put(userCharacter,RedisWork.DONE.getTitle());
        } else {
            throw new RuntimeException();
        }
        return "200ok";
    }
    public boolean checkRedis(String userCharacter){
        if(null==cacheManager.getCache(redisName).get(userCharacter,String.class)){
            return false;
        }
        return true;
    }

}
