package kr.henein.api.service;

import kr.henein.api.error.ErrorCode;
import kr.henein.api.error.exception.UnAuthorizedException;
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
    public void setReadyEmailForSignUp(String email, String token) {
        stringRedisTemplate.opsForValue().set(email,token, 2, TimeUnit.MINUTES);
    }
    public boolean emailIsAlreadyReadied(String email) {
        return stringRedisTemplate.opsForValue().get(email) != null;
    }
    public boolean verifySignUpRequest(String email, String requestAT) {
        String providedAT= stringRedisTemplate.opsForValue().get(email);
        return providedAT.equals(requestAT) && requestAT != null;
    }
    //==================캐릭터 갱신 관련=========================//
    //key = email:characterLong = 1 or email:all = 1

    public boolean onCoolTimeToSingleRefreshOfCharacter(long charId, String email) {
        String key = "char:" +charId;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null){
            stringRedisTemplate.opsForValue().set(key,email,1,TimeUnit.HOURS);
            return false;
        }else {
            return true;
        }
    }
    public boolean onCoolTimeToEntireRefreshOfCharacters(String email) {
        String key = "char:"+email;
        String value = stringRedisTemplate.opsForValue().get(key);
        if ( value == null ){
            stringRedisTemplate.opsForValue().set(key,"1",1,TimeUnit.HOURS);
            return false;
        } else {
            return true;
        }
    }

}
