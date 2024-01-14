package com.example.demo.service;

import com.example.demo.error.ErrorCode;
import com.example.demo.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

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
