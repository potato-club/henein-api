package com.example.demo.service;

import com.example.demo.enumCustom.RedisWork;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public String setWorkStatus(String userCharacter) {
        redisTemplate.opsForValue().set(userCharacter, RedisWork.WORK.getTitle(),1, TimeUnit.HOURS);
        return "good, wait few second for update ";
    }

    public String updateWork(String userCharacter) {
        redisTemplate.opsForValue().getAndSet(userCharacter,RedisWork.DONE.getTitle());
        return "good";
    }
    public String checkRedis(String userCharacter){
        return redisTemplate.opsForValue().get(userCharacter);
    }

}
