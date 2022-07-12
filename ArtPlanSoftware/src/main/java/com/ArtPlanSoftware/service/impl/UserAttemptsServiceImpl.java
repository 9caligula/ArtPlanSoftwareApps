package com.ArtPlanSoftware.service.impl;

import com.ArtPlanSoftware.entity.UserAttemptsEntity;
import com.ArtPlanSoftware.entity.UserEntity;
import com.ArtPlanSoftware.repository.UserAttemptsRepository;
import com.ArtPlanSoftware.repository.UserRepository;
import com.ArtPlanSoftware.service.UserAttemptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserAttemptsServiceImpl implements UserAttemptsService {

    private final static int MAX_ATTEMPTS = 3;
    private static final long HOUR = 3600000;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAttemptsRepository userAttemptsRepository;

    @Override
    public UserAttemptsEntity getAttempts(String username) {
        return userAttemptsRepository.findByUsername(username);
    }

    @Override
    public void updateAttempts(String username) {
        UserAttemptsEntity userAttempts = userAttemptsRepository.findByUsername(username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User with username " + "'" + username + "'" + " not found!"));

        if (userAttempts == null) {
            userAttempts = new UserAttemptsEntity(username, 1, new Date());
            userAttemptsRepository.save(userAttempts);
        } else {
            if (new Date().getTime() - userAttempts.getLastModified().getTime() >= HOUR) {
                userAttempts.setAttempts(0);
                userAttempts.setLastModified(new Date());
            }
            userAttempts.setAttempts(userAttempts.getAttempts() + 1);
            userAttemptsRepository.save(userAttempts);
            if (userAttempts.getAttempts() > MAX_ATTEMPTS) {
                user.setAccountLocked(true);
                userRepository.save(user);
                throw new LockedException("User with username " + "'" + username + "'" + " was locked right now!");
            }
        }
    }

    @Override
    public void resetAttempts(String username) {
        UserAttemptsEntity userAttempts = userAttemptsRepository.findByUsername(username);
        if (userAttempts != null) {
            userAttempts.setAttempts(0);
            UserEntity user = userRepository.findByUsername(username).get();
            user.setAccountLocked(false);
            userRepository.save(user);
            userAttemptsRepository.save(userAttempts);
        }
    }
}
