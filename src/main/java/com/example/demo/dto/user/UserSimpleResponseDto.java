//package com.example.demo.dto.user;
//
//import com.example.demo.entity.UserEntity;
//import lombok.Getter;
//
//import java.util.Objects;
//
//@Getter
//public class UserSimpleResponseDto {
//    private String userName;
//    private String uid;
//
//
//    public UserSimpleResponseDto(UserEntity userEntity, String uid){
//        this.userName = userEntity.getUserName();
//        if (Objects.equals(uid, userEntity.getUid())) {
//            this.uid = userEntity.getUid();
//        }
//    }
//
//}
