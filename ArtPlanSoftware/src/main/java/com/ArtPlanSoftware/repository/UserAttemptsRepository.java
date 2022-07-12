package com.ArtPlanSoftware.repository;

import com.ArtPlanSoftware.entity.UserAttemptsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttemptsRepository extends JpaRepository<UserAttemptsEntity, Long> {

    UserAttemptsEntity findByUsername(String username);
}
