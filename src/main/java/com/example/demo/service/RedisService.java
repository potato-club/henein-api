package com.example.demo.service;

import com.example.demo.enumCustom.RedisWork;
import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisCacheManager cacheManager;
    private final StringRedisTemplate stringRedisTemplate;

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
    //========Mail 서비스 관련========================
    //썩을 deleteExistingOtp 때문에 역매핑을 저장하고 처리했음!!
    public String getEmailOtpData(String OTP) {
        String email = stringRedisTemplate.opsForValue().get(OTP+":requestEmail");
        if (email == null) {
            throw new UnAuthorizedException("잘못된 OTP입니다.", ErrorCode.INVALID_ACCESS);
        }
        return email;
    }
    public void setEmailOtpDataExpire(String requestEmail,String OTP, int duration) {
        stringRedisTemplate.opsForValue().set(requestEmail,OTP,duration,TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(OTP+":requestEmail",requestEmail,duration,TimeUnit.MINUTES); //역매핑
    }
    public void deleteEmailOtpData(String OTP) {
        String requestEmail = stringRedisTemplate.opsForValue().get(OTP+":requestEmail");
        stringRedisTemplate.delete(requestEmail);
        stringRedisTemplate.delete(OTP+":requestEmail");
    }
    public void deleteExistingOtp(String requestEmail) {
        String OTP = stringRedisTemplate.opsForValue().get(requestEmail);
        if (OTP != null) {
            stringRedisTemplate.delete(requestEmail);
            stringRedisTemplate.delete(OTP+":requestEmail");
        }
    }
    public void setReadyEmailForSignUp(String email, String token) {
        stringRedisTemplate.opsForValue().set(email,token, 5, TimeUnit.MINUTES);
    }
    public boolean emailIsAlreadyReadied(String email) {
        if (stringRedisTemplate.opsForValue().get(email) != null){
            return true;
        }
        return false;
    }
    public boolean verifySignUpRequest(String email, String requestAT) {
        String providedAT= stringRedisTemplate.opsForValue().get(email);
        if ( !providedAT.equals(requestAT) ) {
            return false;
        }
        return true;
    }

}
