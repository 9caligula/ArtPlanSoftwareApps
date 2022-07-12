package com.ArtPlanSoftware.service;

import com.ArtPlanSoftware.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity getUserByUsername(String username);

    UserEntity saveUser(UserEntity user);

    List<UserEntity> getAll();

    boolean existsByUsername(String username);
}
