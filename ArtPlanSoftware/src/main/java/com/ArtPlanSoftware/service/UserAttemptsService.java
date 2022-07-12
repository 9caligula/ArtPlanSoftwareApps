package com.ArtPlanSoftware.service;

import com.ArtPlanSoftware.entity.UserAttemptsEntity;

public interface UserAttemptsService {

    void updateAttempts(String username);

    void resetAttempts(String username);

    UserAttemptsEntity getAttempts(String username);
}
