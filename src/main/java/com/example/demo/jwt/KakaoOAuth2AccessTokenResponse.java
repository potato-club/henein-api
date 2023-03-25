package com.example.demo.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoOAuth2AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("id_token")
    private String idToken; // 이게 없어서 에러나길래 넣고 위에 ignoreUnknown =true 처리함
}
/**
 * 에러 note
 * UnrecognizedPropertyException: Unrecognized field "access_token" 에러 발생으로
 * @JsonProperty를 사용하여 JSON 응답의 필드 이름과 Java 필드 이름 간의 매핑을 명시적으로 지정해야 합니다.
 */
