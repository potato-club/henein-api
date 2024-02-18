package kr.henein.api.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class KakaoOAuth2User {
    @JsonProperty("id") // kakao Client 파일의 75번 줄에서 넘어오는 JSON 데이터 중 Id를 캐치하기 위해서
    private Long userid;
    private String email;
    @JsonProperty("connected_at") // kakao Client 파일의 75번 줄에서 넘어오는 JSON 데이터 중 connect_at을 캐치하기 위해서
    private Date connectAt;
    private KakaoAccount kakao_account;
}
