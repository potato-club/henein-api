package kr.henein.api.dto.captcha;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class CaptchaResponseDto {
    private boolean success;
    private Timestamp challenge_ts;
    private String hostname;
    @JsonProperty("error-codes")
    private List<String> errorList;
}
