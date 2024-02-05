package com.example.demo.repository;

import com.example.demo.entity.UserCharEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCharRepository extends JpaRepository<UserCharEntity,Long> {

    UserCharEntity findByUserEntityAndId(UserEntity userEntity,Long id);
    UserCharEntity findByUserEntityAndPickByUser(UserEntity userEntity, boolean pickByUser);
    List<UserCharEntity> findAllByUserEntity(UserEntity userEntity);

}
