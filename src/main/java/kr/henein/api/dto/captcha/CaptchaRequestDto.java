package kr.henein.api.dto.captcha;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CaptchaRequestDto {
    private String secret;
    private String response;
}
