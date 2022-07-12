package com.ArtPlanSoftware.repository;

import com.ArtPlanSoftware.entity.TypeOfPetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfPetsRepository extends JpaRepository<TypeOfPetsEntity, Long> {

    boolean existsByName(String name);
}
