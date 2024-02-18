package kr.henein.api.dto.user;

import kr.henein.api.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDetailInfoResponseDto {
    private String userName;
    private String userEmail;
    private String imageUrl;
    private LocalDate signUpDate;
    private long boardCount;
    private long commentCount;

    public UserDetailInfoResponseDto(UserEntity userEntity, String imageUrl, long boardCount, long commentCount) {
        this.userName = userEntity.getUserName();
        this.userEmail = userEntity.getUserEmail();
        this.imageUrl = imageUrl;
        this.signUpDate = LocalDate.from(userEntity.getCreatedDate());
        this.boardCount = boardCount;
        this.commentCount = commentCount;
    }

}
