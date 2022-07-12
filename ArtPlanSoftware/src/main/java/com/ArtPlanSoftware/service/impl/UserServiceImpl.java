package com.ArtPlanSoftware.service.impl;

import com.ArtPlanSoftware.entity.UserEntity;
import com.ArtPlanSoftware.exception.UserAlreadyExistsException;
import com.ArtPlanSoftware.repository.UserRepository;
import com.ArtPlanSoftware.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("User " + "'" + username + "'" + " with such a username already exists!",
                    HttpStatus.CONFLICT);
        } else {
            return false;
        }
    }
}
