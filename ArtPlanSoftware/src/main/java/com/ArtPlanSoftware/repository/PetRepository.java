package com.ArtPlanSoftware.repository;

import com.ArtPlanSoftware.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {

    boolean existsByName(String name);
}
